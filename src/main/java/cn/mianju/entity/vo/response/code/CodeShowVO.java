package cn.mianju.entity.vo.response.code;

import lombok.Data;

/**
 * @author MianJu 2024/12/18
 * EasyVerify cn.mianju.entity.vo.response.code
 */
@Data
public class CodeShowVO {
    String id;
    String name;
    String code;
    String username;
    Long useTime;
    Long expired;
    Integer timeType;
}
