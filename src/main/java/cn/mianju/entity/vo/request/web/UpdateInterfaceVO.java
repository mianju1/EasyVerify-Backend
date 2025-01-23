package cn.mianju.entity.vo.request.web;

import lombok.Data;
import org.hibernate.validator.constraints.Range;

import java.util.List;

/**
 * @author MianJu 2024/12/21
 * EasyVerify cn.mianju.entity.vo.request.web
 */
@Data
public class UpdateInterfaceVO {
    String sid;

    List<String> url;

    @Range(min = 0,max = 1)
    Integer encryption;
}
