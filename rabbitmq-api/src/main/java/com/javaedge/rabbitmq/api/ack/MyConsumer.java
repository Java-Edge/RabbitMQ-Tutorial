package com.javaedge.rabbitmq.api.ack;

import java.io.IOException;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

/**
 * ACK & 重回队列 - 自定义Con
 *
 * @author JavaEdge
 */
public class MyConsumer extends DefaultConsumer {
    private final Channel channel;

    public MyConsumer(Channel channel) {
        super(channel);
        this.channel = channel;
    }

    @Override
    public void handleDelivery(String consumerTag, Envelope envelope,
                               AMQP.BasicProperties properties, byte[] body) throws IOException {
        System.err.println("-----------Consume Message----------");
        System.err.println("body: " + new String(body));
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if ((Integer) properties.getHeaders().get("num") == 0) {
            channel.basicNack(envelope.getDeliveryTag(), false, true);
        } else {
            channel.basicAck(envelope.getDeliveryTag(), false);
        }
    }
}