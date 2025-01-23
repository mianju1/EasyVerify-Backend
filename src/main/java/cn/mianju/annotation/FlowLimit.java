package cn.mianju.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author MianJu 2024/12/18
 * EasyVerify cn.mianju.annotation
 * 限流注解
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface FlowLimit {

}
