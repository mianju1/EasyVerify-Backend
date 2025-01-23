package cn.mianju.entity.vo.request.code;

import jakarta.validation.constraints.Min;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * @author MianJu 2024/12/19
 * EasyVerify cn.mianju.entity.vo.request.code
 */
@Data
public class UpdateCodeVO {
    @Length(min = 24, max = 24)
    String code;

    Boolean isActive;

    @Min(value = -1)
    Integer score;

    String username;
}
