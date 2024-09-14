package com.javaedge.spring;

import com.javaedge.spring.adapter.MessageDelegate;
import com.javaedge.spring.convert.ImageMessageConverter;
import com.javaedge.spring.convert.PDFMessageConverter;
import com.javaedge.spring.convert.TextMessageConverter;
import com.javaedge.spring.util.MqConstant;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.amqp.support.converter.ContentTypeDelegatingMessageConverter;
import org.springframework.amqp.support.converter.DefaultJackson2JavaTypeMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author JavaEdge
 */
@Configuration
@ComponentScan({"com.javaedge.spring.*"})
public class RabbitMQConfig {

    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setAddresses("localhost:5672");
        connectionFactory.setUsername("guest");
        connectionFactory.setPassword("guest");
        connectionFactory.setVirtualHost("/");
//        // 开启confirm模式
//        connectionFactory.setPublisherConfirms(true);
        return connectionFactory;
    }

    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        RabbitAdmin rabbitAdmin = new RabbitAdmin(connectionFactory);
        rabbitAdmin.setAutoStartup(true);
        return rabbitAdmin;
    }

    /**
     * 针对消费者配置
     * 1. 设置交换机类型
     * 2. 将队列绑定到交换机
     * FanoutExchange: 将消息分发到所有的绑定队列，无 RoutingKey 的概念
     * HeadersExchange ：通过添加属性key-value匹配
     * DirectExchange: 按照 RoutingKey 分发到指定队列
     * TopicExchange: 多关键字匹配
     */
    @Bean
    public TopicExchange exchange001() {
        return new TopicExchange("topic001", true, false);
    }

    /**
     * 简单模式测试队列
     */
    @Bean
    public Queue myQueue() {
        return new Queue("testQueue", true);
    }

    /**
     * 工作模式测试队列
     */
    @Bean
    public Queue myQueue2() {
        return new Queue("workQueue", true);
    }

    /**
     * 工作模式测试队列
     *
     * @return
     */
    @Bean
    public Queue myQueue3() {
        return new Queue(MqConstant.MQ_QUEUE_2_Manual_Ack, true);
    }

    @Bean
    public Queue queue001() {
        //队列持久
        return new Queue("queue001", true);
    }

    @Bean
    public Binding binding001() {
        return BindingBuilder.bind(queue001()).to(exchange001()).with("spring.*");
    }

    @Bean
    public TopicExchange exchange002() {
        return new TopicExchange("topic002", true, false);
    }

    @Bean
    public Queue queue002() {
        return new Queue("queue002", true);
    }

    @Bean
    public Binding binding002() {
        return BindingBuilder.bind(queue002()).to(exchange002()).with("rabbit.*");
    }

    @Bean
    public Queue queue003() {
        return new Queue("queue003", true);
    }

    @Bean
    public Binding binding003() {
        return BindingBuilder.bind(queue003()).to(exchange001()).with("mq.*");
    }

    @Bean
    public Queue queue_image() {
        return new Queue("image_queue", true);
    }

    @Bean
    public Queue queue_pdf() {
        return new Queue("pdf_queue", true);
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        return rabbitTemplate;
    }

    @Bean
    public SimpleMessageListenerContainer messageContainer(ConnectionFactory connectionFactory) {

        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(connectionFactory);
        container.setQueues(queue001(), queue002(), queue003(), queue_image(), queue_pdf());
        container.setConcurrentConsumers(1);
        container.setMaxConcurrentConsumers(5);
        container.setDefaultRequeueRejected(false);
        container.setAcknowledgeMode(AcknowledgeMode.AUTO);
        container.setExposeListenerChannel(true);
        container.setConsumerTagStrategy(queue -> queue + "_" + UUID.randomUUID());

        container.setMessageListener(new ChannelAwareMessageListener() {
            @Override
            public void onMessage(Message message, Channel channel) {
                String msg = new String(message.getBody());
                System.err.println("----------消费者: " + msg);
            }
        });

        /**
         * 适配器方式. 默认有自己的方法名：handleMessage
         * 亦可自定义方法名: consumeMessage
         * 亦可添加转换器: 从字节数组转换为String
         */
//    	MessageListenerAdapter adapter = new MessageListenerAdapter(new MessageDelegate());
//    	adapter.setDefaultListenerMethod("consumeMessage");
//    	adapter.setMessageConverter(new TextMessageConverter());
//    	container.setMessageListener(adapter);

        /**
         *  适配器方式: 队列 - 方法绑定
         */
//    	MessageListenerAdapter adapter = new MessageListenerAdapter(new MessageDelegate());
//    	adapter.setMessageConverter(new TextMessageConverter());
//    	Map<String, String> queueOrTagToMethodName = new HashMap<>(16);
//    	queueOrTagToMethodName.put("queue001", "method1");
//    	queueOrTagToMethodName.put("queue002", "method2");
//    	adapter.setQueueOrTagToMethodName(queueOrTagToMethodName);
//    	container.setMessageListener(adapter);

        /**
         * 支持JSON格式的转换器
         */
//        MessageListenerAdapter adapter = new MessageListenerAdapter(new MessageDelegate());
//        adapter.setDefaultListenerMethod("consumeMessage");
//
//        Jackson2JsonMessageConverter jackson2JsonMessageConverter = new Jackson2JsonMessageConverter();
//        adapter.setMessageConverter(jackson2JsonMessageConverter);
//
//        container.setMessageListener(adapter);

        /**
         * DefaultJackson2JavaTypeMapper & Jackson2JsonMessageConverter 支持Java对象转换
         */
//        MessageListenerAdapter adapter = new MessageListenerAdapter(new MessageDelegate());
//        adapter.setDefaultListenerMethod("consumeMessage");
//
//        Jackson2JsonMessageConverter jackson2JsonMessageConverter = new Jackson2JsonMessageConverter();
//
//        DefaultJackson2JavaTypeMapper javaTypeMapper = new DefaultJackson2JavaTypeMapper();
//        jackson2JsonMessageConverter.setJavaTypeMapper(javaTypeMapper);
//
//        adapter.setMessageConverter(jackson2JsonMessageConverter);
//        container.setMessageListener(adapter);


        /**
         * DefaultJackson2JavaTypeMapper & Jackson2JsonMessageConverter 多个Java对象映射转换
         */
//        MessageListenerAdapter adapter = new MessageListenerAdapter(new MessageDelegate());
//        adapter.setDefaultListenerMethod("consumeMessage");
//        Jackson2JsonMessageConverter jackson2JsonMessageConverter = new Jackson2JsonMessageConverter();
//        DefaultJackson2JavaTypeMapper javaTypeMapper = new DefaultJackson2JavaTypeMapper();
//
//        Map<String, Class<?>> idClassMapping = new HashMap<>(16);
//		idClassMapping.put("order", com.javaedge.spring.entity.Order.class);
//		idClassMapping.put("packaged", com.javaedge.spring.entity.Packaged.class);
//
//		javaTypeMapper.setIdClassMapping(idClassMapping);
//
//		jackson2JsonMessageConverter.setJavaTypeMapper(javaTypeMapper);
//        adapter.setMessageConverter(jackson2JsonMessageConverter);
//        container.setMessageListener(adapter);

//        // ext convert
        MessageListenerAdapter adapter = new MessageListenerAdapter(new MessageDelegate());
        adapter.setDefaultListenerMethod("consumeMessage");
        /**
         * 全局转换器
         */
//		ContentTypeDelegatingMessageConverter convert = new ContentTypeDelegatingMessageConverter();
//
//		TextMessageConverter textConvert = new TextMessageConverter();
//		convert.addDelegate("text", textConvert);
//		convert.addDelegate("html/text", textConvert);
//		convert.addDelegate("xml/text", textConvert);
//		convert.addDelegate("text/plain", textConvert);
//
//		Jackson2JsonMessageConverter jsonConvert = new Jackson2JsonMessageConverter();
//		convert.addDelegate("json", jsonConvert);
//		convert.addDelegate("application/json", jsonConvert);
//
//		ImageMessageConverter imageConverter = new ImageMessageConverter();
//		convert.addDelegate("image/png", imageConverter);
//		convert.addDelegate("image", imageConverter);
//
//		PDFMessageConverter pdfConverter = new PDFMessageConverter();
//		convert.addDelegate("application/pdf", pdfConverter);
//
//		adapter.setMessageConverter(convert);
//		container.setMessageListener(adapter);

        return container;
    }
}
