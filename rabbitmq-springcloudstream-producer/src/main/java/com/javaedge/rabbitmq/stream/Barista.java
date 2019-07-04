package com.javaedge.rabbitmq.stream;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

/**
 * 这里的Barista接口是定义来作为后面类的参数，这一接口定义来通道类型和通道名称。
 * 通道名称是作为配置用，通道类型则决定了app会使用这一通道进行发送消息还是从中接收消息。
 *
 * @author JavaEdge
 */
public interface Barista {
	  
//    String INPUT_CHANNEL = "input_channel";
    String OUTPUT_CHANNEL = "output_channel";  

//    @Input(Barista.INPUT_CHANNEL)  
//    SubscribableChannel loginput();

    /**
     *
     * @return
     */
    @Output(Barista.OUTPUT_CHANNEL)
    MessageChannel logoutput();  

//	String INPUT_BASE = "queue-1";  
//	String OUTPUT_BASE = "queue-1";  
//	@Input(Barista.INPUT_BASE)  
//	SubscribableChannel input1();  
//	MessageChannel output1();
}
