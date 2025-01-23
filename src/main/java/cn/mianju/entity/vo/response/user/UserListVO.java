package cn.mianju.entity.vo.response.user;

import lombok.Data;

import java.util.Date;

@Data
public class UserListVO {
   String sname;
   String username;
   Date expiredTime;
   Date loginTime;
}
