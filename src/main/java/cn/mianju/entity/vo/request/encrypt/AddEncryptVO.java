package cn.mianju.entity.vo.request.encrypt;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AddEncryptVO {
    @NotNull
    String sid;

    @NotNull
    @Min(value = 0)
    Integer encryption;
}
