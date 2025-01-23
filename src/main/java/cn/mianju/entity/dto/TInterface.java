package cn.mianju.entity.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 
 * @TableName t_interface
 */
@TableName(value ="t_interface")
@Data
public class TInterface {
    /**
     * 接口唯一标识
     */
    @TableId
    private String iId;

    /**
     * 接口功能
     */
    private Integer iFunction;

    /**
     * 接口链接
     */
    private String iUrl;

    /**
     * 接口参数
     */
    private String iParam;

    /**
     * 接口描述
     */
    private String iDesc;

    /**
     * 软件唯一标识
     */
    private String sId;

    /**
     * 接口加密方式
     */
    private Integer encryption;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}