package cn.mianju.entity.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @author 10408
 * @TableName t_user
 */
@TableName(value = "t_user")
@Data
public class TUser {
    /**
     * 用户唯一标识
     */
    @TableId
    private String uId;

    /**
     * 用户名
     */
    private String uName;

    /**
     * 用户邮箱
     */
    private String uMail;

    /**
     * 用户密码（SHA256）
     */
    private String uPassword;

    /**
     * 用户最近登陆时间
     */
    private Date uLastloginTime;

    /**
     * 用户所属软件ID
     */
    private String sId;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}