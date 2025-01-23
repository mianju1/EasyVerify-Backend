package cn.mianju.entity.vo.request.code;

import cn.mianju.entity.vo.request.PageVO;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

/**
 * @author MianJu 2024/12/18
 * EasyVerify cn.mianju.entity.vo.request.code
 */
@Data
public class CodeSeachVO {
    String keyword;
    @Range(min = 1, max = 2)
    Integer type; // 1：注册码 2：激活码
    PageVO page;
}
