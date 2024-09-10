package com.javaedge.rabbitmq.api.exchange.header;
import com.rabbitmq.client.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @author JavaEdge
 */
public class Consumer4HeadersExchange {
    public static void main(String[] args) throws Exception {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");
        connectionFactory.setPort(5672);
        connectionFactory.setVirtualHost("/");
        connectionFactory.setAutomaticRecoveryEnabled(true);
        connectionFactory.setNetworkRecoveryInterval(3000);

        Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();

        String exchangeName = "test_headers_exchange";
        String exchangeType = "headers";
        String queueName = "test_headers_queue";

        // 声明交换机
        channel.exchangeDeclare(exchangeName, exchangeType, true, false, false, null);

        // 声明队列
        channel.queueDeclare(queueName, false, false, false, null);

        // 绑定队列到交换机，并设置头部匹配规则
        Map<String, Object> headers = new HashMap<>();
        headers.put("x-match", "any"); // "any" 表示只要有一个匹配即可，"all" 表示所有都要匹配
        headers.put("name", "JavaEdge");
        headers.put("age", "30");

        channel.queueBind(queueName, exchangeName, "", headers);

        QueueingConsumer consumer = new QueueingConsumer(channel);
        channel.basicConsume(queueName, true, consumer);

        while (true) {
            QueueingConsumer.Delivery delivery = consumer.nextDelivery();
            String msg = new String(delivery.getBody());
            System.out.println("Get Message：" + msg);
        }
    }
}
