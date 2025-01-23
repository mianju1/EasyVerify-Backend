package cn.mianju.entity.vo.request.software;

import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

/**
 * @author MianJu 2024/12/16
 * EasyVerify cn.mianju.entity.vo.request.software
 */
@Data
public class SoftwareAddVO {
    @Length(min = 2, max = 10)
    String name;
    @Length(min = 2, max = 10)
    String version;
    @Length(max = 200)
    String desc;
    @Range(min = 0, max = 2)
    Integer codetype;
}
