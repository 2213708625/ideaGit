package com.example.listen;

import com.alibaba.fastjson.JSON;
import com.example.domain.Order;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.ConsumeMode;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

/**
 * @projectName: rocketmq
 * @package: com.example.listen
 * @className: OrderListener
 * @author: 丁海斌
 * @description: TODO
 * @date: 2023/11/15 18:48
 * @version: 1.0
 */
//顺序信息的消费者
/*@Component*/
@RocketMQMessageListener(
        topic ="bootOrder",
        consumerGroup ="boot-order-consumer",
        consumeMode = ConsumeMode.ORDERLY,//顺序消费模式，单线程
        maxReconsumeTimes = 5 //消费重试的次数
)
public class OrderListener implements RocketMQListener<MessageExt> {
    @Override
    public void onMessage(MessageExt messageExt) {
        Order order = JSON.parseObject(new String(messageExt.getBody()), Order.class);
        System.out.println(order);
    }
}
