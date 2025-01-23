package cn.mianju.entity.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 
 * @TableName t_request
 */
@TableName(value ="t_request")
@Data
public class TRequest {
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
}