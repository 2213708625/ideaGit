package com.example.listener;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.example.service.GoodsService;
import org.apache.commons.collections.functors.WhileClosure;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.ConsumeMode;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @projectName: rocketmq
 * @package: com.example.listener
 * @className: seckillListener
 * @author: 丁海斌
 * @description: TODO
 * @date: 2023/11/18 21:02
 * @version: 1.0
 */

@RocketMQMessageListener(topic = "seckillTopic",
        consumerGroup = "seckill-consumer-group",
        consumeMode = ConsumeMode.CONCURRENTLY,
        consumeThreadNumber = 400
)
@Component
public class seckillListener implements RocketMQListener<MessageExt>{

    @Resource(name = "stringRedisTemplate")
    private StringRedisTemplate redisTemplate;
    @Resource
    private GoodsService goodsService;

    //做自旋锁的时间
    int time = 20000;
    @Override
    public void onMessage(MessageExt messageExt) {
        String s = new String(messageExt.getBody());
        JSONObject jsonObject = JSON.parseObject(s);
        Integer userId = jsonObject.getInteger("userId");
        Integer goodsId = jsonObject.getInteger("goodsId");
        int current = 0;
        while (current<time){
            Boolean flag = redisTemplate.opsForValue().setIfAbsent("lock:" + goodsId, "", 10, TimeUnit.SECONDS);
            if(flag){
                try {
                    goodsService.realSeckill(userId,goodsId);
                    return;
                }finally {
                    redisTemplate.delete("lock:" + goodsId);
                }
            }else {
                //循环自旋锁
                current+=200;
                try{
                    Thread.sleep(200);
                }catch(Exception e){
                    e.printStackTrace();
                }

            }
        }
    }
}
