package cn.mianju.service.code.impl;

import cn.mianju.annotation.FlowLimit;
import cn.mianju.entity.RestBean;
import cn.mianju.entity.view.VCodeinfo;
import cn.mianju.entity.vo.request.PageVO;
import cn.mianju.entity.vo.request.code.CodeSeachVO;
import cn.mianju.entity.vo.request.user.UserSeachVO;
import cn.mianju.entity.vo.response.code.CodeShowVO;
import cn.mianju.entity.vo.response.code.UserShowVO;
import cn.mianju.mapper.VCodeinfoMapper;
import cn.mianju.service.code.VCodeinfoService;
import cn.mianju.utils.JwtUtils;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ValidationException;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @author 10408
 * @description 针对表【v_codeinfo】的数据库操作Service实现
 * @createDate 2024-12-18 19:56:28
 */
@Service
public class VCodeinfoServiceImpl extends ServiceImpl<VCodeinfoMapper, VCodeinfo>
        implements VCodeinfoService {

    @Resource
    HttpServletRequest request;

    // jwt工具
    @Resource
    JwtUtils jwtUtils;

    @FlowLimit
    @Override
    public RestBean<List<CodeShowVO>> seachCode(CodeSeachVO vo) {
        // 获取请求用户id
        String id = getUserId();

        // 判断参数是否为空
        if (Objects.isNull(vo.getType())) throw new ValidationException("参数不能为空");

        Integer timeType = null;

        // 根据关键字判断激活码时间类型
        switch (vo.getKeyword()) {
            case "小时卡" -> timeType = 0;
            case "日卡" -> timeType = 1;
            case "周卡" -> timeType = 2;
            case "月卡" -> timeType = 3;
            case "季卡" -> timeType = 4;
            case "年卡" -> timeType = 5;
        }

        // 分页查询
        IPage<VCodeinfo> page = new Page<>(
                vo.getPage().getCurrentPage(),
                vo.getPage().getPageSize()
        );

        // 封装查询条件
        QueryWrapper<VCodeinfo> wrapper = new QueryWrapper<>();
        Integer finalTimeType = timeType;
        wrapper.select("s_id", "s_name", "c_code", "c_name", "c_usetime", "c_expired", "c_timetype")
                .eq("d_id", id)
                .eq("c_type", vo.getType())
                // 如果关键字不为空，则进行模糊查询，否则进行查询全部激活码信息
                .and(!vo.getKeyword().isEmpty(), i -> i.like("c_code", vo.getKeyword())
                        // 如果监测是时间类型的查询，则添加该字段进行查询
                        .or().eq(!Objects.isNull(finalTimeType), "c_timetype", finalTimeType)
                        .or().like("s_name", vo.getKeyword()));

        IPage<VCodeinfo> softwareIPage = this.page(page, wrapper);
        List<VCodeinfo> records = softwareIPage.getRecords();
        Long total = softwareIPage.getTotal();

        // 封装数据返回
        List<CodeShowVO> collect = records.stream()
                .map(code -> {
                    CodeShowVO codeShowVO = new CodeShowVO();
                    codeShowVO.setId(code.getSId());
                    codeShowVO.setName(code.getSName());
                    codeShowVO.setCode(code.getCCode());
                    codeShowVO.setUsername(code.getCName());
                    codeShowVO.setUseTime(dateToTimestamp(code.getCUsetime()));
                    codeShowVO.setExpired(dateToTimestamp(code.getCExpired()));
                    codeShowVO.setTimeType(code.getCTimetype());
                    return codeShowVO;
                }).toList();

        return RestBean.success(collect, total);
    }


    @FlowLimit
    @Override
    public RestBean<List<UserShowVO>> seachUser(UserSeachVO vo) {
        // 获取属性
        String id = getUserId();
        PageVO page = vo.getPage();
        String sid = vo.getSid();
        String keyword = vo.getKeyword();

        // 分页查询
        IPage<VCodeinfo> page_limit = new Page<>(
                page.getCurrentPage(),
                page.getPageSize()
        );

        // 封装查询条件
        QueryWrapper<VCodeinfo> wrapper = new QueryWrapper<>();
        wrapper.select("s_name", "c_code", "c_name", "c_expired", "c_score")
                .eq("d_id", id)
                .eq("s_id", sid)
                // 未激活说明不是用户，没有在使用
                .isNotNull("c_expired")
                // 如果不为空，则模糊查询关键字
                .and(!keyword.isEmpty(), i -> i.like("c_code", vo.getKeyword())
                        .or().like("c_name", vo.getKeyword()));

        IPage<VCodeinfo> userPage = this.page(page_limit, wrapper);
        List<VCodeinfo> records = userPage.getRecords();
        Long total = userPage.getTotal();


        List<UserShowVO> list = records.stream().map(user -> {
            UserShowVO userShowVO = new UserShowVO();
            userShowVO.setCode(user.getCCode());
            userShowVO.setSname(user.getSName());
            userShowVO.setScore(user.getCScore());
            userShowVO.setExpiredTime(user.getCExpired());
            userShowVO.setMachineCode(user.getCName());
            return userShowVO;
        }).toList();

        return RestBean.success(list, total);
    }

    // 获取用户ID
    private String getUserId() {
        DecodedJWT authorization = jwtUtils.resolveJwt(request.getHeader("Authorization"));
        return jwtUtils.toId(authorization);
    }


    // date类型转为时间戳
    private Long dateToTimestamp(Date date) {
        if (Objects.isNull(date)) return null;
        return date.getTime();
    }
}




