package com.controller;


import com.alibaba.fastjson.JSONObject;
import com.entity.*;
import com.service.*;
import com.util.KeyUtil;
import com.util.StatusCode;
import com.vo.LayuiPageVo;
import com.vo.PageVo;
import com.vo.ResultVo;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *

 */
@Controller
public class CommodityController {
    @Autowired
    private CommodityService commodityService;
    @Autowired
    private CommimagesService commimagesService;
    @Autowired
    private LoginService loginService;
    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private CollectService collectService;
    @Autowired
    private NoticesService noticesService;

    @Autowired
    private ClickService clickService;

    /**
     * 跳转到发布商品
     */
    @GetMapping("/user/relgoods")
    public String torelgoods(HttpSession session){
        /*String userid = (String)session.getAttribute("userid");
        if(userid==null){
            return "redirect:/:";
        }*/
        return "/user/product/relgoods";
    }

    /**
     * 跳转到修改商品
     *  --不能修改已删除、已完成的商品
     *  1、查询商品详情
     *  2、查询商品得其他图
     */
    @GetMapping("/user/editgoods/{commid}")
    public String toeditgoods(@PathVariable("commid")String commid, HttpSession session, ModelMap modelMap){
        /*String userid = (String)session.getAttribute("userid");
        if(userid==null){
            return "redirect:/:";
        }*/
        Commodity commodity=commodityService.LookCommodity(new Commodity().setCommid(commid));
        if(commodity.getCommstatus().equals(2) || commodity.getCommstatus().equals(4)){
            return "/error/404";//商品已被删除或已完成交易
        }
        modelMap.put("goods",commodity);
        modelMap.put("otherimg",commimagesService.LookGoodImages(commid));
        return "/user/product/changegoods";
    }

    /**
     * 修改商品
     * 1、修改商品信息
     * 2、修改商品的其他图的状态
     * 3、插入商品的其他图
     */
    @PostMapping("/changegoods/rel")
    @ResponseBody
    public String changegoods(@RequestBody Commodity commodity, HttpSession session){
        String userid = (String) session.getAttribute("userid");
        commodity.setUpdatetime(new Date()).setCommstatus(3);
        commodityService.ChangeCommodity(commodity);
        commimagesService.DelGoodImages(commodity.getCommid());
        List<Commimages> commimagesList=new ArrayList<>();
        for (String list:commodity.getOtherimg()) {
            commimagesList.add(new Commimages().setId(KeyUtil.genUniqueKey()).setCommid(commodity.getCommid()).setImage(list));
        }
        commimagesService.InsertGoodImages(commimagesList);
        /**发出待审核系统通知*/
        Notices notices = new Notices().setId(KeyUtil.genUniqueKey()).setUserid(userid).setTpname("商品审核")
                .setWhys("您的商品 <a href=/product-detail/"+commodity.getCommid()+" style=\"color:#08bf91\" target=\"_blank\" >"+commodity.getCommname()+"</a> 进入待审核队列，请您耐心等待。");
        noticesService.insertNotices(notices);
        return "0";
    }

    /**
     * 发布商品
     * 1、插入商品信息
     * 2、插入商品其他图
     */
    @PostMapping("/relgoods/rel")
    @ResponseBody
    public String relgoods(@RequestBody Commodity commodity, HttpSession session){
        String userid = (String) session.getAttribute("userid");
        UserInfo userInfo = userInfoService.LookUserinfo(userid);
        String commid = KeyUtil.genUniqueKey();
        commodity.setCommid(commid).setUserid(userid).setSchool(userInfo.getSchool());//商品id
        commodityService.InsertCommodity(commodity);
        List<Commimages> commimagesList=new ArrayList<>();
        for (String list:commodity.getOtherimg()) {
            commimagesList.add(new Commimages().setId(KeyUtil.genUniqueKey()).setCommid(commid).setImage(list));
        }
        commimagesService.InsertGoodImages(commimagesList);
        /**发出待审核系统通知*/
        Notices notices = new Notices().setId(KeyUtil.genUniqueKey()).setUserid(userid).setTpname("商品审核")
                .setWhys("您的商品 <a href=/product-detail/"+commid+" style=\"color:#08bf91\" target=\"_blank\" >"+commodity.getCommname()+"</a> 进入待审核队列，请您耐心等待。");
        noticesService.insertNotices(notices);
        return "0";
    }

