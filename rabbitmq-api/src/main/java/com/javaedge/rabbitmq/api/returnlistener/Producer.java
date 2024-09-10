package com.javaedge.rabbitmq.api.returnlistener;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * @author JavaEdge
 */
public class Producer {
    public static void main(String[] args) throws Exception {
        // 创建连接工厂
        ConnectionFactory connectionFactory = new ConnectionFactory();
        // 设置主机地址
        connectionFactory.setHost("localhost");
        // 设置端口号
        connectionFactory.setPort(5672);
        // 设置虚拟主机
        connectionFactory.setVirtualHost("/");
        // 创建连接
        Connection connection = connectionFactory.newConnection();
        // 创建通道
        Channel channel = connection.createChannel();
        // 定义交换机名称
        String exchange = "test_return_exchange";
        // 错误的路由键
//        String routingKeyError = "abc.save";
        // 路由键
        String routingKeyError = "return.#";
        // 定义消息内容
        String msg = "JavaEdge RabbitMQ Return Message";

        // 添加ReturnListener，用于处理无法路由的消息
        channel.addReturnListener((replyCode, replyText, exchange1, routingKey1, properties, body) -> {
            System.err.println("---------Handle  Return----------");
            System.err.println("replyCode: " + replyCode);
            System.err.println("replyText: " + replyText);
            System.err.println("exchange: " + exchange1);
            System.err.println("routingKey: " + routingKey1);
            System.err.println("properties: " + properties);
            System.err.println("body: " + new String(body));
        });
        // 发布消息，第三个参数mandatory设置为true，表示如果消息无法路由，则返回给生产者
        channel.basicPublish(exchange, routingKeyError, true, null, msg.getBytes());
        // 如果将mandatory设置为false，则消息无法路由时会被丢弃
        // channel.basicPublish(exchange, routingKeyError, false, null, msg.getBytes());
    }
}