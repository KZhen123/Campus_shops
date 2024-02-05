package com.service;

import com.entity.Category;
import com.entity.Commodity;
import com.entity.UserInfo;
import com.mapper.CategoryMapper;
import com.mapper.CommodityMapper;
import com.mapper.UserInfoMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author hlt
 * @since 2019-12-21
 */
@Service
@Transactional
public class CommodityService {
    @Autowired
    private CommodityMapper commodityMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private UserInfoMapper userInfoMapper;

    /**插入商品*/
    @Async
    public Integer InsertCommodity(Commodity commodity){
        return commodityMapper.InsertCommodity(commodity);
    }
    /**
     * 查询商品详情
     * 查询卖家积分
     * */
    public Commodity LookCommodity(Commodity commodity){
        Commodity commodity1 = commodityMapper.LookCommodity(commodity);
        String userid = commodity1.getUserid();
        UserInfo userInfo = userInfoMapper.lookUserinfo(userid);
       commodity1.setPoint(userInfo.getPoint());
       return commodity1;

    }
    /**修改商品*/
    public Integer ChangeCommodity(Commodity commodity){
        return commodityMapper.ChangeCommodity(commodity);
    }
    /**修改商品状态*/
    public Integer ChangeCommstatus(String commid,Integer commstatus){
        return commodityMapper.ChangeCommstatus(commid,commstatus);
    }
    /**通过商品名分页模糊查询*/
    public List<Commodity> queryCommodityByName(Integer page,Integer count,String commname){
        return commodityMapper.queryCommodityByName(page,count,"%"+commname+"%");
    }
    /**模糊查询商品总数*/
    public Integer queryCommodityByNameCount(String commname){
        return commodityMapper.queryCommodityByNameCount("%"+commname+"%");
    }
    /**分页展示各类状态的商品信息*/
    public List<Commodity> queryAllCommodity(Integer page,Integer count,String userid,Integer commstatus){
        List<Commodity> commodities = commodityMapper.queryAllCommodity(page, count, userid, commstatus);
        List<Category> categories = categoryMapper.selectAll();
        // list转map
        Map<Integer, String> categoryMap = categories.stream().collect(Collectors.toMap(Category::getId, category -> category.getName()));
        for(Commodity commodity:commodities){
            Integer categoryId = commodity.getCategory();
            commodity.setCategoryName(categoryMap.get(categoryId));
        }
        return commodities;

    }
    /**查询商品各类状态的总数*/
    public Integer queryCommodityCount(String userid,Integer commstatus){
        return commodityMapper.queryCommodityCount(userid,commstatus);
    }

    /**产品清单分类分页展示商品-排序
     * -按类别点击率排序
     * */
    public List<Commodity> queryAllCommodityByCategory(Integer page,Integer count,Integer category,BigDecimal minmoney,BigDecimal maxmoney,Integer sortId,String commname,String userId){
        return commodityMapper.queryAllCommodityByCategorySorted(page,count,category,minmoney,maxmoney,sortId,commname,userId);
    }

    /**产品清单分类分页展示商品-排序
     * */
    public List<Commodity> queryAllCommodityByCategory(Integer page,Integer count,Integer category,BigDecimal minmoney,BigDecimal maxmoney,Integer sortId,String commname){
        return commodityMapper.queryAllCommodityByCategorySorted2(page,count,category,minmoney,maxmoney,sortId,commname);
    }
    /**查询产品清单分类分页展示商品的总数*/
    public Integer queryAllCommodityByCategoryCount(Integer category, BigDecimal minmoney, BigDecimal maxmoney,String commname){
        return commodityMapper.queryAllCommodityByCategoryCount(category,minmoney,maxmoney,commname);
    }
}
