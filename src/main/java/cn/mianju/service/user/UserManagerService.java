package cn.mianju.service.user;

import cn.mianju.entity.RestBean;
import cn.mianju.entity.dto.TUser;
import cn.mianju.entity.vo.request.user.BanUserVO;
import cn.mianju.entity.vo.request.user.DeleteUserVO;
import cn.mianju.entity.vo.request.user.UpdateOneUserVO;
import cn.mianju.entity.vo.request.user.UserSeachVO;
import cn.mianju.entity.vo.response.user.UserListVO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.security.NoSuchAlgorithmException;
import java.util.List;

public interface UserManagerService extends IService<TUser> {
    // 查询用户
    RestBean<List<UserListVO>> getUserList(UserSeachVO vo);

    // 更新单用户信息
    String updateManagerUserOne(UpdateOneUserVO vo) throws NoSuchAlgorithmException;

    // 删除用户
    String deleteManagerUserOne(DeleteUserVO vo);

    // 禁用用户
    String banMangerUserOne(BanUserVO vo);
}
