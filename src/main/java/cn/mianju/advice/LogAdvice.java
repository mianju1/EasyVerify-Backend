package cn.mianju.advice;

import cn.mianju.entity.RestBean;
import cn.mianju.entity.dto.TRequest;
import cn.mianju.service.request.TRequestService;
import cn.mianju.utils.IpUtil;
import cn.mianju.utils.SnowflakeIdGenerator;
import com.alibaba.fastjson2.JSON;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Arrays;
import java.util.Date;

@Aspect
@Component
public class LogAdvice {

    @Resource
    TRequestService requestService;

    @Resource
    SnowflakeIdGenerator snowflakeIdGenerator;

    private static final Logger logger = LoggerFactory.getLogger(LogAdvice.class);

    @Pointcut("execution(public * cn.mianju.controller.*.*.*(..))")
    public void controllerPointcut() {
    }

    @Around("controllerPointcut()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        // 获取请求对象
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        // 获取请求信息
        String ip = IpUtil.getRealIP(request);
        String method = request.getMethod();
        String uri = request.getRequestURI();
        String params = request.getQueryString();
        // 过滤不可序列化的参数
        Object[] args = joinPoint.getArgs();
        Object[] serializableArgs = Arrays.stream(args)
                .filter(arg -> !(arg instanceof HttpServletRequest)) // 过滤掉 HttpServletRequest
                .toArray();
        String body = JSON.toJSONString(serializableArgs);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication != null ? authentication.getName() : "anonymous";
        String authorities = authentication != null ? authentication.getAuthorities().toString() : "[]";

        // 记录请求开始时间
        long startTime = System.currentTimeMillis();
        Date requestTime = new Date(startTime);

        // 执行目标方法
        RestBean result = (RestBean) joinPoint.proceed();

        // 记录请求结束时间
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        // 记录日志
        TRequest tRequest = new TRequest();
        tRequest.setRId(result.id()+"");
        tRequest.setRMethod(method);
        tRequest.setRUri(uri);
        tRequest.setRParams(params);
        tRequest.setRBody(body);
        tRequest.setRUsername(username);
        tRequest.setRRole(authorities);
        tRequest.setRDate(requestTime);
        tRequest.setRTime(duration);
        tRequest.setRIpaddress(ip);
        tRequest.setRResponse(convertResultToJson(result));
        requestService.addRequestToQueue(tRequest);

        return result;
    }

    /**
     * 使用 Fastjson2 将方法返回结果转换为 JSON 字符串
     */
    private String convertResultToJson(Object result) {
        try {
            return JSON.toJSONString(result);
        } catch (Exception e) {
            logger.error("转换 JSON 出错", e);
            return "{}"; // 返回空 JSON 对象作为 fallback
        }
    }
}
