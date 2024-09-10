package com.javaedge.rabbitmq.api.exchange.header;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @author JavaEdge
 */
public class Producer4HeadersExchange {
    public static void main(String[] args) throws Exception {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");
        connectionFactory.setPort(5672);
        connectionFactory.setVirtualHost("/");

        Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();

        String exchangeName = "test_headers_exchange";

        for (int i = 0; i < 4; i++) {
            String msg = "Hello World RabbitMQ 4 HEADERS Exchange Message ...";

            // 设置消息的头部信息
            Map<String, Object> headers = new HashMap<>();
            headers.put("x-match", "any"); // "any" 表示只要有一个匹配即可，"all" 表示所有都要匹配
            headers.put("name", "JavaEdge");
            headers.put("age", "30");

            channel.basicPublish(exchangeName, "", new com.rabbitmq.client.AMQP.BasicProperties.Builder()
                    .headers(headers)
                    .build(), msg.getBytes());
        }

        channel.close();
        connection.close();
    }
}