package cn.mianju.entity.vo.request.developer;

import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * @author MianJu 2024/12/13
 * EasyVerify cn.mianju.entity.vo.request.developer
 */
@Data
public class DeveloperLoginVO {
    @Pattern(regexp = "^[a-zA-Z0-9\\u4e00-\\u9fa5]+$")
    @Length(min = 1, max = 10)
    String username;
    @Length(min = 6, max = 20)
    String password;
}
