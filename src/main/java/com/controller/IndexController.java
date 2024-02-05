package com.controller;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Controller
public class IndexController {
    /**
     * 网站首页
     * */
    @GetMapping("/")
    public String index(){
        return "/index";
    }

    /**
     * 后台管理首页
     * */
    @GetMapping("/admin/index")
    public String adminindex(HttpSession session, HttpServletRequest request, HttpServletResponse response) throws IOException {
        String admin = (String) session.getAttribute("admin");
        /**拦截器：如果不是管理员，则进行重定向*/
        if (StringUtils.isEmpty(admin)){
            response.sendRedirect(request.getContextPath() + "/");//重定向
        }
        return "/admin/index";
    }

    /**
     * 用户登录注册
     * */
    @GetMapping("/login")
    public String login(){
        return "/user/logreg";
    }

    /**
     * 用户忘记密码
     * */
    @GetMapping("/forget")
    public String forget(){
        return "user/forget";
    }

    /**
     * 个人中心
     * */
    @GetMapping("/user/center")
    public String usercenter(HttpSession session, HttpServletRequest request, HttpServletResponse response) throws IOException {
        String userid = (String) session.getAttribute("userid");
        /**拦截器：如果不是用户角色登录，则进行重定向*/
        if (StringUtils.isEmpty(userid)){
            response.sendRedirect(request.getContextPath() + "/");//重定向
        }
        return "/user/user-center";
    }

    /**
     * 用户修改密码
     * */
    @RequiresPermissions("user:userinfo")
    @GetMapping("/user/pass")
    public String userinfo(){
        return "/user/updatepass";
    }

    /**
     * 用户商品列表
     * */
    @GetMapping("/user/product")
    public String userproduct(){
        return "/user/product/productlist";
    }

    /**
     * 通知消息
     * */
    @GetMapping("/user/message")
    public String commonmessage(){
        return "/user/message/message";
    }
    /**
     * 弹出式通知消息
     * */
    @GetMapping("/user/alertmessage")
    public String alertmessage(){
        return "/user/message/alertmessage";
    }
    /**
     * 跳转到产品清单界面
     * */
    @GetMapping("/product-listing")
    public String toproductlisting() {
        return "/common/product-listing";
    }


    /**用户个人中心默认展示图*/
    @GetMapping("/home/console")
    public String homeconsole(){
        return "/admin/home/console";
    }

    /**
     * 管理员首页默认展示图
     * */
    @GetMapping("/echars/console")
    public String echars(){
        return "/admin/echars/console";
    }

    /**
     * 用户收藏列表
     * */
    @GetMapping("/user/collect")
    public String usercollect(){
        return "/user/collect/collectlist";
    }

    /**
     * 用户售出记录
     * */
    @GetMapping("/user/sold")
    public String sold(){
        return "/user/order/soldrecord";
    }

    /**
     * 用户购买记录
     * */
    @GetMapping("/user/buy")
    public String buy(){
        return "/user/order/buyRecord";
    }

    /**
     * 用户捐赠记录
     * */
    @GetMapping("/user/donate")
    public String donate(){
        return "/user/donate/list";
    }

}