    /**
     * 上传主图
     */
    @PostMapping("/relgoods/pic")
    @ResponseBody
    public JSONObject relgoodsPic(@RequestParam(value = "file", required = false) MultipartFile file) throws IOException {
        JSONObject res = new JSONObject();
        JSONObject resUrl = new JSONObject();
        String filename = UUID.randomUUID().toString().replaceAll("-", "");
        String ext = FilenameUtils.getExtension(file.getOriginalFilename());
        String filenames = filename + "." + ext;
        String pathname = "D:\\campusshops\\file\\" + filenames;
        file.transferTo(new File(pathname));
        resUrl.put("src", "/pic/"+filenames);
        res.put("msg", "");
        res.put("code", 0);
        res.put("data", resUrl);
        return res;
    }

    /**
     * 上传其他图片
     */
    @PostMapping(value="/relgoods/images")
    @ResponseBody
    public JSONObject relgoodsimages(@RequestParam(value = "file", required = false) MultipartFile[] file) throws IOException {
        JSONObject res = new JSONObject();
        JSONObject resUrl = new JSONObject();
        List<String> imageurls=new ArrayList<>();
        for (MultipartFile files:file){
            String filename = UUID.randomUUID().toString().replaceAll("-", "");
            String ext = FilenameUtils.getExtension(files.getOriginalFilename());
            String filenames = filename + "." + ext;
            String pathname = "D:\\campusshops\\file\\" + filenames;
            files.transferTo(new File(pathname));
            imageurls.add("/pic/"+filenames);
            res.put("msg", "");
            res.put("code", 0);
        }
        resUrl.put("src", imageurls);
        res.put("data", resUrl);
        return res;
    }

    /**
     * 商品详情
     * 根据商品id（commid）查询商品信息、用户昵称及头像
     * 用户可以查看正常的商品
     * 商品发布者和管理员可以查看
     * */
    @GetMapping("/product-detail/{commid}")
    public String product_detail(@PathVariable("commid") String commid, ModelMap modelMap,HttpSession session){
        String couserid = (String) session.getAttribute("userid");

        Commodity commodity = commodityService.LookCommodity(new Commodity().setCommid(commid).setCommstatus(1));
        int i = 0;
        if (commodity.getCommstatus().equals(1)){//如果商品正常
            i=1;
        }else if (!StringUtils.isEmpty(couserid)){//如果用户已登录
            Login login = loginService.userLogin(new Login().setUserid(couserid));
            /**商品为违规状态时：本人和管理员可查看*/
            if (commodity.getCommstatus().equals(0) && (commodity.getUserid().equals(couserid) || (login.getRoleid().equals(2)))){
                i=1;
                /**商品为待审核状态时：本人和管理员可查看*/
            }else if (commodity.getCommstatus().equals(3) && (commodity.getUserid().equals(couserid) || (login.getRoleid().equals(2)))){
                i=1;
                /**商品为已售出状态时：本人和管理员可查看*/
            }else if (commodity.getCommstatus().equals(4) && (commodity.getUserid().equals(couserid) || (login.getRoleid().equals(2)))){
                i=1;
            }
        }
        if(i==1){
            commodity.setOtherimg(commimagesService.LookGoodImages(commid));
            /**商品浏览量+1*/
            commodityService.ChangeCommodity(new Commodity().setCommid(commid).setRednumber(1));
            clickService.updateNum(couserid,commodity.getCategory());
            modelMap.put("userinfo",userInfoService.queryPartInfo(commodity.getUserid()));
            modelMap.put("gddetail",commodity);
            return "/common/product-detail";
        }else{
            return "/error/404";
        }
    }

    /**
     * 搜索商品分页数据
     * 前端传入搜索的商品名（commname）
     * */
    @GetMapping("/product/search/number/{commname}")
    @ResponseBody
    public PageVo searchCommodityNumber(@PathVariable("commname") String commname){
        Integer dataNumber = commodityService.queryCommodityByNameCount(commname);
        return new PageVo(StatusCode.OK,"查询成功",dataNumber);
    }

    /**
     * 搜索商品
     * 前端传入当前页数（nowPaging）、搜索的商品名（commname）
     * */
    @GetMapping("/product/search/{nowPaging}/{commname}")
    @ResponseBody
    public ResultVo searchCommodity(@PathVariable("nowPaging") Integer page, @PathVariable("commname") String commname){
        List<Commodity> commodityList = commodityService.queryCommodityByName((page - 1) * 20, 20, commname);

        if(!StringUtils.isEmpty(commodityList)){//如果有对应商品
            for (Commodity commodity : commodityList) {
                /**查询商品对应的其它图片*/
                List<String> imagesList = commimagesService.LookGoodImages(commodity.getCommid());
                commodity.setOtherimg(imagesList);
            }
            return new ResultVo(true,StatusCode.OK,"查询成功",commodityList);
        }else{
            return new ResultVo(true,StatusCode.ERROR,"没有相关商品");
        }
    }

