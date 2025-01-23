package cn.mianju.service.web;

import cn.mianju.entity.dto.TInterface;
import cn.mianju.entity.vo.request.web.AddInterfaceVO;
import cn.mianju.entity.vo.request.web.DelInterfaceVO;
import cn.mianju.entity.vo.request.web.UpdateInterfaceVO;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author 10408
 * @description 针对表【t_interface】的数据库操作Service
 * @createDate 2024-12-10 14:01:26
 */
public interface TInterfaceService extends IService<TInterface> {

    // 在指定软件下新增API
    String addInterface(AddInterfaceVO vo);

    // 修改指定软件指定API
    String updateInterface(UpdateInterfaceVO vo);

    // 删除指定接口
    String deleteInterface(DelInterfaceVO vo);

}
