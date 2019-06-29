package com.javaedge.rabbitmq.api.exchange.direct;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.QueueingConsumer.Delivery;

/**
 * 直连模式-消费者
 *
 * @author JavaEdge
 */
public class ConsumerDirectExchange {
	public static void main(String[] args) throws Exception {
        ConnectionFactory connectionFactory = new ConnectionFactory() ;
        connectionFactory.setHost("localhost");
        connectionFactory.setPort(5672);
		connectionFactory.setVirtualHost("/");
		// 自动重连(3s)
        connectionFactory.setAutomaticRecoveryEnabled(true);
        connectionFactory.setNetworkRecoveryInterval(3000);
        Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();
        // name和Pro中的一致
		String exchangeName = "test_direct_exchange";
		String exchangeType = "direct";
		String queueName = "test_direct_queue";
		String routingKey = "test.direct";

		channel.exchangeDeclare(exchangeName, exchangeType, true, false, false, null);
		channel.queueDeclare(queueName, false, false, false, null);
		//建立绑定关系
		channel.queueBind(queueName, exchangeName, routingKey);
        //durable 是否持久化消息
        QueueingConsumer consumer = new QueueingConsumer(channel);
        //参数：队列名称、是否自动ACK、Consumer
        channel.basicConsume(queueName, true, consumer);
        while(true){  
            //获取消息，如果没有消息，该步将会阻塞
            Delivery delivery = consumer.nextDelivery();  
            String msg = new String(delivery.getBody());    
            System.out.println("Get Message：" + msg);
        } 
	}
}
