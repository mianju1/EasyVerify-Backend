package cn.mianju.entity.vo.request.user;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class BanUserVO {
    @NotNull(message = "软件id不能为空")
    private String sid;

    @NotNull(message = "用户名不能为空")
    List<String> username;

    @NotNull(message = "状态不能为空")
    Integer status;
}
