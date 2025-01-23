package cn.mianju.entity.vo.request.web;

import lombok.Data;

import java.util.List;

/**
 * @author MianJu 2024/12/23
 * EasyVerify cn.mianju.entity.vo.request.web
 */
@Data
public class DelInterfaceVO {
    String sid;
    List<String> url;
}
