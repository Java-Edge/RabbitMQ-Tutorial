package com.javaedge.rabbitmq.api.limit;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * 限流策略 - Con
 *
 * @author JavaEdge
 */
public class Consumer {
	public static void main(String[] args) throws Exception {
		ConnectionFactory connectionFactory = new ConnectionFactory();
		connectionFactory.setHost("localhost");
		connectionFactory.setPort(5672);
		connectionFactory.setVirtualHost("/");
		Connection connection = connectionFactory.newConnection();
		Channel channel = connection.createChannel();

		String exchangeName = "test_qos_exchange";
		String queueName = "test_qos_queue";
		String routingKey = "qos.#";

		channel.exchangeDeclare(exchangeName, "topic", true, false, null);
		channel.queueDeclare(queueName, true, false, false, null);
		channel.queueBind(queueName, exchangeName, routingKey);


		channel.basicQos(0, 1, false);
		// 限流方式 autoAck必须设置为false
		channel.basicConsume(queueName, false, new MyConsumer(channel));
	}
}

