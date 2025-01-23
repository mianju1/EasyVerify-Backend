package cn.mianju.entity.vo.request.code;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * @author MianJu 2024/12/19
 * EasyVerify cn.mianju.entity.vo.request.code
 */
@Data
public class ActiveCodeMachineVO {
    @NotNull
    List<String> code;

    String machine;
}
