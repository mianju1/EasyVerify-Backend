package cn.mianju.config;

import cn.mianju.utils.Const;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * RabbitMQ消息队列配置
 */
@Configuration
public class RabbitConfiguration {
    @Bean("mailQueue")
    public Queue mailQueue() {
        return QueueBuilder
                .durable(Const.MQ_MAIL)
                .build();
    }

    @Bean("logQueue")
    public Queue logQueue() {
        return QueueBuilder
                .durable(Const.MQ_LOG)
                .build();
    }

    // 报错加入此配置
    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
