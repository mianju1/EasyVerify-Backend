package cn.mianju.entity.view;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @TableName v_encryptinfo
 */
@TableName(value = "v_encryptinfo")
@Data
public class VEncryptinfo implements Serializable {
    /**
     * 加密算法唯一标识
     */
    @TableId(value = "e_id")
    private String eId;

    /**
     * 加密算法修改时间
     */
    @TableField(value = "e_updatetime")
    private Date eUpdatetime;

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
     * 加密算法类型 0-无加密 1-RSA加密
     */
    @TableField(value = "encryption")
    private Integer encryption;

    /**
     * 加密算法公钥
     */
    @TableField(value = "e_publickey")
    private String ePublickey;

    /**
     * 加密算法私钥
     */
    @TableField(value = "e_privatekey")
    private String ePrivatekey;

    /**
     * 开发者唯一标识
     */
    @TableField(value = "d_id")
    private String dId;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}