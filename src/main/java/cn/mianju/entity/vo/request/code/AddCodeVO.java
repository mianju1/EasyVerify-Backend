package cn.mianju.entity.vo.request.code;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

/**
 * @author MianJu 2024/12/19
 * EasyVerify cn.mianju.entity.vo.request.code
 */
@Data
public class AddCodeVO {
    // 软件id
    @NotNull
    String sid;

    // 类型 1: 注册码,2: 激活码
    @NotNull
    @Range(min = 1, max = 2)
    Integer codeType;

    // 时间类型：0：小时卡，1：天卡，2：周卡，3：月卡，4：季卡，5：年卡
    @NotNull
    @Range(min = 0, max = 5)
    Integer timeType;

    // 是否立即激活
    @NotNull
    Boolean isActive;

    // 生成数量
    @NotNull
    @Range(min = 1, max = 100)
    Integer num;

    // 积分
    @NotNull
    Integer scores;

    // 特殊类型
    @NotNull
    Integer codeSpecialType;

    // 额外参数
    String extraParams;
}
