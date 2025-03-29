package cn.mianju.entity.vo.request.web;

import cn.mianju.entity.vo.request.PageVO;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * @author MianJu 2024/12/20
 * EasyVerify cn.mianju.entity.vo.request.web
 */
@Data
public class GetInterfaceVO {
    @Length(min = 18, max = 20)
    String sid;
    PageVO page;
}
