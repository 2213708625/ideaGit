package com.example.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @projectName: rocketmq
 * @package: com.example.domain
 * @className: Order
 * @author: 丁海斌
 * @description: TODO
 * @date: 2023/11/15 18:37
 * @version: 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    /**
     * 订单id
     */
    private String  orderSn;

    /**
     * 订单编号
     */
    private Integer orderNumber;

    /**
     * 订单价格
     */
    private Double price;

    /**
     * 订单号创建时间
     */
    private Date createTime;

    /**
     * 订单描述
     */
    private String desc;

}