    /**
     * 产品清单分页数据
     * 前端传入商品类别（category）、区域（area）
     * 最低价（minmoney）、最高价（maxmoney）
     * */
    @GetMapping("/list-number/{category}/{minmoney}/{maxmoney}/{name}")
    @ResponseBody
    public PageVo productListNumber(@PathVariable("category") Integer category,
                                    @PathVariable("minmoney") BigDecimal minmoney, @PathVariable("maxmoney") BigDecimal maxmoney,
                                    @PathVariable("name") String name,
                                    HttpSession session) {
        Integer dataNumber = commodityService.queryAllCommodityByCategoryCount(category, minmoney, maxmoney,name);
        return new PageVo(StatusCode.OK,"查询成功",dataNumber);
    }

    /**
     * 产品清单界面-按类别点击率排序
     * 前端传入商品类别（category）、当前页码（nowPaging）、name
     * 最低价（minmoney）、最高价（maxmoney）、价格升序降序（price：0.不排序 1.升序 2.降序）、点击率升序降序（3：升序 4：降序）
     * 后端根据session查出个人本校信息（school）
     * */
    @GetMapping("/product-listing/{category}/{nowPaging}/{minmoney}/{maxmoney}/{sortId}/{name}")
    @ResponseBody
    public ResultVo productlisting(@PathVariable("category") Integer category, @PathVariable("nowPaging") Integer page,
                                  @PathVariable("minmoney") BigDecimal minmoney, @PathVariable("maxmoney") BigDecimal maxmoney,
                                 @PathVariable("sortId") Integer sortId,@PathVariable("name") String name, HttpSession session) {
        // 已登录
        String userId = (String) session.getAttribute("userid");
        List<Commodity> commodityList;
        if(userId!=null){
            commodityList = commodityService.queryAllCommodityByCategory((page - 1) * 16, 16, category, minmoney, maxmoney,sortId,name,userId);
        }else{
            commodityList = commodityService.queryAllCommodityByCategory((page - 1) * 16, 16, category, minmoney, maxmoney,sortId,name);
        }
        for (Commodity commodity : commodityList) {
            /**查询商品对应的其它图片*/
            List<String> imagesList = commimagesService.LookGoodImages(commodity.getCommid());
            commodity.setOtherimg(imagesList);
        }
        return new ResultVo(true,StatusCode.OK,"查询成功",commodityList);
    }

    /**
     * 分页展示个人各类商品信息
     *前端传入页码、分页数量
     *前端传入商品信息状态码（commstatus）-->全部:100，已审核:1，待审核:3，违规:0，已完成:4
     */
    @GetMapping("/user/commodity/{commstatus}")
    @ResponseBody
    public LayuiPageVo userCommodity(@PathVariable("commstatus") Integer commstatus, int limit, int page, HttpSession session) {
        String userid = (String) session.getAttribute("userid");
        //如果未登录，给一个假id
        if(StringUtils.isEmpty(userid)){
            userid = "123456";
        }
        List<Commodity> commodityList=null;
        Integer dataNumber;
        if(commstatus==100){
            commodityList = commodityService.queryAllCommodity((page - 1) * limit, limit, userid,null);
            dataNumber = commodityService.queryCommodityCount(userid,null);
        }else{
            commodityList = commodityService.queryAllCommodity((page - 1) * limit, limit, userid,commstatus);
            dataNumber = commodityService.queryCommodityCount(userid,commstatus);
        }
        return new LayuiPageVo("",0,dataNumber,commodityList);
    }

    /**
     * 个人对商品的操作
     * 前端传入商品id（commid）
     * 前端传入操作的商品状态（commstatus）-->删除:2  已完成:4
     * */
    @ResponseBody
    @GetMapping("/user/changecommstatus/{commid}/{commstatus}")
    public ResultVo ChangeCommstatus(@PathVariable("commid") String commid, @PathVariable("commstatus") Integer commstatus, HttpSession session) {
        Integer i = commodityService.ChangeCommstatus(commid, commstatus);
        if (i == 1){
            return new ResultVo(true,StatusCode.OK,"操作成功");
        }
        return new ResultVo(false,StatusCode.ERROR,"操作失败");
    }
}

