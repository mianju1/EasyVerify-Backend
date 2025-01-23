package cn.mianju.entity.vo.response.code;

import lombok.Data;

import java.util.Date;

@Data
public class UserShowVO {
    String sname;
    String code;
    String machineCode;
    Date expiredTime;
    Integer score;
}
