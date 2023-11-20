package com.example.service.impl;

import com.example.mapper.OrderRecordsMapper;
import com.example.service.GoodsService;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import com.example.domain.*;
import com.example.mapper.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class GoodsServiceImpl implements GoodsService {
    @Resource
    private OrderRecordsMapper orderRecordsMapper;

    @Resource
    private GoodsMapper goodsMapper;

    @Override
    public int deleteByPrimaryKey(Integer id) {
        return goodsMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(Goods record) {
        return goodsMapper.insert(record);
    }

    @Override
    public int insertSelective(Goods record) {
        return goodsMapper.insertSelective(record);
    }

    @Override
    public Goods selectByPrimaryKey(Integer id) {
        return goodsMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(Goods record) {
        return goodsMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(Goods record) {
        return goodsMapper.updateByPrimaryKey(record);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void realSeckill(Integer userId, Integer goodsId) {
        int i = goodsMapper.updateStock(goodsId);
        if (i > 0) {
            // 写订单表
            OrderRecords orderRecords = new OrderRecords();
            orderRecords.setGoodsId(goodsId);
            orderRecords.setUserId(userId);
            orderRecords.setCreateTime(new Date());
            // 时间戳生成订单号
            orderRecords.setOrderSn(String.valueOf(System.currentTimeMillis()));
            orderRecordsMapper.insert(orderRecords);
        }


    }

}
