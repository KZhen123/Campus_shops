package com.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 
 * </p>
 *
 * @author hlt
 * @since 2019-12-25
 */
@AllArgsConstructor//全参构造
@NoArgsConstructor//无参构造
@Data
@Accessors(chain = true)//链式写法
public class Commodity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 商品id
     */
    private String commid;
    /**
     * 商品名
     */
    private String commname;
    /**
     * 商品描述
     */
    private String commdesc;
    /**
     * 原价
     */
    private BigDecimal orimoney;
    /**
     * 售价
     */
    private BigDecimal thinkmoney;
    /**
     * 商品所在学校
     */
    private String school;
    /**
     * 发布时间
     */
    private Date createtime;
    /**
     * 修改时间
     */
    private Date updatetime;
    /**
     * 结束时间
     */
    private Date endtime;
    /**
     * 0违规 1正常 2删除  3待审核
     */
    private Integer commstatus;
    /**
     * 商品其他图集合
     * */
    private List<String> otherimg;

    /**
     * 浏览量
     */
    private Integer rednumber;
    /**
     * 商品类别
     */
    private Integer category;
    /**
     * 简介图
     */
    private String image;
    /**
     * 用户id
     */
    private String userid;

    /**
     * 类别名称
     * */
    private String categoryName;

    /**
     * 卖家积分
     * */
    private Integer point;

}
