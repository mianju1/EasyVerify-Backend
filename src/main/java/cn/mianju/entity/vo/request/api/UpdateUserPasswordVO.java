package cn.mianju.entity.vo.request.api;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class UpdateUserPasswordVO {
    @NotNull
    @Length(min = 2, max = 16)
    String username;

    @NotNull
    @Length(min = 4, max = 20)
    String password;
}
