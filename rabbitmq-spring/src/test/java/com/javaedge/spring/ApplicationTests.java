package com.javaedge.spring;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.javaedge.spring.entity.Order;
import com.javaedge.spring.entity.Packaged;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author JavaEdge
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ApplicationTests {
	@Test
	public void contextLoads() {
	}

	@Autowired
	private RabbitAdmin rabbitAdmin;

	@Test
	public void testAdmin() throws Exception {
		rabbitAdmin.declareExchange(new DirectExchange("test.direct", false, false));
		rabbitAdmin.declareQueue(new Queue("test.direct.queue", false));
		rabbitAdmin.declareBinding(new Binding("test.direct.queue",
				Binding.DestinationType.QUEUE,
				"test.direct", "direct", new HashMap<>()));

		rabbitAdmin.declareExchange(new TopicExchange("test.topic", false, false));
		rabbitAdmin.declareQueue(new Queue("test.topic.queue", false));
		rabbitAdmin.declareBinding(
				BindingBuilder
						.bind(new Queue("test.topic.queue", false))		//直接创建队列
						.to(new TopicExchange("test.topic", false, false))	//直接创建交换机 建立关联关系
						.with("user.#"));	//指定路由Key


		rabbitAdmin.declareExchange(new FanoutExchange("test.fanout", false, false));
		rabbitAdmin.declareQueue(new Queue("test.fanout.queue", false));
		rabbitAdmin.declareBinding(
				BindingBuilder
				.bind(new Queue("test.fanout.queue", false))
				.to(new FanoutExchange("test.fanout", false, false)));

		//清空队列数据
		rabbitAdmin.purgeQueue("test.topic.queue", false);
	}

	
	@Autowired
	private RabbitTemplate rabbitTemplate;
	
	
	@Test
	public void testSendMessage() throws Exception {
		MessageProperties messageProperties = new MessageProperties();
		messageProperties.getHeaders().put("desc", "信息描述..");
		messageProperties.getHeaders().put("type", "自定义消息类型..");
		Message message = new Message("JavaEdge RabbitMQ".getBytes(), messageProperties);
		
		rabbitTemplate.convertAndSend("topic001", "spring.amqp", message, message1 -> {
			System.err.println("------添加额外的设置---------");
			message1.getMessageProperties().getHeaders().put("desc", "额外修改的信息描述");
			message1.getMessageProperties().getHeaders().put("attr", "额外新加的属性");
			return message1;
		});
	}
	
	@Test
	public void testSendMessage2() throws Exception {
		//1 创建消息
		MessageProperties messageProperties = new MessageProperties();
		messageProperties.setContentType("text/plain");
		Message message = new Message("RabbitMQ Message JavaEdge".getBytes(), messageProperties);
		
		rabbitTemplate.send("topic001", "spring.abc", message);
		
		rabbitTemplate.convertAndSend("topic001", "spring.amqp", "JavaEdge Object Message Send!");
		rabbitTemplate.convertAndSend("topic002", "rabbit.abc", "JavaEdge Object Message Send!");
	}
	
	@Test
	public void testSendMessage4Text() throws Exception {
		MessageProperties messageProperties = new MessageProperties();
		messageProperties.setContentType("text/plain");
		Message message = new Message("RabbitMQ Message JavaEdge".getBytes(), messageProperties);
		
		rabbitTemplate.send("topic001", "spring.abc", message);
		rabbitTemplate.send("topic002", "rabbit.abc", message);
	}
	
	
	@Test
	public void testSendJsonMessage() throws Exception {
		Order order = new Order();
		order.setId("001");
		order.setName("Message Order");
		order.setContent("Description Information");
		ObjectMapper mapper = new ObjectMapper();
		String json = mapper.writeValueAsString(order);
		System.err.println("Order 4 JSON: " + json);
		
		MessageProperties messageProperties = new MessageProperties();
		//这里注意一定要修改contentType为 application/json
		messageProperties.setContentType("application/json");
		Message message = new Message(json.getBytes(), messageProperties);
		
		rabbitTemplate.send("topic001", "spring.order", message);
	}
	
	@Test
	public void testSendJavaMessage() throws Exception {
		Order order = new Order();
		order.setId("001");
		order.setName("Order Message");
		order.setContent("Order Description Information");
		ObjectMapper mapper = new ObjectMapper();
		String json = mapper.writeValueAsString(order);
		System.err.println("Order 4 JSON: " + json);
		
		MessageProperties messageProperties = new MessageProperties();
		//这里注意一定要修改contentType为 application/json
		messageProperties.setContentType("application/json");
		messageProperties.getHeaders().put("__TypeId__", "com.javaedge.spring.entity.Order");
		Message message = new Message(json.getBytes(), messageProperties);
		
		rabbitTemplate.send("topic001", "spring.order", message);
	}
	
	@Test
	public void testSendMappingMessage() throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		
		Order order = new Order();
		order.setId("001");
		order.setName("Order Message");
		order.setContent("Order Description Information");
		String orderJson = mapper.writeValueAsString(order);
		System.err.println("Order 4 JSON: " + orderJson);
		
		MessageProperties messageProperties1 = new MessageProperties();
		//这里注意一定要修改contentType为 application/json
		messageProperties1.setContentType("application/json");
		messageProperties1.getHeaders().put("__TypeId__", "order");
		Message message1 = new Message(orderJson.getBytes(), messageProperties1);
		rabbitTemplate.send("topic001", "spring.order", message1);
		
		Packaged pack = new Packaged();
		pack.setId("002");
		pack.setName("Package Message");
		pack.setDescription("Package Description Information");
		
		String packageJson = mapper.writeValueAsString(pack);
		System.err.println("pack 4 json: " + packageJson);

		MessageProperties messageProperties2 = new MessageProperties();
		//这里注意一定要修改contentType为 application/json
		messageProperties2.setContentType("application/json");
		messageProperties2.getHeaders().put("__TypeId__", "packaged");
		Message message2 = new Message(packageJson.getBytes(), messageProperties2);
		rabbitTemplate.send("topic001", "spring.pack", message2);
	}
	
	@Test
	public void testSendExtConverterMessage() throws Exception {
			byte[] body = Files.readAllBytes(Paths.get("/Volumes/doc/test_file/", "head.png"));
			MessageProperties messageProperties = new MessageProperties();
			messageProperties.setContentType("image/png");
			messageProperties.getHeaders().put("extName", "png");
			Message message = new Message(body, messageProperties);
			// 经过默认exchange
			rabbitTemplate.send("", "image_queue", message);
		
//			byte[] body = Files.readAllBytes(Paths.get("/Volumes/doc/test_file/", "mysql.pdf"));
//			MessageProperties messageProperties = new MessageProperties();
//			messageProperties.setContentType("application/pdf");
//			Message message = new Message(body, messageProperties);
//			rabbitTemplate.send("", "pdf_queue", message);
	}
}
