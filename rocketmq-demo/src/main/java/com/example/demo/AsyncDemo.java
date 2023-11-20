package com.example.demo;

import com.example.constant.MqConstant;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.junit.Test;

import java.util.List;


/**
 * @projectName: rocketmq
 * @package: com.example.demo
 * @className: AsyncDemo
 * @author: 丁海斌
 * @description: TODO
 * @date: 2023/11/14 20:49
 * @version: 1.0
 */
//异步消息生产和消费，生产一生产就被消费
public class AsyncDemo {

    //异步信息发送的生产者
    @Test
    public void AsyncProducer() throws Exception {
        DefaultMQProducer producer = new DefaultMQProducer("async-producer-group");
        producer.setNamesrvAddr(MqConstant.NAME_SRV_ADDR);
        producer.start();
        Message message = new Message("asynctopic","异步消息测试".getBytes());
        producer.send(message, new SendCallback() {
            @Override
            public void onSuccess(SendResult sendResult) {
                System.out.println("异步发送信息成功");
            }

            @Override
            public void onException(Throwable throwable) {
                System.out.println("异步发送信息失败"+throwable.getMessage());
            }
        });
        System.out.println("看看谁先执行，毕竟是异步的");
        System.in.read();
        producer.shutdown();
    }
    //异步信息的消费者
    @Test
    public void AsyncConsumer() throws Exception{
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("async-consumer-group");
        consumer.setNamesrvAddr(MqConstant.NAME_SRV_ADDR);
        consumer.subscribe("asynctopic", "*");
        consumer.setMessageListener(new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msg, ConsumeConcurrentlyContext context) {
                System.out.println("异步消费监听器");
                //成功消费异步消息
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });
        consumer.start();
        System.in.read();
    }
}
