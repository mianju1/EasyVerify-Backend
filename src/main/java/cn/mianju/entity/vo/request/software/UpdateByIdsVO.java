package cn.mianju.entity.vo.request.software;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.util.List;

/**
 * @author MianJu 2024/12/16
 * EasyVerify cn.mianju.entity.vo.request.software
 */
@Data
public class UpdateByIdsVO {
    List<String> id;
    @Length(min = 2, max = 10)
    String version;
    @Length(max = 200)
    String desc;
}
