package cn.mianju.entity.vo.request.api;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class LoginCodeVO {
    @NotNull
    @Length(min = 24, max = 24)
    String code;

    @NotNull
    String mac;
}
