package com.javaedge.rabbitmq.api.confirm;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.QueueingConsumer.Delivery;

/**
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

		String exchangeName = "test_confirm_exchange";
		String routingKey = "confirm.#";
		String queueName = "test_confirm_queue";
		channel.exchangeDeclare(exchangeName, "topic", true);
		channel.queueDeclare(queueName, true, false, false, null);
		channel.queueBind(queueName, exchangeName, routingKey);
		QueueingConsumer queueingConsumer = new QueueingConsumer(channel);
		channel.basicConsume(queueName, true, queueingConsumer);

		while(true){
			Delivery delivery = queueingConsumer.nextDelivery();
			String msg = new String(delivery.getBody());
			System.err.println("Con: " + msg);
		}
	}
}

