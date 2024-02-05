package com.controller;

import com.entity.Order;
import com.entity.chart.OrderLine;
import com.entity.chart.OrderPie;
import com.service.CommodityService;
import com.service.OrderService;
import com.service.UserInfoService;
import com.util.StatusCode;
import com.vo.LayuiPageVo;
import com.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private CommodityService commodityService;

    @Autowired
    private UserInfoService userInfoService;

    /**
     * 1. 创建订单
     * 2. 设置商品为已售出
     * 3. 买卖双方增加积分
     * */
    @PostMapping(value="/create",produces = "application/json")
    @ResponseBody
    public ResultVo createOrder(@RequestBody Order order, HttpSession session) {
        String userId = (String) session.getAttribute("userid");

        if (StringUtils.isEmpty(userId)){
            return new ResultVo(false, StatusCode.ACCESSERROR,"请先登录");
        }

        order.setBuyerId(userId);
        Integer create = orderService.createOrder(order);

        // 修改商品状态
        commodityService.ChangeCommstatus(order.getCommodityId(), 4);
        // 积分
        String sellerId = order.getSellerId();
        Integer update1 = userInfoService.updatePoint(sellerId);
        Integer update2 = userInfoService.updatePoint(userId);
        ResultVo result;
        if (create == 1 && update1 ==1 && update2 == 1) {
            result = new ResultVo(true, StatusCode.OK, "购买成功！请至个人中心查看");
        } else {
            result = new ResultVo(false, StatusCode.SERVERERROR, "新增失败");
        }
        return result;
    }

    @PostMapping("/delete/{id}")
    public Integer deleteOrderById(@PathVariable int id) {
        return orderService.deleteOrderById(id);
    }

    @GetMapping("/all")
    public List<Order> getAllOrders() {
        return orderService.getAllOrders();
    }

//    @GetMapping("/page")
//    public List<Order> getOrdersByPage(@RequestParam Integer page, @RequestParam Integer size) {
//        return orderService.getOrdersByPage(page, size);
//    }
//
//    @GetMapping("/count")
//    public Long getOrderCount() {
//        return orderService.getOrderCount();
//    }

    @RequestMapping(value="/selectBuyOrder", produces = "application/json")
    @ResponseBody
    public LayuiPageVo selectBuyOrder(@RequestParam Integer page, @RequestParam Integer limit, HttpSession session) {
        String userid = (String) session.getAttribute("userid");

        List<Order> list = orderService.getOrdersByPage(page, limit, userid, null);
        int count = orderService.getOrderCount(userid, null);

        return new LayuiPageVo("", 0, count, list);
    }


    @RequestMapping(value="/selectSellerOrder", produces = "application/json")
    @ResponseBody
    public LayuiPageVo selectSellerOrder(@RequestParam Integer page, @RequestParam Integer limit, HttpSession session) {
        String userid = (String) session.getAttribute("userid");

        List<Order> list = orderService.getOrdersByPage(page, limit, null, userid);
        int count = orderService.getOrderCount( null,userid);

        return new LayuiPageVo("", 0, count, list);
    }

    @RequestMapping(value="/pie", produces = "application/json")
    @ResponseBody
    public List<OrderPie> pie() {
        List<OrderPie> pie = orderService.getPie();
        return pie;
    }

    @RequestMapping(value="/line", produces = "application/json")
    @ResponseBody
    public OrderLine line() {
        OrderLine orderLine = new OrderLine();
        List<Integer> boyOrder = orderService.selectBoyOrder();
        List<Integer> girlOrder = orderService.selectGirlOrder();
        orderLine.setBoyOrder(boyOrder);
        orderLine.setGirlOrder(girlOrder);
        return orderLine;
    }
}
