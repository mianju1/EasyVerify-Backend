package cn.mianju.entity.vo.request.developer;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * @author MianJu 2024/12/12
 * EasyVerify cn.mianju.entity.vo.request
 */
@Data
public class DeveloperRegisterVO {

    @Email
    String dEmail;

    @Length(max = 6, min = 6)
    String code;

    @Pattern(regexp = "^[a-zA-Z0-9\\u4e00-\\u9fa5]+$")
    @Length(min = 1, max = 10)
    String dName;

    @Length(min = 6, max = 20)
    String dPassword;
}
