package com.javaedge.rabbitmq.api.consumer;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * 自定义的Con - Pro
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
        String exchange = "test_consumer_exchange";
        String routingKey = "consumer.save";
        String msg = "JavaEdge RabbitMQ Consumer Message";
        for (int i = 0; i < 3; i++) {
            channel.basicPublish(exchange, routingKey, true, null, msg.getBytes());
        }

    }
}


