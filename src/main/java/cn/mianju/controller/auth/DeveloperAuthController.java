package cn.mianju.controller.auth;

import cn.mianju.entity.RestBean;
import cn.mianju.entity.vo.request.developer.EmailRegisterVO;
import cn.mianju.entity.vo.request.developer.EmailResetVO;
import cn.mianju.entity.vo.response.developer.DeveloperChangeEmailVO;
import cn.mianju.service.developer.TDeveloperAuthService;
import cn.mianju.utils.IpUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


/**
 * @author MianJu 2024/12/12
 * EasyVerify cn.mianju.controller
 */
@Validated
@RestController
@RequestMapping("/api/auth")
@Tag(name = "登录校验相关", description = "包括用户登录、注册、验证码请求等操作。")
public class DeveloperAuthController {

    @Resource
    TDeveloperAuthService developerService;


    /**
     * 更改绑定邮箱的接口
     *
     * @param vo 包含原邮箱、新邮箱、验证码等信息的对象
     * @return 如果操作成功，则返回成功的RestBean对象；如果操作失败，则返回包含错误信息的RestBean对象
     */
    @PostMapping("/change-email")
    @Operation(summary = "更改绑定邮箱")
    public RestBean<Void> changeBindEmail(@RequestBody @Valid DeveloperChangeEmailVO vo) {
        String message = developerService.changeBindEmail(vo);
        return message == null ? RestBean.success() : RestBean.failure(400, message);
    }

    /**
     * 请求邮件验证码
     *
     * @param email   请求邮件
     * @param type    类型
     * @param request 请求
     * @return 是否请求成功
     */
    @GetMapping("/email-code")
    @Operation(summary = "请求邮件验证码")
    public RestBean<Void> askVerifyCode(@RequestParam @Email String email,
                                        @RequestParam @Pattern(regexp = "(register|reset|change)") String type,
                                        HttpServletRequest request) {

        String message = developerService.registerEmailVerifyCode(type, email, IpUtil.getRealIP(request));
        return message == null ? RestBean.success() : RestBean.failure(400, message);
    }


    /**
     * 进行用户注册操作，需要先请求邮件验证码
     *
     * @param vo 注册信息
     * @return 是否注册成功
     */
    @PostMapping("/register")
    @Operation(summary = "注册开发者账号")
    public RestBean<Void> register(@RequestBody @Valid EmailRegisterVO vo) {
        String message = developerService.registerEmailAccount(vo);
        return message == null ? RestBean.success() : RestBean.failure(400, message);
    }

    /**
     * 执行密码重置操作
     *
     * @param vo 密码重置信息
     * @return 是否操作成功
     */
    @PostMapping("/reset-password")
    @Operation(summary = "密码重置操作")
    public RestBean<Void> resetPassword(@RequestBody @Valid EmailResetVO vo) {
        String message = developerService.resetEmailAccountPassword(vo);
        return message == null ? RestBean.success() : RestBean.failure(400, message);
    }

}
