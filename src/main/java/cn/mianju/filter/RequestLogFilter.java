package cn.mianju.filter;

import cn.mianju.utils.Const;
import cn.mianju.utils.IpUtil;
import cn.mianju.utils.SnowflakeIdGenerator;
import com.alibaba.fastjson2.JSONObject;
import jakarta.annotation.Resource;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;

/**
 * @author MianJu 2024/12/11
 * EasyVerify cn.mianju.filter
 */
@Slf4j
@Component
// 继承 OncePerRequestFilter，每一次 request 都会执行一次过滤器
public class RequestLogFilter extends OncePerRequestFilter {

    @Resource
    SnowflakeIdGenerator generator;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        long startTime = System.currentTimeMillis();
        this.logRequestStart(request);
        ContentCachingResponseWrapper wrapper = new ContentCachingResponseWrapper(response);
        filterChain.doFilter(request, wrapper);
        this.logRequestEnd(wrapper, startTime);
        wrapper.copyBodyToResponse();

    }

    /**
     * 请求开始时的日志打印，包含请求全部信息，以及对应用户角色
     *
     * @param request 请求
     */
    public void logRequestStart(HttpServletRequest request) throws IOException {
        long reqId = generator.nextId();
        MDC.put("reqId", String.valueOf(reqId));
        JSONObject params = new JSONObject();
        request.getParameterMap().forEach((k, v) -> params.put(k, v.length > 0 ? v[0] : null));
        Object id = request.getAttribute(Const.ATTR_USER_ID);
        String ip = IpUtil.getRealIP(request);


        if (id != null) {
            User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            log.info("请求URL: \"{}\" ({}) | 远程IP地址: {} │ 身份: {} (UID: {}) | 角色: {} | 请求参数列表: {}",
                    request.getServletPath(), request.getMethod(), ip,
                    user.getUsername(), id, user.getAuthorities(), params);
        } else {
            log.info("请求URL: \"{}\" ({}) | 远程IP地址: {} │ 身份: 未验证 | 请求参数列表: {}",
                    request.getServletPath(), request.getMethod(), ip, params);
        }
    }

    private static String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        log.info("x-forwarded-for:" + ip);
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
            log.info("Proxy-Client-IP:" + ip);
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
            log.info("WL-Proxy-Client-IP:" + ip);
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
            log.info("unknown:" + ip);
        }

        return ip;
    }


    /**
     * 请求结束时的日志打印，包含处理耗时以及响应结果
     *
     * @param wrapper   用于读取响应结果的包装类
     * @param startTime 起始时间
     */
    public void logRequestEnd(ContentCachingResponseWrapper wrapper, long startTime) {
        long time = System.currentTimeMillis() - startTime;
        int status = wrapper.getStatus();
        String content = status != 200 ?
                status + " 错误" : new String(wrapper.getContentAsByteArray());
        log.info("请求处理耗时: {}ms | 响应结果: {}", time, content);
    }
}
