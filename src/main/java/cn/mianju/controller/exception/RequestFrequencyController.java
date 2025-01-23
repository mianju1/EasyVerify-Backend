package cn.mianju.controller.exception;

import cn.mianju.entity.RestBean;
import cn.mianju.exception.RequestFrequencyException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author MianJu 2024/12/13
 * EasyVerify cn.mianju.controller.exception
 */
@Slf4j
@RestControllerAdvice
public class RequestFrequencyController {

    /**
     * 校验不通过打印警告信息，而不是直接抛出异常
     * @param exception 验证异常
     * @return 校验结果
     */
    @ExceptionHandler(RequestFrequencyException.class)
    public RestBean<Void> requestFrequencyError(RequestFrequencyException exception) {
        log.warn("请求频率过快 [{}: {}]", exception.getClass().getName(), exception.getMessage());
        return RestBean.failure(-1, exception.getMessage());
    }

}
