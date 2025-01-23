package cn.mianju.entity.view;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 
 * @TableName v_requestlog
 */
@TableName(value ="v_requestlog")
@Data
public class VRequestlog {
    /**
     * 请求的唯一主键
     */
    @TableId(value = "r_id")
    private String rId;

    /**
     * 请求方法
     */
    @TableField(value = "r_method")
    private String rMethod;

    /**
     * 请求链接
     */
    @TableField(value = "r_uri")
    private String rUri;

    /**
     * 请求参数，通常get携带
     */
    @TableField(value = "r_params")
    private String rParams;

    /**
     * 请求体，通常post携带
     */
    @TableField(value = "r_body")
    private String rBody;

    /**
     * 请求用户
     */
    @TableField(value = "r_username")
    private String rUsername;

    /**
     * 请求身份
     */
    @TableField(value = "r_role")
    private String rRole;

    /**
     * 请求日期
     */
    @TableField(value = "r_date")
    private Date rDate;

    /**
     * 请求响应时间
     */
    @TableField(value = "r_time")
    private Long rTime;

    /**
     * 请求IP地址
     */
    @TableField(value = "r_ipaddress")
    private String rIpaddress;

    /**
     * 请求响应内容
     */
    @TableField(value = "r_response")
    private String rResponse;

    /**
     * 软件唯一标识
     */
    @TableField(value = "s_id")
    private String sId;

    /**
     * 软件唯一标识
     */
    @TableField(value = "s_name")
    private String sName;

    /**
     * 接口功能 0-注册 1-账号登录 2-激活码登录 3-获取版本 4-获取公告 5-获取指定用户到期时间 6-是否为当前最新版本 7-修改用户密码
     */
    @TableField(value = "i_function")
    private Integer iFunction;

    /**
     * 接口加密方式 0-无加密 1-RSA2048
     */
    @TableField(value = "encryption")
    private Integer encryption;

    /**
     * 开发者唯一标识
     */
    @TableField(value = "d_id")
    private String dId;

    // group by
    @TableField(exist = false)
    private Integer num;
    @TableField(exist = false)
    private Date date;
}