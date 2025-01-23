package cn.mianju.entity.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @TableName t_encrypt
 */
@TableName(value ="t_encrypt")
@Data
public class TEncrypt implements Serializable {
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
     * 加密算法所属软件
     */
    @TableField(value = "s_id")
    private String sId;

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

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}