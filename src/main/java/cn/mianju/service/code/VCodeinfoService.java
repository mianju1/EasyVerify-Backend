package cn.mianju.service.code;

import cn.mianju.entity.RestBean;
import cn.mianju.entity.view.VCodeinfo;
import cn.mianju.entity.vo.request.code.CodeSeachVO;
import cn.mianju.entity.vo.request.user.UserSeachVO;
import cn.mianju.entity.vo.response.code.CodeShowVO;
import cn.mianju.entity.vo.response.code.UserShowVO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author 10408
 * @description 针对表【v_codeinfo】的数据库操作Service
 * @createDate 2024-12-18 19:56:28
 */
public interface VCodeinfoService extends IService<VCodeinfo> {
    // 搜索激活、注册码信息
    RestBean<List<CodeShowVO>> seachCode(CodeSeachVO vo);

    // 搜索用户信息
    RestBean<List<UserShowVO>> seachUser(UserSeachVO vo);

}
