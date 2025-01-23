package cn.mianju.service.software;

import cn.mianju.entity.RestBean;
import cn.mianju.entity.dto.TSoftware;
import cn.mianju.entity.vo.request.software.*;
import cn.mianju.entity.vo.response.software.SoftwareShowVO;
import cn.mianju.entity.vo.response.software.SoftwareTypeVO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author 10408
 * @description 针对表【t_software】的数据库操作Service
 * @createDate 2024-12-10 14:01:27
 */
public interface TSoftwareService extends IService<TSoftware> {
    // 修改软件信息
    String updateSoftware(SoftwareUpdateVO vo);

    String updateSoftwareByIds(UpdateByIdsVO vo);

    // 添加软件
    String addSoftware(SoftwareAddVO vo);

    // 删除软件
    String deleteSoftware(DeleteSoftwareVo vo);

    // 搜索软件
    RestBean<List<SoftwareShowVO>> searchSoft(SoftwareSeachVO vo);

    // 列出激活码或者注册码的软件
    RestBean<List<SoftwareTypeVO>> listSoft(Integer codetype);

    // 获取指定did下所有软件id
    List<String> getSoftIdByDid(String did);

}
