package com.example.listen;

import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

/**
 * @projectName: rocketmq
 * @package: com.example.listen
 * @className: SimpleListener
 * @author: 丁海斌
 * @description: TODO
 * @date: 2023/11/15 16:40
 * @version: 1.0
 */
/*@Component*/
@RocketMQMessageListener(topic = "testboottest",consumerGroup = "boot-test-consumer-group")
public class SimpleListener implements RocketMQListener<MessageExt> {

    @Override
    public void onMessage(MessageExt messageExt) {
        System.out.println(new String(messageExt.getBody()));
    }
}
