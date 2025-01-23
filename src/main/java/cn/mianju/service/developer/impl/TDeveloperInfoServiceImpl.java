package cn.mianju.service.developer.impl;


import cn.mianju.annotation.FlowLimit;
import cn.mianju.entity.RestBean;
import cn.mianju.entity.dto.TDeveloper;
import cn.mianju.entity.vo.response.developer.DeveloperInfoVO;
import cn.mianju.mapper.TDeveloperMapper;
import cn.mianju.service.developer.TDeveloperInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * @author MianJu 2024/12/14
 * EasyVerify cn.mianju.service.developer.impl
 */
@Service
public class TDeveloperInfoServiceImpl extends ServiceImpl<TDeveloperMapper, TDeveloper>
        implements TDeveloperInfoService {

    @FlowLimit
    @Override
    public RestBean<DeveloperInfoVO> getDeveloperInfo(String d_name) {
        // 实现获取开发者个人中心信息的方法
        if (Objects.isNull(d_name)) return RestBean.failure(400, "开发者名称不能为空");

        // 查询开发者信息
        TDeveloper developer = this.query()
                .eq("d_name", d_name)
                .one();

        if (Objects.isNull(developer)) return RestBean.failure(404, "未找到该用户");

        // 封装返回数据
        DeveloperInfoVO vo = new DeveloperInfoVO();
        vo.setD_id(developer.getDId());
        vo.setD_mail(developer.getDMail());

        return RestBean.success(vo);

    }
}
