package com.javaedge.spring.util;

/**
 * @author 福参
 * @Description
 * @date 2023/2/7
 */
public class MqConstant {
    /**
     * 测试消息通道
     */
    public final static String MQ_QUEUE_1="testQueue";

    /**
     * 测试消息通道
     */
    public final static String MQ_QUEUE_2="workQueue";

    /**
     * 测试消息通道-手动ack
     */
    public final static String MQ_QUEUE_2_Manual_Ack="workQueueManualAck";

    /**
     * 测试消息通道-发布订阅模式-交换机类型-direct-队列
     */
    public final static String MQ_QUEUE_3_Direct="myQueue2Direct";
    /**
     * 测试消息通道-发布订阅模式-交换机类型-direct-交换机名称
     */
    public final static String Exchange_Name_Direct="exchangeDirect";


    //测试消息通道-发布订阅模式-交换机类型-Fanout-队列
    public final static String MQ_QUEUE_3_Fanout_1="myQueue2Fanout1";
    public final static String MQ_QUEUE_3_Fanout_2="myQueue2Fanout2";
    //测试消息通道-发布订阅模式-交换机类型-Fanout-交换机名称
    public final static String Exchange_Name_Fanout="exchangeFanout";



    //测试消息通道-发布订阅模式-交换机类型-Topic-队列
    public final static String MQ_QUEUE_3_Topic_1="myQueue2Topic1_hangzhou";
    public final static String MQ_QUEUE_3_Topic_2="myQueue2Topic2_suzhou";
    public final static String MQ_QUEUE_3_Topic_3="myQueue2Topic3_zhongguo";
    //测试消息通道-发布订阅模式-交换机类型-Topic-交换机名称
    public final static String Exchange_Name_Topic="exchangeTopic";


    //测试消息通道-发布订阅模式-交换机类型-Headers-队列
    public final static String MQ_QUEUE_3_Headers_1="myQueue2Headers_hangzhou";
    public final static String MQ_QUEUE_3_Headers_2="myQueue2Headers_suzhou";
    //测试消息通道-发布订阅模式-交换机类型-Headers-交换机名称
    public final static String Exchange_Name_Headers="exchangeHeaders";


    //测试6、远程调用（RPC）模式-队列
    public final static String MQ_QUEUE_6_Rpc_Send="myQueue6Rpc_send";
    public final static String MQ_QUEUE_6_Rpc_Reply="myQueue6Rpc_reply";
    //测试消息通道-发布订阅模式-交换机类型-Headers-交换机名称
    public final static String Exchange_Name_Rpc="exchangeRpc";


    //测试7、发布确认模式-队列
    public final static String MQ_QUEUE_7_Publisher_Confirms="myQueue7";
    //测试消息通道-发布订阅模式-交换机名称
    public final static String Exchange_Name_PC="exchangePc";


    //支付通知、发布确认模式-队列
    public final static String MQ_QUEUE_8_Publisher_Confirms="myQueue8";
    //支付通知-发布订阅模式-交换机名称
    public final static String Exchange_Name_8_PC="exchangePc8";

    //尽可能确保消息成功消费并解决幂等消费问题-队列
    public final static String MQ_QUEUE_ENUSRE_QUEUE="myQueue9";

    //邮件发送-队列
    public final static String MQ_QUEUE_EMAIL="queueEmail";




}
