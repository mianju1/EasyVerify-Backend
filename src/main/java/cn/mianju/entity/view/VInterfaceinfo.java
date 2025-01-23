package cn.mianju.entity.view;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 
 * @TableName v_interfaceinfo
 */
@TableName(value ="v_interfaceinfo")
@Data
public class VInterfaceinfo implements Serializable {
    /**
     * 接口唯一标识
     */
    @TableId(value = "i_id")
    private String iId;

    /**
     * 接口功能
     */
    @TableField(value = "i_function")
    private Integer iFunction;

    /**
     * 接口链接
     */
    @TableField(value = "i_url")
    private String iUrl;

    /**
     * 接口描述
     */
    @TableField(value = "i_desc")
    private String iDesc;

    /**
     * 接口加密方式
     */
    @TableField(value = "encryption")
    private Integer encryption;

    /**
     * 软件名称
     */
    @TableField(value = "s_name")
    private String sName;

    /**
     * 软件是否验证机器码：0-不验证 1-验证
     */
    @TableField(value = "s_verifymac")
    private Integer sVerifyMac;

    /**
     * 软件登录类型：0-账户密码 1-账户密码+注册码 2-激活码
     */
    @TableField(value = "s_codetype")
    private Integer sCodetype;

    /**
     * 开发者唯一标识
     */
    @TableField(value = "d_id")
    private String dId;

    /**
     * 软件唯一标识
     */
    @TableField(value = "s_id")
    private String sId;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}