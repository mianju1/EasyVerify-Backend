package cn.mianju.entity.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @TableName t_code
 */
@TableName(value ="t_code")
@Data
public class TCode implements Serializable {
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
     * 激活码创建时间
     */
    @TableField(value = "c_createtime")
    private Date cCreatetime;

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


    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}