package com.mapper;

import com.entity.Order;
import com.entity.chart.OrderPie;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface OrderMapper {

    Integer insert(Order order);

    Integer deleteByPrimaryKey(int id);

    List<Order> selectAll();

    /**
     * 查询分页列表
     * */
    List<Order> queryPageList(@Param("begin") Integer begin, @Param("size") Integer size, @Param("buyerId") String buyerId, @Param("sellerId")String sellerId);

    /**
     * 查找总数
     * */
    Long getCount(@Param("buyerId") String buyerId, @Param("sellerId")String sellerId);

    List<OrderPie> getPie();

    List<Map<String, Object>> getOrderByGender(@Param("gender") String gender);
}
