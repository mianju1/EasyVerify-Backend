package cn.mianju.entity.vo.response.encrypt;

import lombok.Data;

import java.util.Date;

@Data
public class ShowEncrtyptVO {
    String sname;
    Integer encryption;
    Date updateTime;
    String publicKey;
    String privateKey;
}
