package cn.mianju.entity.vo.request.user;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class DeleteUserVO {
    @NotNull(message = "软件id不能为空")
    String sid;

    List<String> code;
    List<String> username;
}
