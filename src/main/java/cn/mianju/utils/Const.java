package cn.mianju.utils;

import java.util.Map;

/**
 * 一些常量字符串整合
 *
 * @author 10408
 */
public final class Const {
    //JWT令牌
    public final static String JWT_BLACK_LIST = "jwt:blacklist:";
    public final static String JWT_FREQUENCY = "jwt:frequency:";
    //请求频率限制
    public final static String FLOW_LIMIT_COUNTER = "flow:counter:";
    public final static String FLOW_LIMIT_BLOCK = "flow:block:";
    //邮件验证码
    public final static String VERIFY_EMAIL_LIMIT = "verify:email:limit:";
    public final static String VERIFY_EMAIL_DATA = "verify:email:data:";
    public final static int VERIFY_EMAIL_LIMIT_TIME = 5;
    public final static int ORDER_CORS = -102;
    //请求自定义属性
    public final static String ATTR_USER_ID = "userId";
    //消息队列
    public final static String MQ_MAIL = "mail";
    public final static String MQ_LOG = "log";
    //角色
    public final static String ROLE_USER = "USER";
    public final static String ROLE_DEVELOPER = "DEVELOPER";

    // 时长常量，用于计算不同时间单位（毫秒）
    public static final long MILLIS_IN_HOUR = 60 * 60 * 1000;
    public static final long MILLIS_IN_DAY = 24 * MILLIS_IN_HOUR;
    public static final long MILLIS_IN_WEEK = 7 * MILLIS_IN_DAY;
    public static final long MILLIS_IN_MONTH = 30 * MILLIS_IN_DAY; // 假设一个月为30天，根据实际情况调整
    public static final long MILLIS_IN_QUARTER = 90 * MILLIS_IN_DAY; // 假设一个季度为90天，根据实际情况调整
    public static final long MILLIS_IN_YEAR = 365 * MILLIS_IN_DAY; // 假设一年为365天，不考虑闰年

    // 激活码类型
    public static final int CODE_TYPE_ACTIVATION = 2; // 激活码类型
    public static final int CODE_TYPE_REGISTRATION = 1; // 注册码类型


    // 接口描述
    public static final Map<Integer, String> CODE_TYPE_DESC = Map.of(
            0, """
                    {
                        "username": "用户名"(必填),
                        "password": "密码"(必填),
                        "code": "邀请码"
                    }
                    """,
            1, """
                    {
                        "username": "用户名"(必填),
                        "password": "密码"(必填)
                    }
                    """,
            2, """
                    {
                        "code": "激活码"(必填),
                        "mac": "机器码"(必填)
                    }
                    """,
            3, "无参数",
            4, "无参数",
            5, """
                    {
                        "username": "用户名"(选填)
                        "code": "激活码"(选填)
                    }
                    """,
            6, """
                    {
                        "version": "版本号"(必填)
                    }
                    """,
            7, """
                    {
                        "username": "用户名"(必填),
                        "password": "新密码"(必填),
                        "idCode": "身份码"(必填)
                    }
                    """
    );

}
