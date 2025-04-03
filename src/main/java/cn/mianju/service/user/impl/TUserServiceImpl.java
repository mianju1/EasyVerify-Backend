package cn.mianju.service.user.impl;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.mianju.annotation.FlowLimit;
import cn.mianju.entity.EncryptInfo;
import cn.mianju.entity.RestBean;
import cn.mianju.entity.dto.TSoftware;
import cn.mianju.entity.dto.TUser;
import cn.mianju.entity.view.VCodeinfo;
import cn.mianju.entity.view.VEncryptinfo;
import cn.mianju.entity.view.VInterfaceinfo;
import cn.mianju.entity.view.VUserinfo;
import cn.mianju.entity.vo.request.api.*;
import cn.mianju.entity.vo.response.ResponseVO;
import cn.mianju.mapper.TUserMapper;
import cn.mianju.service.code.VCodeinfoService;
import cn.mianju.service.code.impl.TCodeServiceImpl;
import cn.mianju.service.encrypt.VEncryptinfoService;
import cn.mianju.service.software.TSoftwareService;
import cn.mianju.service.user.TUserService;
import cn.mianju.service.user.VUserinfoService;
import cn.mianju.service.web.VInterfaceinfoService;
import cn.mianju.strategy.function.FunctionContext;
import cn.mianju.utils.EncryptUtils;
import cn.mianju.utils.RedisUtils;
import cn.mianju.utils.SnowflakeIdGenerator;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.validation.annotation.Validated;

