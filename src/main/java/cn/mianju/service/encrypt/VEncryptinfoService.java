package cn.mianju.service.encrypt;

import cn.mianju.entity.RestBean;
import cn.mianju.entity.view.VEncryptinfo;
import cn.mianju.entity.vo.response.encrypt.ShowEncrtyptVO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author Admin
* @description 针对表【v_encryptinfo】的数据库操作Service
* @createDate 2025-01-06 22:11:35
*/
public interface VEncryptinfoService extends IService<VEncryptinfo> {

    // 查询当前用户指定软件的加密方法
    RestBean<List<ShowEncrtyptVO>> getEncryptByUserAndSoft(String sid);

    // 查询当前用户所有软件的加密方法
    RestBean<List<Integer>> getEncryptByUser(String sid);

}
