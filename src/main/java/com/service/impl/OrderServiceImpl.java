package com.service.impl;

import com.entity.Commodity;
import com.entity.Order;
import com.entity.chart.OrderPie;
import com.mapper.CommodityMapper;
import com.mapper.OrderMapper;
import com.service.OrderService;
import com.util.OrderUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private CommodityMapper commodityMapper;

    @Override
    public Integer createOrder(Order order) {
        // 订单号码
        String orderNo = OrderUtil.getOrderNo();
        order.setOrderId(orderNo);
        return orderMapper.insert(order);
    }

    @Override
    public Integer deleteOrderById(int id) {
        return orderMapper.deleteByPrimaryKey(id);
    }

    @Override
    public List<Order> getAllOrders() {
        return orderMapper.selectAll();
    }

    @Override
    public List<Order> getOrdersByPage(Integer page, Integer size,String buyerId, String sellerId) {
        Integer begin = (page - 1) * size;
        List<Order> orders = orderMapper.queryPageList(begin, size, buyerId, sellerId);
        List<Commodity> commodities = commodityMapper.selectAll();
        // list转map
        Map<String, String> commodityMap = commodities.stream()
                .collect(Collectors.toMap(commodity -> commodity.getCommid(), commodity -> commodity.getCommdesc()));
        for(Order order:orders){
            String desc = commodityMap.get(order.getCommodityId());
            order.setCommodityDesc(desc);
        }
        return orders;
    }

    @Override
    public Integer getOrderCount(String buyerId, String sellerId) {
        return orderMapper.getCount(buyerId,sellerId).intValue();
    }

    @Override
    public List<OrderPie> getPie() {
        return orderMapper.getPie();
    }

    @Override
    public List<Integer> selectBoyOrder() {
        String gender = "男";
        List<Map<String, Object>> orderByGender = orderMapper.getOrderByGender(gender);
        List<Integer> data = new ArrayList<>(Collections.nCopies(12, 0));
        // 遍历orderByGender，将每个月份的订单总量填充到相应的下标位置
        for (Map<String, Object> monthData : orderByGender) {
            int monthNumber = (int) monthData.get("order_month");
            Long totalOrders = (Long) monthData.get("total_orders");

            // 下标从0开始，而月份从1开始，因此要减1
            data.set(monthNumber - 1, totalOrders.intValue());
        }
        return data;
    }

    @Override
    public List<Integer> selectGirlOrder() {
        String gender = "女";
        List<Map<String, Object>> orderByGender = orderMapper.getOrderByGender(gender);
        List<Integer> data = new ArrayList<>(Collections.nCopies(12, 0));
        // 遍历orderByGender，将每个月份的订单总量填充到相应的下标位置
        for (Map<String, Object> monthData : orderByGender) {
            int monthNumber = (int) monthData.get("order_month");
            Long totalOrders = (Long) monthData.get("total_orders");

            // 下标从0开始，而月份从1开始，因此要减1
            data.set(monthNumber - 1, totalOrders.intValue());
        }
        return data;
    }
}
