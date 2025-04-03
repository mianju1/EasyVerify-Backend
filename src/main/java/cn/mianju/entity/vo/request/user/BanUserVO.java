package cn.mianju.entity.vo.request.user;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class BanUserVO {
    @NotNull(message = "软件id不能为空")
    private String sid;

    List<String> username;
}
