package com.javaedge.springboot.producer;

import java.util.Map;

import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate.ConfirmCallback;
import org.springframework.amqp.rabbit.core.RabbitTemplate.ReturnCallback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import com.javaedge.springboot.entity.Order;

/**
 * 发送消息
 *
 * @author JavaEdge
 */
@Component
public class RabbitSender {

    /**
     * 自动注入RabbitTemplate模板类
     */
    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 回调函数: confirm确认
     */
    final ConfirmCallback confirmCallback = (correlationData, ack, cause) -> {
        System.err.println("correlationData: " + correlationData);
        System.err.println("ack: " + ack);
        if (!ack) {
            System.err.println("异常处理....");
        }
    };

    /**
     * 回调函数: return返回
     */
    final ReturnCallback returnCallback = (message, replyCode, replyText, exchange, routingKey) -> {
        System.err.println("return exchange: " + exchange + ", routingKey: "
                + routingKey + ", replyCode: " + replyCode + ", replyText: " + replyText);
    };

    /**
     * 发送消息方法调用: 构建Message消息
     *
     * @param message
     * @param properties
     * @throws Exception
     */
    public void send(Object message, Map<String, Object> properties) throws Exception {
        MessageHeaders mhs = new MessageHeaders(properties);
        Message msg = MessageBuilder.createMessage(message, mhs);
        rabbitTemplate.setConfirmCallback(confirmCallback);
        rabbitTemplate.setReturnCallback(returnCallback);
        //id + 时间戳 全局唯一
        CorrelationData correlationData = new CorrelationData("1234567890");
        rabbitTemplate.convertAndSend("exchange-1", "springboot.javaedge", msg, correlationData);
    }

    /**
     * 发送消息方法调用: 构建自定义对象消息
     *
     * @param order
     * @throws Exception
     */
    public void sendOrder(Order order) throws Exception {
        rabbitTemplate.setConfirmCallback(confirmCallback);
        rabbitTemplate.setReturnCallback(returnCallback);
        //id + 时间戳 全局唯一
        CorrelationData correlationData = new CorrelationData("0987654321");
        rabbitTemplate.convertAndSend("exchange-2", "springboot.order", order, correlationData);
    }
}
