package cn.mianju.entity.view;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @TableName v_userinfo
 */
@TableName(value = "v_userinfo")
@Data
public class VUserinfo implements Serializable {
    /**
     * 用户唯一标识
     */
    @TableId(value = "u_id")
    private String uId;

    /**
     * 用户名
     */
    @TableField(value = "u_name")
    private String uName;

    /**
     * 用户邮箱
     */
    @TableField(value = "u_mail")
    private String uMail;

    /**
     * 用户状态：-1-启用 0-禁用
     */
    @TableField(value = "u_status")
    private Integer uStatus;

    /**
     * 用户密码（SHA256）
     */
    @TableField(value = "u_password")
    private String uPassword;

    /**
     * 软件唯一标识
     */
    @TableField(value = "s_id")
    private String sId;

    /**
     * 软件名称
     */
    @TableField(value = "s_name")
    private String sName;

    /**
     * 软件是否验证机器码：0-不验证 1-验证
     */
    @TableField(value = "s_verifymac")
    private Integer sVerifymac;

    /**
     * 开发者唯一标识
     */
    @TableField(value = "d_id")
    private String dId;

    /**
     * 激活码
     */
    @TableField(value = "c_code")
    private String cCode;

    /**
     * 注册码绑定用户，如果是激活码则是机器码
     */
    @TableField(value = "c_name")
    private String cName;

    /**
     * 激活码到期时间
     */
    @TableField(value = "c_expired")
    private Date cExpired;

    /**
     * 激活码积分 -1则无限制 0为停用
     */
    @TableField(value = "c_score")
    private Integer cScore;

    /**
     * 激活码类型：1-注册码 2-激活码
     */
    @TableField(value = "c_type")
    private Integer cType;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}