package com.example.listen;

import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.annotation.SelectorType;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

/**
 * @projectName: rocketmq
 * @package: com.example.listen
 * @className: Sql92Listener
 * @author: 丁海斌
 * @description: TODO
 * @date: 2023/11/15 19:30
 * @version: 1.0
 */
//sql92过滤模式的监听器
@Component
@RocketMQMessageListener(
        topic = "bootTag",
        consumerGroup = "sql-consumer-group",
        selectorType = SelectorType.SQL92,
        selectorExpression = "a>3"  //只有a大于3才能被监听到
)
public class Sql92Listener implements RocketMQListener<MessageExt> {
    @Override
    public void onMessage(MessageExt messageExt) {
        System.out.println(new String(messageExt.getBody()));
    }
}
