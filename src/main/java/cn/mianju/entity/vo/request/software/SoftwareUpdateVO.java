package cn.mianju.entity.vo.request.software;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * @author MianJu 2024/12/16
 * EasyVerify cn.mianju.entity.vo.request.software
 */
@Data
public class SoftwareUpdateVO {
    String id;
    @Length(min = 2, max = 10)
    String version;
    @Length(max = 200)
    String desc;
    @Length(max = 400)
    String notice;
}
