package cn.mianju.controller.user;

import cn.mianju.entity.RestBean;
import cn.mianju.entity.vo.request.user.DeleteUserVO;
import cn.mianju.entity.vo.request.user.UpdateOneUserVO;
import cn.mianju.entity.vo.request.user.UpdateUserVO;
import cn.mianju.entity.vo.request.user.UserSeachVO;
import cn.mianju.entity.vo.response.code.UserShowVO;
import cn.mianju.entity.vo.response.user.UserListVO;
import cn.mianju.service.code.TCodeService;
import cn.mianju.service.code.VCodeinfoService;
import cn.mianju.service.user.UserManagerService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.NoSuchAlgorithmException;
import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Resource
    VCodeinfoService vCodeinfoService;

    @Resource
    TCodeService tCodeService;

    @Resource
    UserManagerService userManagerService;

    @PostMapping("/get-user")
    @Operation(summary = "查询用户信息")
    public RestBean<List<UserShowVO>> getUser(@RequestBody @Validated UserSeachVO vo) {
        return vCodeinfoService.seachUser(vo);
    }


    @PostMapping("/del-user")
    @Operation(summary = "删除用户")
    public RestBean<Void> delUser(@RequestBody @Validated DeleteUserVO vo) {
        String message = tCodeService.delUser(vo);
        return message == null ? RestBean.success() : RestBean.failure(400, message);
    }

    @PostMapping("/update-user")
    @Operation(summary = "更新单个或多个用户")
    public RestBean<Void> updateUser(@RequestBody @Validated UpdateUserVO vo) {
        String message = tCodeService.updateUser(vo);
        return message == null ? RestBean.success() : RestBean.failure(400, message);
    }

    @PostMapping("/manager-user-get")
    @Operation(summary = "查询要管理的用户")
    public RestBean<List<UserListVO>> getManagerUser(@RequestBody @Validated UserSeachVO vo) {
        return userManagerService.getUserList(vo);
    }

    @PostMapping("/manager-user-update")
    @Operation(summary = "更新要管理的用户")
    public RestBean<Void> updateManagerUser(@RequestBody @Validated UpdateOneUserVO vo) throws NoSuchAlgorithmException {
        String message = userManagerService.updateManagerUserOne(vo);
        return message == null ? RestBean.success() : RestBean.failure(400, message);
    }

    @PostMapping("/manager-user-delete")
    @Operation(summary = "删除要管理的用户")
    public RestBean<Void> deleteManagerUser(@RequestBody @Validated DeleteUserVO vo) {
        String message = userManagerService.deleteManagerUserOne(vo);
        return message == null ? RestBean.success() : RestBean.failure(400, message);
    }

}
