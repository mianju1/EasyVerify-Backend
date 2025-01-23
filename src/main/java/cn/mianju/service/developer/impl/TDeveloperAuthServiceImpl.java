package cn.mianju.service.developer.impl;

import cn.mianju.annotation.FlowLimit;
import cn.mianju.entity.dto.TDeveloper;
import cn.mianju.entity.vo.request.developer.ConfirmResetVO;
import cn.mianju.entity.vo.request.developer.EmailRegisterVO;
import cn.mianju.entity.vo.request.developer.EmailResetVO;
import cn.mianju.entity.vo.response.developer.DeveloperChangeEmailVO;
import cn.mianju.exception.RequestFrequencyException;
import cn.mianju.mapper.TDeveloperMapper;
import cn.mianju.service.developer.TDeveloperAuthService;
import cn.mianju.utils.Const;
import cn.mianju.utils.FlowUtils;
import cn.mianju.utils.JwtUtils;
import cn.mianju.utils.SnowflakeIdGenerator;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * @author 10408
 * @description 针对表【t_developer】的数据库操作Service实现
 * @createDate 2024-12-10 14:01:26
 */
@Service
public class TDeveloperAuthServiceImpl extends ServiceImpl<TDeveloperMapper, TDeveloper>
        implements TDeveloperAuthService {
    //验证邮件发送冷却时间限制，秒为单位
    @Value("${spring.web.verify.mail-limit}")
    int verifyLimit;

    // jwt 工具类
    @Resource
    JwtUtils jwtUtils;

    // 限流
    @Resource
    FlowUtils flow;

    // rabbitmq
    @Resource
    AmqpTemplate rabbitTemplate;

    // redis
    @Resource
    StringRedisTemplate stringRedisTemplate;

    // 加密器
    @Resource
    PasswordEncoder passwordEncoder;

    // 雪花算法生成器
    @Resource
    SnowflakeIdGenerator generator;

    @Resource
    HttpServletRequest request;

    /**
     * 从数据库中通过用户名或邮箱查找用户详细信息
     * 这是重写的SpringSecurity提供的方法 登录请求后会执行
     *
     * @param username 用户名
     * @return 用户详细信息
     * @throws RuntimeException 如果用户未找到或频率过快则抛出此异常
     */
    @FlowLimit
    @Override
    public UserDetails loadUserByUsername(String username) throws RuntimeException {
        // 登录接口用自定义限制频率，防止恶意请求 10秒内5次请求，周期30秒
        TDeveloper developer = this.findAccountByNameOrEmail(username);
        if (developer == null) {
            throw new UsernameNotFoundException("用户不存在，请检查用户名");
        }
        return User
                .withUsername(username)
                .password(developer.getDPassword())
                .roles(Const.ROLE_DEVELOPER)
                .build();
    }

    /**
     * 更改账户绑定邮箱
     *
     * @param vo DeveloperChangeEmailVO 对象，包含原邮箱、新邮箱、验证码、请求IP地址等信息
     * @return 操作结果，null表示正常，否则为错误原因
     * @throws RequestFrequencyException 请求过于频繁时抛出此异常
     */
    @FlowLimit
    @Override
    public String changeBindEmail(DeveloperChangeEmailVO vo) {
        // 检查新旧邮箱是否相同
        if (vo.getEmail().equals(vo.getNewEmail())) return "新旧邮箱相同，无需更改";

        // 获取原邮箱的验证码
        String code = this.getEmailVerifyCode(vo.getEmail());

        // 检查验证码是否为空
        if (code == null) return "请先获取验证码";

        // 检查验证码是否正确
        if (!code.equals(vo.getCode())) return "验证码错误，请重新输入";

        // 检查原邮箱是否存在
        if (!this.existsAccountByEmail(vo.getEmail())) return "原邮箱不存在";

        // 检查新邮箱是否已被占用
        if (this.existsAccountByEmail(vo.getNewEmail())) return "新邮箱已被占用";

        // 根据原邮箱查找账户
        TDeveloper developer = this.findAccountByNameOrEmail(vo.getEmail());
        // 更新账户的邮箱
        developer.setDMail(vo.getNewEmail());

        // 更新数据库中的邮箱信息
        boolean update = this.update().eq("d_mail", vo.getEmail())
                .set("d_mail", vo.getNewEmail())
                .update();

        // 返回操作结果
        return update ? null : "更新失败，请联系管理员";
    }


    /**
     * 生成注册验证码存入Redis中，并将邮件发送请求提交到消息队列等待发送
     *
     * @param type    类型
     * @param email   邮件地址
     * @param address 请求IP地址
     * @return 操作结果，null表示正常，否则为错误原因
     */
    @FlowLimit
    @Override
    public String registerEmailVerifyCode(String type, String email, String address) {
        // 判断验证码类型
        if (this.existsAccountByEmail(email) && "register".equals(type)) return "用户已存在，注册失败";
        if (!this.existsAccountByEmail(email) && ("reset".equals(type) || "change".equals(type))) return "用户不存在，重置失败";

        synchronized (address.intern()) {
            if (!this.verifyLimit(address)) return "请求频繁，请稍后再试";
            Random random = new Random();
            // 生成随机6位数
            int code = random.nextInt(899999) + 100000;
            // 丢进消息队列中
            Map<String, Object> data = Map.of("type", type, "email", email, "code", code);
            rabbitTemplate.convertAndSend(Const.MQ_MAIL, data);
            // 验证码存入 redis 中以验证用户 设置5分钟失效
            stringRedisTemplate.opsForValue()
                    .set(Const.VERIFY_EMAIL_DATA + email, String.valueOf(code), Const.VERIFY_EMAIL_LIMIT_TIME, TimeUnit.MINUTES);
            return null;
        }
    }

    /**
     * 邮件验证码注册账号操作，需要检查验证码是否正确以及邮箱、用户名是否存在重名
     *
     * @param vo 注册基本信息
     * @return 操作结果，null表示正常，否则为错误原因
     */
    @FlowLimit
    @Override
    public String registerEmailAccount(EmailRegisterVO vo) {
        String email = vo.getEmail();
        String code = this.getEmailVerifyCode(email);

        // 判断验证码是否正确
        if (code == null) return "请先获取验证码";
        if (!code.equals(vo.getCode())) return "验证码错误，请重新输入";

        // 判断用户名是否可用
        String username = vo.getUsername();
        if (this.existsAccountByUsername(username)) return "该用户名已被他人使用，请重新更换";

        // 可以创建账户
        String password = passwordEncoder.encode(vo.getPassword());
        String deveId = "dev" + generator.nextId();
        TDeveloper developer = new TDeveloper();
        developer.setDId(deveId);
        developer.setDName(username);
        developer.setDPassword(password);
        developer.setDMail(email);

        if (!this.save(developer)) {
            return "内部错误，注册失败";
        } else {
            this.deleteEmailVerifyCode(email);
            return null;
        }
    }

    /**
     * 邮件验证码重置密码操作，需要检查验证码是否正确
     *
     * @param info 重置基本信息
     * @return 操作结果，null表示正常，否则为错误原因
     */
    @FlowLimit
    @Override
    public String resetEmailAccountPassword(EmailResetVO info) {
        String verify = resetConfirm(new ConfirmResetVO(info.getEmail(), info.getCode()));
        if (verify != null) return verify;
        String email = info.getEmail();
        String password = passwordEncoder.encode(info.getPassword());
        boolean update = this.update().eq("d_mail", email).set("d_password", password).update();

        if (update) {
            this.deleteEmailVerifyCode(email);

            // 如果用户已经登录，则注销其JWT令牌
            String headerToken = request.getHeader("Authorization");
            if (!headerToken.isEmpty()) jwtUtils.invalidateJwt(headerToken);
        }
        return update ? null : "更新失败，请联系管理员";
    }

    /**
     * 重置密码确认操作，验证验证码是否正确
     *
     * @param info 验证基本信息
     * @return 操作结果，null表示正常，否则为错误原因
     */
    @FlowLimit
    @Override
    public String resetConfirm(ConfirmResetVO info) {
        String email = info.getEmail();
        String code = this.getEmailVerifyCode(email);
        if (code == null) return "请先获取验证码";
        if (!code.equals(info.getCode())) return "验证码错误，请重新输入";
        return null;
    }


    /**
     * 通过用户名或邮件地址查找用户
     *
     * @param text 用户名或邮件
     * @return 账户实体
     */
    @Override
    public TDeveloper findAccountByNameOrEmail(String text) {
        return this.query()
                .eq("d_name", text).or()
                .eq("d_mail", text)
                .one();
    }

    /**
     * 移除Redis中存储的邮件验证码
     *
     * @param email 电邮
     */
    private void deleteEmailVerifyCode(String email) {
        String key = Const.VERIFY_EMAIL_DATA + email;
        stringRedisTemplate.delete(key);
    }

    /**
     * 获取Redis中存储的邮件验证码
     *
     * @param email 电邮
     * @return 验证码
     */
    private String getEmailVerifyCode(String email) {
        String key = Const.VERIFY_EMAIL_DATA + email;
        return stringRedisTemplate.opsForValue().get(key);
    }

    /**
     * 查询指定邮箱的用户是否已经存在
     *
     * @param email 邮箱
     * @return 是否存在
     */
    private boolean existsAccountByEmail(String email) {
        return this.baseMapper.exists(Wrappers.<TDeveloper>query().eq("d_mail", email));
    }

    /**
     * 查询指定用户名的用户是否已经存在
     *
     * @param username 用户名
     * @return 是否存在
     */
    private boolean existsAccountByUsername(String username) {
        return this.baseMapper.exists(Wrappers.<TDeveloper>query().eq("d_name", username));
    }

    /**
     * 针对IP地址进行邮件验证码获取限流
     *
     * @param address 地址
     * @return 是否通过验证
     */
    private boolean verifyLimit(String address) {
        String key = Const.VERIFY_EMAIL_LIMIT + address;
        return flow.limitOnceCheck(key, verifyLimit);
    }

}




