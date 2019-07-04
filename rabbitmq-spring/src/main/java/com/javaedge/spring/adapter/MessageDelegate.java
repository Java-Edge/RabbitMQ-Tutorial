package com.javaedge.spring.adapter;

import java.io.File;
import java.util.Map;

import com.javaedge.spring.entity.Order;
import com.javaedge.spring.entity.Packaged;


/**
 * @author JavaEdge
 */
public class MessageDelegate {

	public void handleMessage(byte[] messageBody) {
		System.err.println("Default Method,Message Content:" + new String(messageBody));
	}
	
	public void consumeMessage(byte[] messageBody) {
		System.err.println("Byte Array Method, Message Content:" + new String(messageBody));
	}

	public void consumeMessage(String messageBody) {
		System.err.println("String Method,Message Content:" + messageBody);
	}

	public void method1(String messageBody) {
		System.err.println("method1 gets message content:" + messageBody);
	}

	public void method2(String messageBody) {
		System.err.println("method2 gets message content:" + messageBody);
	}


	public void consumeMessage(Map messageBody) {
		System.err.println("map方法, 消息内容:" + messageBody);
	}


	public void consumeMessage(Order order) {
		System.err.println("order对象, 消息内容, id: " + order.getId() +
				", name: " + order.getName() +
				", content: "+ order.getContent());
	}

	public void consumeMessage(Packaged pack) {
		System.err.println("package对象, 消息内容, id: " + pack.getId() +
				", name: " + pack.getName() +
				", content: "+ pack.getDescription());
	}

	public void consumeMessage(File file) {
		System.err.println("文件对象 方法, 消息内容:" + file.getName());
	}
}

