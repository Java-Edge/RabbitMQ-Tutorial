package com.javaedge.spring.config;

import com.javaedge.spring.util.MqConstant;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 发布订阅模式-【DirectExchange】-直连交换机
 * DirectExchange中Exchange的路由策略是将消息队列绑定到一个DirectExchange上，
 * 当一条消息到达 DirectExchange 时会被转发到与该条消息routing key相同的Queue上
 */
@Configuration
@SuppressWarnings("all")
public class DirectRabbitConfig {

    // 队列
    @Bean
    Queue MQ_QUEUE_3_Direct() {
        return new Queue(MqConstant.MQ_QUEUE_3_Direct);
    }

    // 交换机
    @Bean
    DirectExchange directExchange() {
        return new DirectExchange(MqConstant.Exchange_Name_Direct, true, false);
    }

    // 队列交换机绑定
    @Bean
    Binding binding() {
        return BindingBuilder.bind(MQ_QUEUE_3_Direct())
                .to(directExchange()).with(MqConstant.MQ_QUEUE_3_Direct);
    }
}