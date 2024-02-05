package com.entity.chart;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 年订单折线图数据
 * */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderLine {

    // 男 年订单
    List<Integer> boyOrder;

    // 女 年订单
    List<Integer> girlOrder;
}
