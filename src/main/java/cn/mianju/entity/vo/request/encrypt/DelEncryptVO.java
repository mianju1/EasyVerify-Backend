package cn.mianju.entity.vo.request.encrypt;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class DelEncryptVO {
    @NotNull
    String sid;

    @NotNull
    List<Integer> encryption;
}
