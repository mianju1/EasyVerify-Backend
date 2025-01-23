package cn.mianju.service.developer;

import cn.mianju.entity.RestBean;
import cn.mianju.entity.dto.TDeveloper;
import cn.mianju.entity.vo.response.developer.DeveloperInfoVO;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author MianJu 2024/12/14
 * EasyVerify cn.mianju.service.developer
 */

public interface TDeveloperInfoService extends IService<TDeveloper> {
    // 获取开发者个人中心信息
    RestBean<DeveloperInfoVO> getDeveloperInfo(String d_name);


}
