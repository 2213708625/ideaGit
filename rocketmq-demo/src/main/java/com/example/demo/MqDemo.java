package com.example.demo;

import com.example.constant.MqConstant;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.junit.Test;

import java.util.List;

/**
 * @projectName: rocketmq
 * @package: com.example.demo
 * @className: MqProducerDemo
 * @author: 丁海斌
 * @description: TODO
 * @date: 2023/11/14 16:37
 * @version: 1.0
 */
//测试普通生产消费
public class MqDemo {
    //生产者demo测试
    @Test
    public void simpleProdducer() throws Exception {
            //创建一个生产者（指定一个组名）
            DefaultMQProducer producer = new DefaultMQProducer("test-producer-group");
            //连接namesrv
            producer.setNamesrvAddr(MqConstant.NAME_SRV_ADDR);
            //启动
            producer.start();
            //创建一个信息
            Message message = new Message("testTopic", "我是一个信息".getBytes());
            //发送信息
            SendResult result = producer.send(message);
            System.out.println(result.getSendStatus());
            //关闭生产者
            producer.shutdown();
    }
    //消费者demo测试
    @Test
    public void simpleConsumer() throws Exception {
        //创建一个消费者,指定消费组
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("test-consumer-group");
        //连接namesrv
        consumer.setNamesrvAddr(MqConstant.NAME_SRV_ADDR);
        //订阅一个主题，*表示订阅这个中所有消息
        consumer.subscribe("testTopic", "*");
        //设置一个消息监听器
        consumer.setMessageListener(new MessageListenerConcurrently() {
            @Override//使用匿名内部类，这个就是消费的方法，做业务处理
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msg, ConsumeConcurrentlyContext context) {
                System.out.println("我是消费者");
                System.out.println(msg.get(0).toString());
                //消费消息正文
                System.out.println(new String(msg.get(0).getBody()));
                System.out.println("消费上下文"+context);
                //ConsumeConcurrentlyStatus.CONSUME_SUCCESS表示成功，消息会从队列中出去，也就是成功消费
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                //ConsumeConcurrentlyStatus.RECONSUME_LATER表示失败，消息会重新回到mq队列，过一会重新投递进来
                //给当前消费者，或者其他消费者消费
                /*return ConsumeConcurrentlyStatus.RECONSUME_LATER;*/
            }
        });
        //启动
        consumer.start();
        //挂起当前jvm，监听器是异步的，时当前消费者一直处于一个挂起的状态
        //当我们的消息队列中没有消息时，会阻塞等待消息，这时我们的生产者发送消息，我们的消费者就会立马消费
        System.in.read();


    }



}
