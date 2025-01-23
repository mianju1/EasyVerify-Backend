package cn.mianju.entity.vo.request.web;

import lombok.Data;
import org.hibernate.validator.constraints.Range;

/**
 * @author MianJu 2024/12/20
 * EasyVerify cn.mianju.entity.vo.request.web
 */
@Data
public class AddInterfaceVO {
    String sid;
    @Range(min = 0, max = 7)
    Integer function;
    @Range(min = 0, max = 1)
    Integer encryption;
}
