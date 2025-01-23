package cn.mianju.advice;

import cn.mianju.exception.RequestFrequencyException;
import cn.mianju.utils.FlowUtils;
import cn.mianju.utils.IpUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * @author MianJu 2024/12/18
 * EasyVerify cn.mianju.advice
 * 流量控制切面类
 */
@Aspect
@Component
public class FlowAdvice {

    @Resource
    FlowUtils flowUtils;

    @Resource
    HttpServletRequest request;

    // 定义一个切入点，拦截所有请求
    @Pointcut("@annotation(cn.mianju.annotation.FlowLimit)")
    public void pointcut() {
    }

    @Before("pointcut()")
    public void doBefore() throws RequestFrequencyException {
        // 以IP为key，进行频率限制
        if (!flowUtils.requestLimit(IpUtil.getRealIP(request))) throw new RequestFrequencyException();

    }

}
