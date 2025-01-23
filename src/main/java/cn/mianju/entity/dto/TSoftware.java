package cn.mianju.entity.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 
 * @TableName t_software
 */
@TableName(value ="t_software")
@Data
public class TSoftware {
    /**
     * 软件唯一标识
     */
    @TableId
    private String sId;

    /**
     * 软件名称
     */
    private String sName;

    /**
     * 软件密钥
     */
    private String sKey;

    /**
     * 软件描述
     */
    private String sDesc;

    /**
     * 软件公告
     */
    private String sNotice;

    /**
     * 软件当前版本
     */
    private String sVersion;

    /**
     * 软件登录类型：0-账户密码 1-账户密码+注册码 2-激活码
     */
    private Integer sCodetype;


    /**
     * 软件是否验证机器码：0-不验证 1-验证
     */
    private Integer sVerifyMac;


    /**
     * 开发者唯一标识
     */
    private String dId;

    /**
     * 软件创建时间
     */
    private Date createTime;

    /**
     * 软件创建时间
     */
    private String createAddress;



    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}