package cn.mianju.service.developer;

import cn.mianju.entity.dto.TDeveloper;
import cn.mianju.entity.vo.request.developer.ConfirmResetVO;
import cn.mianju.entity.vo.request.developer.EmailRegisterVO;
import cn.mianju.entity.vo.request.developer.EmailResetVO;
import cn.mianju.entity.vo.response.developer.DeveloperChangeEmailVO;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * @author 10408
 * @description 针对表【t_developer】的数据库操作Service
 * @createDate 2024-12-10 14:01:26
 */
public interface TDeveloperAuthService extends IService<TDeveloper>, UserDetailsService {
    // 根据用户名或电子邮件查找开发者账户
    TDeveloper findAccountByNameOrEmail(String text);


    // 定义一个方法，用于注册时发送验证邮件验证码
    String registerEmailVerifyCode(String type, String email, String address);


    // 定义一个方法，用于注册电子邮件账户
    String registerEmailAccount(EmailRegisterVO info);


    // 重置电子邮件账户密码的方法
    String resetEmailAccountPassword(EmailResetVO info);


    // 重置密码的确认方法
    // 参数 info 是一个包含确认信息的对象
    String resetConfirm(ConfirmResetVO info);

    // 更改绑定邮箱
    String changeBindEmail(DeveloperChangeEmailVO vo);


}
