package cn.mianju.entity.vo.request.developer;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * @author MianJu 2024/12/17
 * EasyVerify cn.mianju.entity.vo.request.developer
 */
@Data
public class PasswordModiyVO {
    @Length(min = 6, max = 20)
    String oldPassword;
    @Length(min = 6, max = 20)
    String newPassword;
}
