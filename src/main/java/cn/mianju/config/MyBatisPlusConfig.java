package cn.mianju.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author MianJu 2024/12/11
 * EasyVerify cn.mianju.config
 */
@Configuration
public class MyBatisPlusConfig {
    /**
     * 分页插件
     */
    @Bean
    public MybatisPlusInterceptor paginationInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        //添加分页拦截器到MybatisPlusInterceptor中
        //在MP3.5.9 版本之后 该插件模块被分离出来 需要在pom中导包
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        return interceptor;
    }
    /**
     * 自定义sql打印格式
     * @return
     */
//    @Bean
//    public ConfigurationCustomizer mybatisConfigurationCustomizer() {
//        return configuration -> {
//            configuration.addInterceptor(new SqlInterceptor());
//        };
//    }

}
