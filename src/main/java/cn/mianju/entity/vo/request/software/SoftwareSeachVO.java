package cn.mianju.entity.vo.request.software;

import cn.mianju.entity.vo.request.PageVO;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * @author MianJu 2024/12/17
 * EasyVerify cn.mianju.entity.vo.request.software
 */
@Data
public class SoftwareSeachVO {
    @Length(max = 25)
    String keyword;
    PageVO page;
}
