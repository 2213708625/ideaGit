package com.example.demo;

import com.example.constant.MqConstant;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.junit.Test;

import java.util.List;

/**
 * @projectName: rocketmq
 * @package: com.example.demo
 * @className: OneWayDemo
 * @author: 丁海斌
 * @description: TODO
 * @date: 2023/11/14 21:06
 * @version: 1.0
 */
//发送单向数据的生产者消费者
public class OneWayDemo {

    //单向信息的生产者
    @Test
    public void  OneWayProducer() throws Exception{
        DefaultMQProducer producer = new DefaultMQProducer("oneWay-producer-group");
        producer.setNamesrvAddr(MqConstant.NAME_SRV_ADDR);
        producer.start();
        Message message = new Message("oneWaytopic","测试单向信息".getBytes());
        producer.sendOneway(message);
        System.out.println("生产一个单项信息");
        producer.shutdown();
    }
    //单向信息的消费者
    @Test
    public void OneWayConsumer() throws Exception{
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("onwWay-consumer-group");
        consumer.setNamesrvAddr(MqConstant.NAME_SRV_ADDR);
        consumer.subscribe("oneWaytopic","*");
        consumer.setMessageListener(new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list, ConsumeConcurrentlyContext consumeConcurrentlyContext) {
                //单向信息消费成功
                System.out.println("单向信息消费成功");
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });
        consumer.start();
        System.in.read();
    }

}
