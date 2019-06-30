package com.javaedge.rabbitmq.api.limit;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * 限流策略 - Pro
 *
 * @author JavaEdge
 */
public class Producer {
    public static void main(String[] args) throws Exception {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");
        connectionFactory.setPort(5672);
        connectionFactory.setVirtualHost("/");
        Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();
        String exchange = "test_qos_exchange";
        String routingKey = "qos.save";
            String msg = "JavaEdge RabbitMQ Qos Message";
        for (int i = 0; i < 3; i++) {
            channel.basicPublish(exchange, routingKey, true, null, msg.getBytes());
        }

    }
}




