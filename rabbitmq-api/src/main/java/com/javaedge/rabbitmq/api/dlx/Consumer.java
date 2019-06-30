package com.javaedge.rabbitmq.api.dlx;

import java.util.HashMap;
import java.util.Map;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * 死信队列 - Con
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
		
		// 普通的交换机/队列/路由!
		String exchangeName = "test_dlx_exchange";
		String routingKey = "dlx.#";
		String queueName = "test_dlx_queue";
		
		channel.exchangeDeclare(exchangeName, "topic", true, false, null);
		
		Map<String, Object> arguments = new HashMap<>(16);
		arguments.put("x-dead-letter-exchange", "dlx.exchange");
		// arguments属性,要设置到普通队列的声明
		channel.queueDeclare(queueName, true, false, false, arguments);
		channel.queueBind(queueName, exchangeName, routingKey);
		
		// 声明死信队列
		channel.exchangeDeclare("dlx.exchange", "topic", true, false, null);
		channel.queueDeclare("dlx.queue", true, false, false, null);
		channel.queueBind("dlx.queue", "dlx.exchange", "#");
		
		channel.basicConsume(queueName, true, new MyConsumer(channel));
	}
}

