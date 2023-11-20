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

import java.util.Date;
import java.util.List;

/**
 * @projectName: rocketmq
 * @package: com.example.demo
 * @className: MsDemo
 * @author: 丁海斌
 * @description: TODO
 * @date: 2023/11/14 21:24
 * @version: 1.0
 */
//测试延迟信息生产者与消费者
public class MsDemo {
    //延迟信息生产者
    @Test
    public void MsProducer() throws Exception{
        DefaultMQProducer producer = new DefaultMQProducer("Ms-producer-group");
        producer.setNamesrvAddr(MqConstant.NAME_SRV_ADDR);
        producer.start();
        // 给这个消息设定一个延迟等级
        // messageDelayLevel = "1s 5s 10s 30s 1m 2m 3m 4m 5m 6m 7m 8m 9m 10m 20m 30m 1h 2h
        Message message = new Message("Mstopic","测试延迟信息".getBytes());
        message.setDelayTimeLevel(3);//延迟10秒才能被消费者消费
        producer.send(message);
        System.out.println(new Date());
        producer.shutdown();
    }
    //延迟信息消费者
    @Test
    public void MsConsumer() throws Exception{
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("Ms-consumer-group");
        consumer.setNamesrvAddr(MqConstant.NAME_SRV_ADDR);
        consumer.subscribe("Mstopic","*");
        consumer.setMessageListener(new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list, ConsumeConcurrentlyContext consumeConcurrentlyContext) {
                //测试时间延迟
                System.out.println(new Date());
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });
        consumer.start();
        System.in.read();
    }
}