import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class TUserServiceImpl extends ServiceImpl<TUserMapper, TUser>
        implements TUserService {

    @Value("${spring.web.verify.user-id-code-second}")
    private Integer userIdCodeSec;

    @Resource
    SnowflakeIdGenerator snowflakeIdGenerator;

    @Resource
    VUserinfoService vUserinfoService;
    @Resource
    VInterfaceinfoService vInterfaceinfoService;
    @Resource
    VEncryptinfoService vEncryptinfoService;
    @Resource
    VCodeinfoService vCodeinfoService;
    @Resource
    TCodeServiceImpl tCodeService;
    @Resource
    TSoftwareService tSoftwareService;

    @Resource
    TransactionTemplate transactionTemplate;

    @Resource
    RedisUtils redisUtils;


    @Override
    public RestBean<?> routeFunction(String jsonBody, String url) throws Exception {
        // 查询url所属软件id
        VInterfaceinfo interfaceInfo = vInterfaceinfoService.query()
                .select("s_codetype", "encryption", "s_verifymac", "s_id", "i_function")
                .eq("i_url", url).one();
        Integer iFunction = interfaceInfo.getIFunction();
        String sId = interfaceInfo.getSId();
        Integer encryption = interfaceInfo.getEncryption();

        EncryptInfo encryptInfo;
        String body = jsonBody;
        if (!encryption.equals(0)) {
            // 获取加密信息
            encryptInfo = this.getEncryptInfo(sId, encryption);
            if (Objects.nonNull(jsonBody) && !jsonBody.isBlank()) {
                try {
                    body = EncryptUtils.decryptRsa(jsonBody.strip(), encryptInfo.getPrivateKey());
                } catch (Exception e) {
                    return RestBean.failure(400, "参数错误");
                }
            }
        } else {
            body = jsonBody;
            encryptInfo = null;
        }

        // 策略模式执行
        FunctionContext functionContext = new FunctionContext();

        return functionContext.executeStrategy(iFunction, this, body, interfaceInfo, encryptInfo);
    }

    @FlowLimit
    @Override
    public String registerUser(@Validated RegisterUserVO vo, VInterfaceinfo interfaceInfo) throws NoSuchAlgorithmException {
        if (Objects.isNull(vo) || vo.getUsername().isBlank() || vo.getPassword().isBlank()) return "参数不能为空!";

        String username = vo.getUsername();
        String password = EncryptUtils.sha256Encode(vo.getPassword());
        String registerCode = vo.getCode();
        Integer codeType = interfaceInfo.getSCodetype();
        String sId = interfaceInfo.getSId();

        if (codeType.equals(2)) return "激活码用户不需要注册，请直接登录使用";


        // 查询是否重复存在用户
        List<VUserinfo> userInfoList = vUserinfoService.query()
                .select("u_id")
                .eq("u_name", username)
                .eq("s_id", sId).list();

        if (!userInfoList.isEmpty()) return "用户已存在";

        String cId;
        Integer cType;
        // 如果需要注册码则验证注册码
        if (codeType.equals(1)) {
            if (Objects.isNull(registerCode) || registerCode.isEmpty()) return "邀请码不能为空";
            VCodeinfo codeInfo = vCodeinfoService.query()
                    .select("c_id", "c_timetype")
                    .eq("s_id", sId)    // 查询指定软件id
                    .eq("c_code", registerCode) // 查询指定激活码
                    .and(wrapper -> {   // 时间过期则不满足
                        wrapper.gt("c_expired", new Date())
                                .or()
                                .isNull("c_expired");
                    })
                    .and(wrapper -> {   // 用户名不符合则不满足
                        wrapper.eq("c_name", username)
                                .or()
                                .isNull("c_name");
                    }).one();

            if (Objects.isNull(codeInfo)) return "激活码错误或已过期";

            cId = codeInfo.getCId();
            cType = codeInfo.getCTimetype();
        } else {
            cType = null;
            cId = null;
        }

        TUser tUser = new TUser();
        tUser.setSId(sId);
        tUser.setUId(snowflakeIdGenerator.nextId() + "");
        tUser.setUName(username);
        tUser.setUPassword(password);

        // 开启事务，统一添加用户和修改注册码信息
        Boolean execute = transactionTemplate.execute(status -> {
            try {
                // 更新注册码信息
                if (!Objects.isNull(cType) && !Objects.isNull(cId)) {
                    tCodeService.update()
                            .eq("c_id", cId)
                            .set("c_name", username)
                            .set("c_usetime", new Date())
                            .set("c_expired", tCodeService.getExpireTime(cType))
                            .update();
                }
                // 添加用户信息
                this.save(tUser);

                return true;

            } catch (Exception e) {
                status.setRollbackOnly(); // 标记事务回滚
                throw e;
            }
        });

        return execute ? null : "注册失败";
    }

    @FlowLimit
    @Override
    public RestBean<String> loginUser(@Validated LoginUserVO vo, VInterfaceinfo interfaceInfo) throws NoSuchAlgorithmException {
        String username = vo.getUsername();
        String password = EncryptUtils.sha256Encode(vo.getPassword());
        String sId = interfaceInfo.getSId();
        Integer sCodetype = interfaceInfo.getSCodetype();


        if (sCodetype.equals(2)) return RestBean.failure(400, "激活码请使用 激活码方式 登录");


        VUserinfo userinfo = vUserinfoService.query()
                .select("u_id", "u_status", "c_score", "c_name")
                .eq("u_name", username)
                .eq("u_password", password)
                .eq("s_id", sId)
                .one();

        if (Objects.isNull(userinfo)) return RestBean.failure(400, "用户名或密码错误");
        if (sCodetype.equals(1) && this.isExpiredAccount(username, sId)) return RestBean.failure(400, "账号已过期");
        if (userinfo.getUStatus().equals(0)) return RestBean.failure(400, "当前账号已被限制,请联系管理员");
        if (sCodetype.equals(2) && userinfo.getCScore().equals(0)) return RestBean.failure(400, "积分不足，登录失败");

        // 生成id码，并返回
        String idCode = createIdCode();
        boolean b = setIdCode(sId, username, idCode);

        return b ? RestBean.success(idCode, "登陆成功") : RestBean.failure(400, "登录失败");
    }

    @FlowLimit
    @Override
    public String loginCode(LoginCodeVO vo, VInterfaceinfo interfaceInfo) {
        String code = vo.getCode().strip();
        String mac = vo.getMac();
        String sId = interfaceInfo.getSId();
        Integer sVerifyMac = interfaceInfo.getSVerifyMac();

        if (Objects.isNull(mac)) return "参数有误";
        if (!interfaceInfo.getSCodetype().equals(2)) return "非激活码用户请使用账号登录";

        VCodeinfo one = vCodeinfoService.query()
                .select("c_id", "c_score", "c_name", "c_usetime", "c_timetype")
                .eq("s_id", sId)
                .eq("c_code", code)
                .one();


        if (Objects.isNull(one)) return "激活码有误";
        boolean isUsed = !Objects.isNull(one.getCUsetime());

        if (sVerifyMac.equals(1) && (!Objects.isNull(one.getCName()) && !mac.equals(one.getCName())))
            return "机器码有误";
        if (one.getCScore().equals(0)) return "当前激活码已被禁用，请联系相关人员";
        if (isUsed && isExpiredCode(code, sId)) return "激活码已过期";

        boolean update = true;

        if (!isUsed || sVerifyMac.equals(1)) {
            // 修改激活码信息
            update = tCodeService.update()
                    .eq("c_id", one.getCId())
                    .set(sVerifyMac.equals(1), "c_name", mac)
                    .set(!isUsed, "c_usetime", new Date())
                    .set(!isUsed, "c_expired", tCodeService.getExpireTime(one.getCTimetype()))
                    .update();
        }

        return update ? null : "登录失败";
    }

    @FlowLimit
    @Override
    public ResponseVO getVersion(VInterfaceinfo interfaceInfo) {
        String sId = interfaceInfo.getSId();

        TSoftware one = tSoftwareService.query()
                .select("s_id", "s_version")
                .eq("s_id", sId)
                .one();

        ResponseVO responseVO = new ResponseVO();

        if (Objects.isNull(one)) {
            responseVO.setMessage("请求失败");
            responseVO.setSuccess(false);
            return null;
        }

        responseVO.setData(one.getSVersion());

        return responseVO;
    }

    @FlowLimit
    @Override
    public ResponseVO getNotice(VInterfaceinfo interfaceInfo) {
        String sId = interfaceInfo.getSId();
        TSoftware one = tSoftwareService.query()
                .select("s_id", "s_notice")
                .eq("s_id", sId)
                .one();

        ResponseVO responseVO = new ResponseVO();

        if (Objects.isNull(one)) {
            responseVO.setMessage("请求失败");
            responseVO.setSuccess(false);
            return responseVO;
        }
        String sNotice = one.getSNotice();
        responseVO.setData(sNotice);
        return responseVO;
    }

    @FlowLimit
    @Override
    public ResponseVO getExpireTime(SoftwareKeyVO vo, VInterfaceinfo interfaceInfo) {
        String sId = interfaceInfo.getSId();
        String username = vo.getUsername();
        String code = vo.getCode();

        VCodeinfo one = vCodeinfoService.query()
                .select("c_id", "c_expired")
                .eq("s_id", sId)
                .eq(!Objects.isNull(username), "c_name", username)
                .eq(!Objects.isNull(code), "c_code", code)
                .one();

        ResponseVO responseVO = new ResponseVO();
        if (Objects.isNull(one)) {
            responseVO.setMessage("用户或码不存在");
            return responseVO;
        }
        responseVO.setData(one.getCExpired() == null ? "未激活" : one.getCExpired());
        return responseVO;
    }

    @FlowLimit
    @Override
    public ResponseVO getIsNewVersion(SoftwareKeyVO vo, VInterfaceinfo interfaceInfo) {
        String sId = interfaceInfo.getSId();
        String version = vo.getVersion();

        TSoftware one = tSoftwareService.query()
                .select("s_version")
                .eq("s_id", sId)
                .one();

        ResponseVO responseVO = new ResponseVO();
        if (Objects.isNull(one)) {
            responseVO.setData(null);
            responseVO.setMessage("请求失败");
            responseVO.setSuccess(false);
        }

        if (version.equals(one.getSVersion())) {
            responseVO.setData(true);
        } else {
            responseVO.setData(false);
        }

        return responseVO;

    }

    @FlowLimit
    @Override
    public String updateUserPassword(UpdateUserPasswordVO vo, VInterfaceinfo interfaceInfo) throws NoSuchAlgorithmException {
        String sId = interfaceInfo.getSId();
        String username = vo.getUsername();
        String idCode = vo.getIdCode();
        // 校验参数
        if (StrUtil.isEmptyIfStr(sId) || StrUtil.isEmptyIfStr(username) || StrUtil.isEmptyIfStr(idCode))
            return "参数不能为空";
        if (idCode.length() != 32) return "身份码不合法";

        // 敏感操作 需要校验身份码
        boolean checked = checkIdCode(sId, username, idCode);
        if (!checked) return "身份码校验失败";

        // 更新密码 与 身份码有效期
        String password = EncryptUtils.sha256Encode(vo.getPassword());
        updateIdCode(sId, username, idCode);

        boolean update = this.update()
                .eq("s_id", sId)
                .eq("u_name", username)
                .set("u_password", password)
                .update();

        return update ? null : "修改失败";
    }

    // todo 加一个查看当前用户是否有效的功能，使用idcode


    private EncryptInfo getEncryptInfo(String sid, Integer encryption) {
        VEncryptinfo one = vEncryptinfoService.query()
                .select("e_publickey", "e_privatekey")
                .eq("s_id", sid)
                .eq("encryption", encryption)
                .one();

        EncryptInfo encryptInfo = new EncryptInfo();
        encryptInfo.setPublicKey(one.getEPublickey());
        encryptInfo.setPrivateKey(one.getEPrivatekey());

        return encryptInfo;
    }

    private Boolean isExpiredAccount(String username, String sid) {
        VUserinfo userinfo = vUserinfoService.query()
                .select("c_code", "c_expired")
                .eq("u_name", username)
                .eq("s_id", sid).one();
        if (Objects.isNull(userinfo)) return true;
        Date date = userinfo.getCExpired();
        return date.before(new Date());
    }

    private Boolean isExpiredCode(String code, String sid) {
        VCodeinfo codeinfo = vCodeinfoService.query()
                .select("c_expired")
                .eq("c_code", code)
                .eq("s_id", sid).one();
        Date date = codeinfo.getCExpired();
        return date.before(new Date());
    }

    private TSoftware getSidBySkey(String key) {
        return tSoftwareService.query()
                .select("s_id", "s_codetype")
                .eq("s_key", key)
                .one();
    }

    /**
     * 身份码校验
     *
     * @param sid  软件唯一标识
     * @param name 用户名
     * @return
     */
    private boolean checkIdCode(String sid, String name, String idCode) {
        String key = String.format("Sid-%s-User-%s:IdCode", sid, name);
        // 从redis获取身份码
        String value = String.valueOf(redisUtils.get(key));

        // 获取不到或者空则说明身份码不存在或者过期
        return !StrUtil.isEmptyIfStr(value) && value.equals(idCode);
    }

    /**
     * 获取redis中身份码
     *
     * @param sid  软件唯一标识
     * @param name 用户名
     * @return
     */
    private String getIdCode(String sid, String name) {
        String key = String.format("Sid-%s-User-%s:IdCode", sid, name);
        // 从redis获取身份码
        String value = String.valueOf(redisUtils.get(key));

        // 获取不到或者空则说明身份码不存在或者过期
        return StrUtil.isEmptyIfStr(value) ? null : value;
    }

    /**
     * redis设置身份码
     *
     * @param sid    软件唯一标识
     * @param name   用户名
     * @param idCode 用户身份码
     * @return
     */
    private boolean setIdCode(String sid, String name, String idCode) {
        String key = String.format("Sid-%s-User-%s:IdCode", sid, name);
        log.info("redis设置身份码 - key:{} | value: {} ", key, idCode);
        // 设置身份码过期时间，默认为：4小时
        return redisUtils.set(key, idCode, userIdCodeSec);
    }


    /**
     * 更新身份码有效时间
     *
     * @param sid
     * @param name
     * @param idCode
     * @return
     */
    private boolean updateIdCode(String sid, String name, String idCode) {
        String key = String.format("Sid-%s-User-%s:IdCode", sid, name);
        // 更新身份码过期时间
        log.info("redis更新身份码过期时间 - key:{} | value: {} ", key, idCode);

        // 判断是否存在key，如果存在则更新过期时间
        if (redisUtils.hasKey(key)) {
            long expire = redisUtils.getExpire(key);
            if (expire > 0) {
                return redisUtils.expire(key, userIdCodeSec);
            }
        }
        return false;
    }

    /**
     * 创建身份码
     *
     * @return 身份码
     */
    private static String createIdCode() {
        // 生成随机32位大写字符串作为身份码
        return IdUtil.fastSimpleUUID().toUpperCase();
    }


}




