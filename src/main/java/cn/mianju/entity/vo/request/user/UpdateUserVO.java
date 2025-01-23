package cn.mianju.entity.vo.request.user;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import java.util.Date;
import java.util.List;

@Data
public class UpdateUserVO {

    @NotNull
    String sid;

    List<String> code;
    List<String> username;

    @Range(min = -1, max = 0)
    Integer status;

    Date expiredTime;
    String machineCode;


}
