package com.javaedge.rabbitmq.api.returnlistener;

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
        String exchange = "test_return_exchange";
        String routingKeyError = "abc.save";
        String msg = "JavaEdge RabbitMQ Return Message";

        channel.addReturnListener((replyCode, replyText, exchange1, routingKey1, properties, body) -> {
            System.err.println("---------Handle  Return----------");
            System.err.println("replyCode: " + replyCode);
            System.err.println("replyText: " + replyText);
            System.err.println("exchange: " + exchange1);
            System.err.println("routingKey: " + routingKey1);
            System.err.println("properties: " + properties);
            System.err.println("body: " + new String(body));
        });
        channel.basicPublish(exchange, routingKeyError, true, null, msg.getBytes());
//        channel.basicPublish(exchange, routingKeyError, false, null, msg.getBytes());
    }
}
