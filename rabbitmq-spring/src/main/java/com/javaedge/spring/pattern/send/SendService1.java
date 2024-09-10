//package com.javaedge.spring.pattern.send;
//
//import lombok.extern.slf4j.Slf4j;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.amqp.core.Queue;
//import org.springframework.amqp.rabbit.core.RabbitTemplate;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.stereotype.Component;
//
///**
// * 模拟队列中的发送消息
// * 【简单模式】
// * 走默认的交换机，我们需要提供一个生产者一个队列以及一个消费者即可
// */
//@Slf4j
//@Component
//@SuppressWarnings("all")
//public class SendService1 {
//    private static final Logger logger = LoggerFactory.getLogger(SendService1.class);
//
//    @Autowired
//    private RabbitTemplate rabbitTemplate;
//
//    @Autowired
//    @Qualifier("queue001")
//    private Queue myQueue;
//
//    public void sendMsg(String msg) {
//        try {
//            rabbitTemplate.convertAndSend(myQueue.getName(), msg);
//        } catch (Exception e) {
//            System.out.println(e);
//            logger.error("调用失败,原因是:" + e.getStackTrace());
//            throw new RuntimeException();
//        }
//    }
//}