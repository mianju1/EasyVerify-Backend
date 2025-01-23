package cn.mianju.service.request.impl;

import cn.mianju.annotation.FlowLimit;
import cn.mianju.entity.RestBean;
import cn.mianju.entity.view.VRequestlog;
import cn.mianju.mapper.VRequestlogMapper;
import cn.mianju.service.request.VRequestlogService;
import cn.mianju.service.software.TSoftwareService;
import cn.mianju.utils.JwtUtils;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Admin
 * @description 针对表【v_requestlog】的数据库操作Service实现
 * @createDate 2025-01-16 19:27:02
 */
@Service
public class VRequestlogServiceImpl extends ServiceImpl<VRequestlogMapper, VRequestlog>
        implements VRequestlogService {

    @Resource
    JwtUtils jwtUtils;

    @Resource
    HttpServletRequest request;

    @Resource
    TSoftwareService tSoftwareService;

    @FlowLimit
    @Override
    public RestBean<HashMap<String, Map<String, Integer>>> last7DayLoginCount() {
        String userId = getUserId();
        List<String> sids = tSoftwareService.getSoftIdByDid(userId);
        if (sids.isEmpty()) return RestBean.failure(404, "未查询到数据");

//        List<VRequestlog> list = this.baseMapper.selectLast7DaysLogin(userId, sids);
        List<VRequestlog> list = this.query()
                .select("s_id", "s_name", "d_id", "DATE(r_date) as date", "COUNT(DISTINCT(r_body)) as num")
                .ge("r_date", LocalDate.now().minusDays(7)) // r_date >= 当前日期 - 7 天
                .in("s_id", sids) // s_id IN (sIds)
                .eq("d_id", userId)  // d_id = dId
                .in("i_function", 1, 2) // i_function IN (1, 2)
                .apply("INSTR(r_response, '200') > 0") // INSTR(r_response, '200')
                .groupBy("s_id", "DATE(r_date)") // GROUP BY s_id, DATE(r_date)
                .list();

        if (list.isEmpty()) return RestBean.failure(404, "未查询到数据");

        HashMap<String, Map<String, Integer>> map = new HashMap<>();
        for (VRequestlog v : list) {
            String sName = v.getSName();
            Integer num = v.getNum();
            Date date = v.getDate();

            if (map.containsKey(sName)) {
                map.get(sName).put(date.toString(), num);
            } else {
                HashMap<String, Integer> newMap = new HashMap<>();
                newMap.put(date.toString(), num);
                map.put(sName, newMap);
            }
        }

        boolean empty = map.isEmpty();

        return !empty ? RestBean.success(map) : RestBean.failure(404, "未查询到数据");
    }

    // 获取用户ID
    private String getUserId() {
        DecodedJWT authorization = jwtUtils.resolveJwt(request.getHeader("Authorization"));
        return jwtUtils.toId(authorization);
    }
}




