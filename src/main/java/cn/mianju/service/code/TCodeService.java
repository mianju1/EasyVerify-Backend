package cn.mianju.service.code;

import cn.mianju.entity.RestBean;
import cn.mianju.entity.dto.TCode;
import cn.mianju.entity.vo.request.code.ActiveCodeMachineVO;
import cn.mianju.entity.vo.request.code.AddCodeVO;
import cn.mianju.entity.vo.request.code.DeleteCodeVO;
import cn.mianju.entity.vo.request.code.UpdateCodeVO;
import cn.mianju.entity.vo.request.user.DeleteUserVO;
import cn.mianju.entity.vo.request.user.UpdateUserVO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author 10408
 * @description 针对表【t_code】的数据库操作Service
 * @createDate 2024-12-18 10:23:54
 */
public interface TCodeService extends IService<TCode> {

    // 删除指定激活、注册码
    String deleteCode(DeleteCodeVO vo);

    // 新增激活、注册码
    RestBean<List<String>> addCode(AddCodeVO vo);

    // 更新激活、注册码
    String updateCode(UpdateCodeVO vo);

    // 修改激活码的机器码
    String updateCodeMachine(ActiveCodeMachineVO vo);

    // 删除用户
    String delUser(DeleteUserVO vo);

    // 更新用户信息
    String updateUser(UpdateUserVO vo);


}
