package com.example.service;

import com.example.domain.Goods;

import java.util.List;

public interface GoodsService{


    int deleteByPrimaryKey(Integer id);

    int insert(Goods record);

    int insertSelective(Goods record);

    Goods selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Goods record);

    int updateByPrimaryKey(Goods record);
    void realSeckill(Integer userId,Integer goodsId);


}
