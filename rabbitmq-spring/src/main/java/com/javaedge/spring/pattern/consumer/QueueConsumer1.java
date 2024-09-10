package com.javaedge.spring.pattern.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@SuppressWarnings("all")
@Slf4j
@RabbitListener(queues = "testQueue")
@Component
public class QueueConsumer1 {

    @RabbitHandler
    public void receive(String param) {
        log.info("接收到:" + param);
    }

}
