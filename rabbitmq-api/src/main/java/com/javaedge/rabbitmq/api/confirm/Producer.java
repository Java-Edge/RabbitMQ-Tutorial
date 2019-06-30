package com.javaedge.rabbitmq.api.confirm;

import java.io.IOException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmListener;
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
		// 指定消息投递模式: confirm机制
		channel.confirmSelect();
		String exchangeName = "test_confirm_exchange";
		String routingKey = "confirm.save";
		String msg = "JavaEdge RabbitMQ Send Confirm Message!";
		channel.basicPublish(exchangeName, routingKey, null, msg.getBytes());
		// 添加一个确认监听
		channel.addConfirmListener(new ConfirmListener() {
			@Override
			public void handleNack(long deliveryTag, boolean multiple) throws IOException {
				System.err.println("-------NO ACK!-----------");
			}
			
			@Override
			public void handleAck(long deliveryTag, boolean multiple) throws IOException {
				System.err.println("-------ACK!-----------");
			}
		});
	}
}

