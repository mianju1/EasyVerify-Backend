package cn.mianju.entity.vo.response.developer;

import lombok.Data;

import java.util.Date;

/**
 * @author MianJu 2024/12/11
 * EasyVerify cn.mianju.entity.vo
 * 登录验证成功的开发者信息响应
 */
@Data
public class DeveloperAuthVO {
    String developerName;
    String developerToken;
    Date developerExpire;
}
