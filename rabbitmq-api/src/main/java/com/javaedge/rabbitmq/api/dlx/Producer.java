package com.javaedge.rabbitmq.api.dlx;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * 死信队列 - Pro
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

        // 自定义的普通交换机,非死信队列!
        String exchange = "test_dlx_exchange";
        // 普通路由key
        String routingKey = "dlx.save";
        String msg = "JavaEdge RabbitMQ DLX Message";

        for (int i = 0; i < 1; i++) {
            AMQP.BasicProperties properties = new AMQP.BasicProperties.Builder()
                    .deliveryMode(2)
                    .contentEncoding("UTF-8")
                    .expiration("10000")
                    .build();
            channel.basicPublish(exchange, routingKey, true, properties, msg.getBytes());
        }
    }
}
