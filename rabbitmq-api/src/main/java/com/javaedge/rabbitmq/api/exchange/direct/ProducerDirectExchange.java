package com.javaedge.rabbitmq.api.exchange.direct;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * 直连模式-生产者
 *
 * @author JavaEdge
 */
public class ProducerDirectExchange {
	public static void main(String[] args) throws Exception {
		//1 创建ConnectionFactory
		ConnectionFactory connectionFactory = new ConnectionFactory();
		connectionFactory.setHost("localhost");
		connectionFactory.setPort(5672);
		connectionFactory.setVirtualHost("/");
		
		//2 创建Connection
		Connection connection = connectionFactory.newConnection();

		//3 创建Channel
		Channel channel = connection.createChannel();

		//4 声明
		String exchangeName = "test_direct_exchange";
		String routingKey = "test.direct";

		//5 发送
		String msg = "Hello JavaEdge RabbitMQ Direct Exchange Message ... ";
		channel.basicPublish(exchangeName, routingKey , null , msg.getBytes());
	}
}