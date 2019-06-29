package com.javaedge.rabbitmq.api.message;

import java.util.HashMap;
import java.util.Map;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
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
		Map<String, Object> headers = new HashMap<>(16);
		headers.put("MyAttr1", "JavaEdge");
		headers.put("MyAttr2", "公众号");

		AMQP.BasicProperties properties = new AMQP.BasicProperties.Builder()
				.deliveryMode(2)
				.contentEncoding("UTF-8")
				.expiration("10000")
				.headers(headers)
				.build();

		for(int i=0; i < 5; i++){
			String msg = "Hello RabbitMQ!";
			channel.basicPublish("", "test_message", properties, msg.getBytes());
		}
		channel.close();
		connection.close();
	}
}
