package cn.mianju.entity.vo.response.developer;

import lombok.Data;

/**
 * @author MianJu 2024/12/14
 * EasyVerify cn.mianju.entity.vo.response
 */
@Data
public class DeveloperChangeEmailVO {
    String email;
    String newEmail;
    String code;
}
