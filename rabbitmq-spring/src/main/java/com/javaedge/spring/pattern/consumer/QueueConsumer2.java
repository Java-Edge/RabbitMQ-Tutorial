package com.javaedge.spring.pattern.consumer;

import com.javaedge.spring.util.MqConstant;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import java.io.IOException;

@SuppressWarnings("all")
@Slf4j
@Component
public class QueueConsumer2 {

    @RabbitListener(queues = "workQueue")
    public void receive(String param) {
        log.info(String.format("消费者%s,从队列-%s-接收到消息:%s,消费线程为:%s", "receive", "workQueue", param, Thread.currentThread().getName()));
    }

    /**
     * 消费者2: 配置了 concurrency 为 20，此时，对于第二个消费者，将会同时存在 20 个子线程去消费消息
     *
     * @param param
     */
    @RabbitListener(queues = "workQueue", concurrency = "20")
    public void receive2(String param) {
        log.info(String.format("消费者%s,从队列-%s-接收到消息:%s,消费线程为:%s", "receive2", "workQueue", param, Thread.currentThread().getName()));
    }


    /**
     * 手动确认-测试-需要添加-spring.rabbitmq.listener.simple.acknowledge-mode=manual
     *
     * @param message
     * @param channel
     * @throws IOException
     */
    @RabbitListener(queues = MqConstant.MQ_QUEUE_2_Manual_Ack)
    public void receiveManualAck(Message message, Channel channel) throws IOException {
        log.info(String.format("消费者%s,从队列-%s-接收到消息:%s,消费线程为:%s", "receiveManualAck", MqConstant.MQ_QUEUE_2_Manual_Ack, message.getPayload(), Thread.currentThread().getName()));
        channel.basicAck(((Long) message.getHeaders().get(AmqpHeaders.DELIVERY_TAG)), true);

    }

    /**
     * 消费者2: basicReject 拒绝了所有消息，receiveManualAck消费者消费了所有消息
     *
     * @param param
     */
    @RabbitListener(queues = MqConstant.MQ_QUEUE_2_Manual_Ack, concurrency = "20")
    public void receive2ManualAck(Message message, Channel channel) throws IOException {
        log.info(String.format("消费者%s,从队列-%s-接收到消息:%s,消费线程为:%s", "receive2ManualAck", MqConstant.MQ_QUEUE_2_Manual_Ack, message.getPayload(), Thread.currentThread().getName()));
        channel.basicReject(((Long) message.getHeaders().get(AmqpHeaders.DELIVERY_TAG)), true);
    }


}
