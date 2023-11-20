package com.example.listen;

import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.annotation.SelectorType;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

/**
 * @projectName: rocketmq
 * @package: com.example.listen
 * @className: TagListener
 * @author: 丁海斌
 * @description: TODO
 * @date: 2023/11/15 19:22
 * @version: 1.0
 */
/*@Component*/
@RocketMQMessageListener(
        topic = "bootTag",
        consumerGroup = "boot-tag-consumer-group",
        selectorType = SelectorType.TAG,  //tag过滤模式
        selectorExpression = "tagA || tagB"
)
public class TagListener implements RocketMQListener<MessageExt>{
    @Override
    public void onMessage(MessageExt messageExt) {
        System.out.println(new String(messageExt.getBody()));
    }
}
