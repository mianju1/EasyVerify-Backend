package cn.mianju.config;

import cn.mianju.entity.RestBean;
import cn.mianju.entity.dto.TDeveloper;
import cn.mianju.entity.vo.response.developer.DeveloperAuthVO;
import cn.mianju.exception.RequestFrequencyException;
import cn.mianju.filter.JwtAuthenticationFilter;
import cn.mianju.filter.RequestLogFilter;
import cn.mianju.service.developer.TDeveloperAuthService;
import cn.mianju.utils.Const;
import cn.mianju.utils.JwtUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutFilter;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author MianJu 2024/12/10
 * EasyVerify cn.mianju.config
 */
@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Resource
    RequestLogFilter requestLogFilter;

    @Resource
    JwtAuthenticationFilter jwtAuthenticationFilter;

    @Resource
    TDeveloperAuthService service;

    @Resource
    JwtUtils utils;


    /**
     * 针对于 SpringSecurity 6 的新版配置方法
     *
     * @param http 配置器
     * @return 自动构建的内置过滤器链
     * @throws Exception 可能的异常
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(conf -> conf
                        // 放行登录接口和 swagger 接口
                        .requestMatchers("/api/auth/**", "/error").permitAll()
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                        // 只对 developer 身份用户放行该接口
                        .requestMatchers("/api/dev/**").hasAnyRole(Const.ROLE_DEVELOPER)
                        .requestMatchers("/api/soft/**").hasAnyRole(Const.ROLE_DEVELOPER)
                        .requestMatchers("/api/code/**").hasAnyRole(Const.ROLE_DEVELOPER)
                        .requestMatchers("/api/web/**").hasAnyRole(Const.ROLE_DEVELOPER)
                        .requestMatchers("/api/user/**").hasAnyRole(Const.ROLE_DEVELOPER)
                        .requestMatchers("/api/encrypt/**").hasAnyRole(Const.ROLE_DEVELOPER)
                        // 其他请求都放行
                        .anyRequest().permitAll()
                )
                // 登录设置
                .formLogin(conf -> conf
                        // 设置登录接口地址
                        .loginProcessingUrl("/api/auth/login")
                        // 设置登录接口接收参数
                        .usernameParameter("username")
                        .passwordParameter("password")
                        // 设置失败处理器
                        .failureHandler(this::handleProcess)
                        // 设置成功处理器
                        .successHandler(this::handleProcess)
                )
                // 登出设置
                .logout(conf -> conf
                        // 设置登出接口
                        .logoutUrl("/api/auth/logout")
                        // 设置登出成功处理器
                        .logoutSuccessHandler(this::onLogoutSuccess)
                )
                // 异常处理
                .exceptionHandling(conf -> conf
                        // 访问拒绝处理器
                        .accessDeniedHandler(this::handleProcess)
                        // 身份认证处理器
                        .authenticationEntryPoint(this::handleProcess)
                )
                .csrf(AbstractHttpConfigurer::disable)
                // 将 session 设置为无状态
                .sessionManagement(conf -> conf
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // 在 验证账号密码过滤器之前 添加请求日志过滤器
//                .addFilterBefore(requestLogFilter, UsernamePasswordAuthenticationFilter.class)
                // 在 登出过滤器之前 添加请求日志过滤器 （退出登录过滤器在密码验证过滤器之前）
                .addFilterBefore(requestLogFilter, LogoutFilter.class)
                .addFilterBefore(jwtAuthenticationFilter, RequestLogFilter.class)
                .build();

    }


    /**
     * 将多种类型的Handler整合到同一个方法中，包含：
     * - 登录成功
     * - 登录失败
     * - 未登录拦截/无权限拦截
     *
     * @param request                   请求
     * @param response                  响应
     * @param exceptionOrAuthentication 异常或是验证实体
     * @throws IOException 可能的异常
     */
    private void handleProcess(HttpServletRequest request,
                               HttpServletResponse response,
                               Object exceptionOrAuthentication) throws IOException {
        response.setContentType("application/json;charset=utf-8");
        PrintWriter writer = response.getWriter();
        // 判断为 身份权限认证 异常
        if (exceptionOrAuthentication instanceof AccessDeniedException exception) {
            writer.write(RestBean
                    .forbidden(exception.getMessage()).asJsonString());
            // 其他异常
        } else if (exceptionOrAuthentication instanceof Exception exception) {
            if (exception instanceof InternalAuthenticationServiceException e && e.getCause() instanceof RequestFrequencyException) {
                writer.write(RestBean
                        .failure(-1, exception.getMessage()).asJsonString());

            } else {
                writer.write(RestBean
                        .unauthorized(exception.getMessage()).asJsonString());
            }
        } else if (exceptionOrAuthentication instanceof Authentication authentication) {
//             登录成功后拿到用户信息
            User user = (User) authentication.getPrincipal();
            TDeveloper developer = service.findAccountByNameOrEmail(user.getUsername());
            String jwt = utils.createJwt(user, developer.getDName(), developer.getDId());
            if (jwt == null) {
                writer.write(RestBean.forbidden("登录验证频繁，请稍后再试").asJsonString());
            } else {

                DeveloperAuthVO vo = new DeveloperAuthVO();
                vo.setDeveloperToken(jwt);
                vo.setDeveloperName(developer.getDName());
                vo.setDeveloperExpire(utils.expireTime());

                writer.write(RestBean.success(vo).asJsonString());
            }
        }
    }

    /**
     * 退出登录处理，将对应的Jwt令牌列入黑名单不再使用
     *
     * @param request        请求
     * @param response       响应
     * @param authentication 验证实体
     * @throws IOException 可能的异常
     */
    private void onLogoutSuccess(HttpServletRequest request,
                                 HttpServletResponse response,
                                 Authentication authentication) throws IOException {
        response.setContentType("application/json;charset=utf-8");
        PrintWriter writer = response.getWriter();
        String authorization = request.getHeader("Authorization");
        if (utils.invalidateJwt(authorization)) {
            writer.write(RestBean.success("退出登录成功").asJsonString());
            return;
        }
        writer.write(RestBean.failure(400, "退出登录失败").asJsonString());
    }
}
