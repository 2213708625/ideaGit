package com.example;

import com.alibaba.fastjson.JSON;
import com.example.domain.Order;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.apache.rocketmq.spring.support.RocketMQHeaders;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

//使用boot来操作生产者
//全部生产者的消费者基本都是一样的写法，除了发送顺序消息和带tag和key的不一样
@SpringBootTest
class BootProducerApplicationTests {

    @Autowired
    private RocketMQTemplate rocketMQTemplate;
    @Test
    void contextLoads() {
    }
    //发送同步消息
    @Test
    public void testsync(){
        SendResult sendResult = rocketMQTemplate.syncSend("bootSync", "我是一个同步的消息");
        System.out.println(sendResult.getSendStatus());
        System.out.println(sendResult.getMsgId());
    }
    //发送异步消息
    @Test
    public void testAsync(){
        rocketMQTemplate.asyncSend("bootAsync", "我是一个异步消息", new SendCallback() {
            @Override
            public void onSuccess(SendResult sendResult) {
                System.out.println("异步消息发送成功");
            }

            @Override
            public void onException(Throwable throwable) {
                System.out.println("异步消息发送失败");
            }
        });
    }
    //发送单项消息
    @Test
    public void testOneWay(){
        rocketMQTemplate.sendOneWay("bootOneWay", "单项消息");
    }
    //发送延迟消息
    @Test
    public void testMs(){
        //使用消息构建器构建message对象
        Message<String> msg = MessageBuilder.withPayload("我是一个延迟消息").build();
        //发送延迟消息，需要设置连接mq过期时间，超时时间等级
        rocketMQTemplate.syncSend("bootMs",msg,3000,3);
    }
    //发送顺序消息
    @Test
    public void testOrder(){
        List<Order> orderList = Arrays.asList(
                new Order("qwer", 111, 59D, new Date(), "下订单"),
                new Order("qwer", 111, 59D, new Date(), "物流"),
                new Order("qwer", 111, 59D, new Date(), "签收"),
                new Order("zxcv", 112, 89D, new Date(), "下订单"),
                new Order("zxcv", 112, 89D, new Date(), "物流"),
                new Order("zxcv", 112, 89D, new Date(), "拒收")
        );
        for (Order order : orderList) {
            SendResult sendResult = rocketMQTemplate.syncSendOrderly("bootOrder",JSON.toJSONString(order),order.getOrderSn());
        }
    }

    //tag发送信息
    @Test
    public void testTag(){
        rocketMQTemplate.syncSend("bootTag:tagA", "我是一个带tagA的消息");
    }
    //带key发送消息
    @Test
    public void testKey(){
        MessageBuilder<String> msg = MessageBuilder.withPayload("我是一个带key的消息").setHeader(RocketMQHeaders.KEYS,"dsadasd");
        rocketMQTemplate.syncSend("bootKey",msg);
    }
    //sql92模式发送消息,这个要在配置文件中进行设置一般不用
    @Test
    public void testSql92(){
        rocketMQTemplate.syncSend("bootTag:5", "我是一个sql92的消息");
    }
}
