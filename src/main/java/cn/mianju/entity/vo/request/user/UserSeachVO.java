package cn.mianju.entity.vo.request.user;

import cn.mianju.entity.vo.request.PageVO;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserSeachVO {
    @NotNull
    String sid;

    String keyword;
    PageVO page;
}
