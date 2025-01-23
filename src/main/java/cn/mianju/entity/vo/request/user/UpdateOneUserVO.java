package cn.mianju.entity.vo.request.user;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
@Data
public class UpdateOneUserVO {
    @NotNull
    String sid;
    @NotNull
    String username;

    @NotNull
    String password;
}
