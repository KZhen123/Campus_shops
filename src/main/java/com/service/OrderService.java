package com.service;

import com.entity.Order;
import com.entity.chart.OrderPie;

import java.util.List;

public interface OrderService {

    Integer createOrder(Order order);

    Integer deleteOrderById(int id);

    List<Order> getAllOrders();

    List<Order> getOrdersByPage(Integer page, Integer size, String buyerId, String sellerId);

    Integer getOrderCount(String buyerId, String sellerId);

    /**
     * 获取订单饼图数据
     * */
    List<OrderPie> getPie();

    List<Integer> selectBoyOrder();

    List<Integer> selectGirlOrder();
}

