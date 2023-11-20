package com.example.mapper;

import com.example.domain.OrderRecords;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderRecordsMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(OrderRecords record);

    int insertSelective(OrderRecords record);

    OrderRecords selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(OrderRecords record);

    int updateByPrimaryKey(OrderRecords record);
}
