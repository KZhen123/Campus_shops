package com.controller;

import com.entity.Category;
import com.entity.Donate;
import com.service.CommodityService;
import com.service.DonateService;
import com.util.StatusCode;
import com.vo.LayuiPageVo;
import com.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("donate")
public class DonateController {

    @Autowired
    private DonateService donateService;

    @Autowired
    private CommodityService commodityService;

    @RequestMapping(value = "selectPage", produces = "application/json")
    @ResponseBody
    public LayuiPageVo selectPage(@RequestParam int size, @RequestParam int page, HttpSession session) {
        String userid = (String) session.getAttribute("userid");
        List<Donate> list = donateService.queryPageList(page, size,userid);
        int count = donateService.getCount(userid);
        return new LayuiPageVo("", 200, count, list);
    }

    @RequestMapping(value = "selectAllPage", produces = "application/json")
    @ResponseBody
    public LayuiPageVo selectPage(@RequestParam int size, @RequestParam int page) {
        List<Donate> list = donateService.queryPageList(page, size);
        int count = donateService.getCount();
        return new LayuiPageVo("", 200, count, list);
    }

    @RequestMapping(value = "add", produces = "application/json")
    @ResponseBody
    public ResultVo insert(@RequestBody Donate donate, HttpSession session){
        String userid = (String) session.getAttribute("userid");
        donate.setUserId(userid);
        ResultVo result = new ResultVo();
        Integer res = donateService.insert(donate);
        commodityService.ChangeCommstatus(donate.getCommodityId(),5);
        if (res == 1) {
            result = new ResultVo(true, StatusCode.OK, "捐赠成功！");
        } else {
            result = new ResultVo(false, StatusCode.SERVERERROR, "新增失败");
        }
        return result;
    }
}
