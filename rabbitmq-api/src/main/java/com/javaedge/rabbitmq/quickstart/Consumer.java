package com.javaedge.rabbitmq.quickstart;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.QueueingConsumer.Delivery;

/**
 * @author JavaEdge
 */
public class Consumer {

	public static void main(String[] args) throws Exception {
		
		//step1:创建一个ConnectionFactory, 并进行基础配置
		ConnectionFactory connectionFactory = new ConnectionFactory();
		connectionFactory.setHost("localhost");
		connectionFactory.setPort(5672);
		connectionFactory.setVirtualHost("/");
		
		//step2:通过连接工厂创建连接
		Connection connection = connectionFactory.newConnection();
		
		//step3:通过connection创建一个Channel
		Channel channel = connection.createChannel();
		
		//step4:声明一个队列
		String queueName = "test001";
		channel.queueDeclare(queueName, true, false, false, null);
		
		//5 创建消费者
		QueueingConsumer queueingConsumer = new QueueingConsumer(channel);
		
		//6 设置Channel
		channel.basicConsume(queueName, true, queueingConsumer);
		
		while(true){
			//7 获取消息
			Delivery delivery = queueingConsumer.nextDelivery();
			String msg = new String(delivery.getBody());
			System.err.println("消费端: " + msg);
			//Envelope envelope = delivery.getEnvelope();
		}
		
	}
}
