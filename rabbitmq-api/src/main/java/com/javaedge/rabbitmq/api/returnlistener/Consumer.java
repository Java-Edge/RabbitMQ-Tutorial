package com.javaedge.rabbitmq.api.returnlistener;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.QueueingConsumer.Delivery;

/**
 * @author JavaEdge
 */
/**
 * @author JavaEdge
 */
public class Consumer {
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
        String exchangeName = "test_return_exchange";
        // 定义路由键
        String routingKey = "return.#";
        // 定义队列名称
        String queueName = "test_return_queue";
        // 声明交换机
        channel.exchangeDeclare(exchangeName, "topic", true, false, null);
        // 声明队列
        channel.queueDeclare(queueName, true, false, false, null);
        // 绑定队列到交换机
        channel.queueBind(queueName, exchangeName, routingKey);
        // 创建队列消费者
        QueueingConsumer queueingConsumer = new QueueingConsumer(channel);
        // 消费队列中的消息
        channel.basicConsume(queueName, true, queueingConsumer);

        // 无限循环，等待消息
        while (true) {
            // 获取下一条消息
            Delivery delivery = queueingConsumer.nextDelivery();
            // 将消息体转换为字符串
            String msg = new String(delivery.getBody());
            // 打印消息
            System.err.println("Consumer: " + msg);
        }
    }
}