package com.javaedge.springboot.conusmer;

import java.util.Map;

import com.javaedge.springboot.entity.Order;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import com.rabbitmq.client.Channel;

/**
 * @author JavaEdge
 */
@Component
public class RabbitReceiver {

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "queue-1",
                    durable = "true"),
            exchange = @Exchange(value = "exchange-1",
                    durable = "true",
                    type = "topic",
                    ignoreDeclarationExceptions = "true"),
            key = "springboot.*")
    )
    @RabbitHandler
    public void onMessage(Message message, Channel channel) throws Exception {
        System.err.println("--------------------------------------");
        System.err.println("Con - Payload: " + message.getPayload());
        Long deliveryTag = (Long) message.getHeaders().get(AmqpHeaders.DELIVERY_TAG);
        //手工ACK
        channel.basicAck(deliveryTag, false);
    }


    /**
     * 对应配置文件内容
     *
     * @param order
     * @param channel
     * @param headers
     * @throws Exception
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "${spring.rabbitmq.listener.order.queue.name}",
                    durable = "${spring.rabbitmq.listener.order.queue.durable}"),
            exchange = @Exchange(value = "${spring.rabbitmq.listener.order.exchange.name}",
                    durable = "${spring.rabbitmq.listener.order.exchange.durable}",
                    type = "${spring.rabbitmq.listener.order.exchange.type}",
                    ignoreDeclarationExceptions = "${spring.rabbitmq.listener.order.exchange.ignoreDeclarationExceptions}"),
            key = "${spring.rabbitmq.listener.order.key}")
    )
    @RabbitHandler
    public void onOrderMessage(@Payload Order order,
                               Channel channel,
                               @Headers Map<String, Object> headers) throws Exception {
        System.err.println("--------------------------------------");
        System.err.println("Con Order: " + order.getId());
        Long deliveryTag = (Long) headers.get(AmqpHeaders.DELIVERY_TAG);
        //手工ACK
        channel.basicAck(deliveryTag, false);
    }


}
