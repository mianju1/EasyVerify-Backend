package cn.mianju.utils;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * @author MianJu 2024/12/11
 * EasyVerify cn.mianju.utils
 * 限流通用工具
 * 针对于不同的情况进行限流操作，支持限流升级
 */
@Slf4j
@Component
public class FlowUtils {
    @Resource
    StringRedisTemplate template;

    //验证邮件发送冷却时间限制，秒为单位
    @Value("${spring.web.verify.mail-limit}")
    int verifyLimit;

    // 接口请求限制，时间
    @Value("${spring.web.flow.limit.time}")
    int flowLimitTime;

    // 接口请求限制，次数
    @Value("${spring.web.flow.limit.count}")
    int flowLimitCount;

    // 接口请求限制，周期
    @Value("${spring.web.flow.limit.period}")
    int flowLimitPeriod;


    private static final String LIMIT_VALUE = "1";

    /**
     * 针对于单次频率限制，请求成功后，在冷却时间内不得再次进行请求，如3秒内不能再次发起请求
     * @param key 键
     * @param blockTime 限制时间
     * @return 是否通过限流检查
     */
    public boolean limitOnceCheck(String key, int blockTime){
        return this.internalCheck(key, 1, blockTime, (overclock) -> false);
    }

    /**
     * 针对于单次频率限制，请求成功后，在冷却时间内不得再次进行请求
     * 如3秒内不能再次发起请求，如果不听劝阻继续发起请求，将限制更长时间
     * @param key 键
     * @param frequency 请求频率
     * @param baseTime 基础限制时间
     * @param upgradeTime 升级限制时间
     * @return 是否通过限流检查
     */
    public boolean limitOnceUpgradeCheck(String key, int frequency, int baseTime, int upgradeTime){
        return this.internalCheck(key, frequency, baseTime, (overclock) -> {
            if (overclock) {
                template.opsForValue().set(key, LIMIT_VALUE, upgradeTime, TimeUnit.SECONDS);
            }
            return false;
        });
    }

    /**
     * 针对某个字段对接口请求次数限流
     *
     * @param column 字段
     * @return 是否通过验证
     */
    public boolean requestLimit(String column) {
        String countKey = Const.FLOW_LIMIT_COUNTER + column;
        String blockKey = Const.FLOW_LIMIT_BLOCK + column;

        return this.limitPeriodCheck(countKey, blockKey, flowLimitTime, flowLimitCount, flowLimitPeriod);
    }

    /**
     * 针对某个字段对接口请求次数限流(自定义时间、次数和周期)
     *
     * @param column 字段
     * @return 是否通过验证
     */
    public boolean requestLimit(String column, int flowTime, int flowCount, int flowPeriod) {
        String countKey = Const.FLOW_LIMIT_COUNTER + column;
        String blockKey = Const.FLOW_LIMIT_BLOCK + column;

        return this.limitPeriodCheck(countKey, blockKey, flowTime, flowCount, flowPeriod);
    }

    /**
     * 针对于在时间段内多次请求限制，如3秒内限制请求20次，超出频率则封禁一段时间
     * @param counterKey 计数键
     * @param blockKey 封禁键
     * @param blockTime 封禁时间
     * @param frequency 请求频率
     * @param period 计数周期
     * @return 是否通过限流检查
     */
    public boolean limitPeriodCheck(String counterKey, String blockKey, int blockTime, int frequency, int period){
        return this.internalCheck(counterKey, frequency, period, (overclock) -> {
            if (overclock) {
                template.opsForValue().set(blockKey, "", blockTime, TimeUnit.SECONDS);
            }
            return !overclock;
        });
    }

    /**
     * 内部使用请求限制主要逻辑
     * @param key 计数键
     * @param frequency 请求频率
     * @param period 计数周期
     * @param action 限制行为与策略
     * @return 是否通过限流检查
     */
    private boolean internalCheck(String key, int frequency, int period, LimitAction action){
        // 从redis中获取当前值计数
        String count = template.opsForValue().get(key);
        if (count != null) {
            // 如果当前值存在，则计次加一
            long value = Optional.ofNullable(template.opsForValue().increment(key)).orElse(0L);
            int c = Integer.parseInt(count);
            if(value != c + 1) {
                template.expire(key, period, TimeUnit.SECONDS);
            }
            return action.run(value > frequency);
        } else {
            template.opsForValue().set(key, LIMIT_VALUE, period, TimeUnit.SECONDS);
            return true;
        }
    }

    /**
     * 内部使用，限制行为与策略
     */
    private interface LimitAction {
        boolean run(boolean overclock);
    }
}
