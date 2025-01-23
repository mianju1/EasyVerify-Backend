package cn.mianju.service.web;

import cn.mianju.entity.RestBean;
import cn.mianju.entity.view.VInterfaceinfo;
import cn.mianju.entity.vo.request.web.GetInterfaceVO;
import cn.mianju.entity.vo.response.web.ShowInterfaceVO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author 10408
* @description 针对表【v_interfaceinfo】的数据库操作Service
* @createDate 2024-12-20 13:54:09
*/
public interface VInterfaceinfoService extends IService<VInterfaceinfo> {

    // 获取指定软件下所有接口
    RestBean<List<ShowInterfaceVO>> getInterfaceBySid(GetInterfaceVO vo);

    // 判断在请求的ID下是否存在软件，且查询是否存在相同function的接口
    List<VInterfaceinfo> getInterfaceByDidAndSid(String did, String sid);
}
