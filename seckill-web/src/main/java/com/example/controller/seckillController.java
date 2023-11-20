package com.example.controller;

import com.alibaba.fastjson2.JSON;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @projectName: rocketmq
 * @package: com.example.controller
 * @className: seckillController
 * @author: 丁海斌
 * @description: TODO
 * @date: 2023/11/18 12:25
 * @version: 1.0
 */
@RestController
public class seckillController {
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private RocketMQTemplate rocketMQTemplate;
    //压测时自动生成userId
    AtomicInteger ai = new AtomicInteger(0);
    /**
     * 1.一个用户针对一种商品只能抢购一次
     * 2.做库存的预扣减  拦截掉大量无效请求
     * 3.放入mq 异步化处理订单
     *
     * @return
     */
    @GetMapping("/doSeckill")
    public String doSeckill(Integer goodsId){
        //自动生成当前的userId
        int userId = ai.incrementAndGet();
        //通过用户id和商品id拼接形成unique的key，在redis中判断是否存在该key
        String uk = userId+"-"+goodsId;
        Boolean flag = redisTemplate.opsForValue().setIfAbsent("seckillUk:" + uk, "");
        //如果已存在该key就说明，当前用户已经参与过当前商品的抢购了
        if(!flag){
            return "您已经参与过该商品的抢购了，请参与其他商品的抢购";
        }
        //假设商品已经同步到redis中了，我们要对商品进行数量上的减少
        Long count = redisTemplate.opsForValue().decrement("goodsId:"+goodsId);
        if(count<0){
            return "当前商品已被抢购完";
        }
        //放入mq
        Map<String,Integer> map = new HashMap<>(4);
        map.put("goodsId", goodsId);
        map.put("userId",userId);
        rocketMQTemplate.asyncSend("seckillTopic", JSON.toJSONString(map), new SendCallback() {
            @Override
            public void onSuccess(SendResult sendResult) {
                System.out.println("抢购成功");
            }
            @Override
            public void onException(Throwable throwable) {
                System.out.println("用户"+userId+"抢购失败:"+goodsId);
            }
        });
        return "拼命抢购中";
    }

}
