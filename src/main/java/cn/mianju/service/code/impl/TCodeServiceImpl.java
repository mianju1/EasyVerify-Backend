package cn.mianju.service.code.impl;

import cn.hutool.core.util.StrUtil;
import cn.mianju.annotation.FlowLimit;
import cn.mianju.entity.RestBean;
import cn.mianju.entity.dto.TCode;
import cn.mianju.entity.dto.TSoftware;
import cn.mianju.entity.vo.request.code.ActiveCodeMachineVO;
import cn.mianju.entity.vo.request.code.AddCodeVO;
import cn.mianju.entity.vo.request.code.DeleteCodeVO;
import cn.mianju.entity.vo.request.code.UpdateCodeVO;
import cn.mianju.entity.vo.request.user.DeleteUserVO;
import cn.mianju.entity.vo.request.user.UpdateUserVO;
import cn.mianju.mapper.TCodeMapper;
import cn.mianju.service.code.TCodeService;
import cn.mianju.service.software.TSoftwareService;
import cn.mianju.utils.Const;
import cn.mianju.utils.JwtUtils;
import cn.mianju.utils.SnowflakeIdGenerator;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author 10408
 * @description 针对表【t_code】的数据库操作Service实现
 * @createDate 2024-12-18 10:23:54
 */
@Service
public class TCodeServiceImpl extends ServiceImpl<TCodeMapper, TCode>
        implements TCodeService {

    @Resource
    JwtUtils jwtUtils;

    @Resource
    TSoftwareService softwareService;

    @Resource
    HttpServletRequest request;

    @Resource
    SnowflakeIdGenerator snowflakeIdGenerator;

    @FlowLimit
    @Override
    public String deleteCode(DeleteCodeVO vo) {
        List<String> code = vo.getCode();
        if (Objects.isNull(code) || code.isEmpty()) return "参数不能为空";

        String id = getUserId();

        // 查询该开发者下所有激活码
        List<TCode> codes = this.baseMapper.selectCodeFormDid(id);
        if (Objects.isNull(codes) || codes.isEmpty()) return "删除失败";

        // 构建删除条件，c_code 为要删除的激活码，且c_code 在开发者下所有激活码中
        QueryWrapper<TCode> wrapper = new QueryWrapper<>();
        wrapper.in("c_code", code)
                .in("c_code", codes.stream().map(TCode::getCCode).toList());

        // 删除激活码
        boolean remove = this.remove(wrapper);

        return remove ? null : "删除失败";
    }

    @FlowLimit
    @Override
    public RestBean<List<String>> addCode(AddCodeVO vo) {
        String id = getUserId();

        TSoftware one = softwareService.query()
                .select("s_codetype")
                .eq("s_id", vo.getSid())
                .eq("d_id", id)
                .one();

        // 如果软件不存在，则返回错误信息
        if (Objects.isNull(one)) return RestBean.failure(400, "软件不存在");

        // 如果软件类型不匹配，则返回错误信息
        if (!Objects.equals(one.getSCodetype(), vo.getCodeType()))
            return RestBean.failure(400, "软件类型不匹配");

        List<TCode> codes = new ArrayList<>();
        List<String> showList = new ArrayList<>();
        for (int i = 0; i < vo.getNum(); i++) {
            // 生成24位字符串UUID作为软件密钥key
            String key = UUID.randomUUID().toString().substring(0, 28).toUpperCase().replace("-", "");
            Date currentDate = new Date();

            TCode tCode = new TCode();
            tCode.setSId(vo.getSid());
            tCode.setCType(vo.getCodeType());
            tCode.setCTimetype(vo.getTimeType());
            tCode.setCId(snowflakeIdGenerator.nextId() + "");
            tCode.setCCode(key);
            tCode.setCScore(vo.getScores());
            tCode.setCCreatetime(currentDate);
            tCode.setCSpecialType(vo.getCodeSpecialType());
            tCode.setCSpecialExtraParams(vo.getExtraParams());

            if (vo.getIsActive()) {
                tCode.setCUsetime(currentDate);
                tCode.setCExpired(getExpireTime(vo.getTimeType()));
            }

            showList.add(key);
            codes.add(tCode);
        }

        // 批量插入激活码
        boolean b = this.saveBatch(codes);

        return b ? RestBean.success(showList) : RestBean.failure(400, "添加失败");
    }


    @FlowLimit
    @Override
    public String updateCode(UpdateCodeVO vo) {
        Boolean isActive = vo.getIsActive();

        Date expireTime = null;
        // 如果激活 则设置过期时间
        if (isActive) {
            if (vo.getUsername().isEmpty()) return "用户名不能为空";
            Integer timeType = this.query()
                    .eq("c_code", vo.getCode())
                    .one()
                    .getCTimetype();

            expireTime = getExpireTime(timeType);
        }

        // 更新激活码信息 内部使用
        boolean update = this.update()
                .eq("c_code", vo.getCode())
                .set(!Objects.isNull(vo.getScore()), "c_score", vo.getScore())
                .set(!Objects.isNull(vo.getUsername()), "c_name", vo.getUsername())
                .set(isActive, "c_usetime", new Date())
                .set(isActive, "c_expired", expireTime)
                .update();

        return update ? null : "更新失败";
    }

    @FlowLimit
    @Override
    public String updateCodeMachine(ActiveCodeMachineVO vo) {
        String id = getUserId();
        String machine = vo.getMachine();
        List<String> code = vo.getCode();

        if (Objects.isNull(code) || code.isEmpty()) return "参数不能为空";

        // 查询该开发者下所有激活码
        List<TCode> codes = this.baseMapper.selectCodeFormDid(id);
        if (Objects.isNull(codes) || codes.isEmpty()) return "更新失败";

        // 执行修改语句
        boolean update = this.update()
                .eq("c_type", Const.CODE_TYPE_ACTIVATION) // 写死因为2是激活码 只有激活码才能修改机器码
                .in("c_code", vo.getCode())
                .in("c_code", codes.stream().map(TCode::getCCode).toList())
                .set(!machine.isEmpty(), "c_name", machine)
                .set(machine.isEmpty(), "c_name", null)
                .update();


        return update ? null : "更新失败";
    }

    @FlowLimit
    @Override
    public String delUser(DeleteUserVO vo) {
        String userId = getUserId();
        List<String> codes = vo.getCode();
        String sid = vo.getSid();

        if (Objects.isNull(codes) || codes.isEmpty()) return "参数有误";

        // 查询该开发者下所有激活码
        List<TCode> codesByDid = this.baseMapper.selectCodeFormDid(userId);
        if (Objects.isNull(codesByDid) || codesByDid.isEmpty()) return "删除失败";

        QueryWrapper<TCode> wrapper = new QueryWrapper<>();
        wrapper.eq("s_id", sid)
                .in("c_code", codes)
                .isNotNull("c_expired")
                // 将List<TCode> 转换为List<String> 进行验证
                .in("c_code", codesByDid.stream().map(TCode::getCCode).toList());

        boolean remove = this.remove(wrapper);

        return remove ? null : "删除失败";
    }

    @FlowLimit
    @Override
    public String updateUser(UpdateUserVO vo) {
        String userId = getUserId();
        String machineCode = vo.getMachineCode();
        Integer score = vo.getStatus();
        Date expiredTime = vo.getExpiredTime();
        String sid = vo.getSid();
        List<String> code = vo.getCode();

        boolean isMany = code.size() > 1;

        if (code.isEmpty()) return "参数有误";

        // 查询该开发者下所有激活码
        List<String> codes = this.baseMapper.selectCodeFormDid(userId).stream().map(TCode::getCCode).toList();
        if (codes.isEmpty()) return "更新失败";

        // 修改指定软件下的指定激活码，如果是多个码则不读取修改过期时间
        boolean update = this.update()
                .eq("s_id", sid)
                .in("c_code", code)
                .in("c_code", codes)
                .set(!Objects.isNull(score), "c_score", score)
                .set("c_name", StrUtil.isEmptyIfStr(machineCode) ? null : machineCode)
                .set(!isMany && !Objects.isNull(expiredTime), "c_expired", expiredTime).update();


        return update ? null : "更新失败";
    }

    public Date getExpireTime(Integer timeType) {
        long currentTimeMillis = System.currentTimeMillis();
        return switch (timeType) {
            // 小时卡
            case 0 -> new Date(currentTimeMillis + Const.MILLIS_IN_HOUR);
            // 天卡
            case 1 -> new Date(currentTimeMillis + Const.MILLIS_IN_DAY);
            // 周卡
            case 2 -> new Date(currentTimeMillis + Const.MILLIS_IN_WEEK);
            // 月卡
            case 3 -> new Date(currentTimeMillis + Const.MILLIS_IN_MONTH);
            // 季卡
            case 4 -> new Date(currentTimeMillis + Const.MILLIS_IN_QUARTER);
            // 年卡
            case 5 -> new Date(currentTimeMillis + Const.MILLIS_IN_YEAR);
            default -> null;
        };
    }


    // 获取用户ID
    private String getUserId() {
        DecodedJWT authorization = jwtUtils.resolveJwt(request.getHeader("Authorization"));
        return jwtUtils.toId(authorization);
    }
}




