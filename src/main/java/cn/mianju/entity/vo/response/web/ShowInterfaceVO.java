package cn.mianju.entity.vo.response.web;

import lombok.Data;

/**
 * @author MianJu 2024/12/20
 * EasyVerify cn.mianju.entity.vo.response.web
 */
@Data
public class ShowInterfaceVO {
    String name;
    Integer function;
    String url;
    String desc;
    Integer encryption;
}
