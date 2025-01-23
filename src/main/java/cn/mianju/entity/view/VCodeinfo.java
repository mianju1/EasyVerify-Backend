package cn.mianju.entity.view;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @TableName v_codeinfo
 */
@TableName(value ="v_codeinfo")
@Data
public class VCodeinfo implements Serializable {
    /**
     * 激活码唯一标识
     */
    @TableId(value = "c_id")
    private String cId;

    /**
     * 激活码
     */
    @TableField(value = "c_code")
    private String cCode;

    /**
     * 激活码类型：1-注册码 2-激活码
     */
    @TableField(value = "c_type")
    private Integer cType;

    /**
     * 激活码时长类型：0-小时卡 1-日卡 2-周卡 3-月卡 4-季卡 5-年卡
     */
    @TableField(value = "c_timetype")
    private Integer cTimetype;

    /**
     * 注册码绑定用户，如果是激活码则是本身
     */
    @TableField(value = "c_name")
    private String cName;

    /**
     * 激活码使用时间
     */
    @TableField(value = "c_usetime")
    private Date cUsetime;

    /**
     * 激活码到期时间
     */
    @TableField(value = "c_expired")
    private Date cExpired;

    /**
     * 激活码积分
     */
    @TableField(value = "c_score")
    private Integer cScore;

    /**
     * 所属软件ID
     */
    @TableField(value = "s_id")
    private String sId;

    /**
     * 软件名称
     */
    @TableField(value = "s_name")
    private String sName;

    /**
     * 开发者唯一标识
     */
    @TableField(value = "d_id")
    private String dId;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}