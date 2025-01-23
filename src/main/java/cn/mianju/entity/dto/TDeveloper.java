package cn.mianju.entity.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 
 * @TableName t_developer
 */
@TableName(value ="t_developer")
@Data
public class TDeveloper {
    /**
     * 开发者唯一标识
     */
    @TableId
    private String dId;

    /**
     * 开发者名称
     */
    private String dName;

    /**
     * 开发者绑定邮箱
     */
    private String dMail;

    /**
     * 开发者密码（SHA256）
     */
    private String dPassword;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}