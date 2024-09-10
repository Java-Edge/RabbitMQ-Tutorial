package com.javaedge.spring.pattern.send;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * 工作队列模式
 * 一个pro，一个默认的交换机（DirectExchange），一个队列，两个或更多con
 * 注意点:
 * 一个队列对应多个消费者，默认，由队列对消息进行平均分配，消息会被分到不同的消费者手中。
 * 消费者可以配置各自的并发能力，进而提高消息的消费能力，也可以配置手动 ack，来决定是否要消费某一条消息
 */
@Slf4j
@Component
@SuppressWarnings("all")
public class SendService2 {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    @Qualifier("queue002")
    private Queue myQueue2;

    @Qualifier("queue003")
    @Autowired
    private Queue myQueue3;

    /**
     * @param msg
     * @param type 0、默认工作队列模式 1、手动ack
     */
    public void sendMsg(String msg, Integer type) {
        try {
            if (type == 0) {
                rabbitTemplate.convertAndSend(myQueue2.getName(), msg);
            } else {
                rabbitTemplate.convertAndSend(myQueue3.getName(), msg);
            }
        } catch (Exception e) {
            log.error("发送消息失败", e);
        }
    }
}