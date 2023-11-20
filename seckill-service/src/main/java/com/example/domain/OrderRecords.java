package com.example.domain;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderRecords {
    private Integer id;

    private Integer userId;

    private String orderSn;

    private Integer goodsId;

    private Date createTime;
}
