package com.service.impl;

import com.entity.Click;
import com.mapper.ClickMapper;
import com.service.ClickService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClickServiceImpl implements ClickService {

    @Autowired
    private ClickMapper clickMapper;

    /**
     * 1. 判断该记录是否存在
     * 2. 存在：num+1， 不存在：插入
     * */
    @Override
    public void updateNum(String couserid, Integer category) {
        Click historyClick = clickMapper.getClick(couserid,category);
        if(historyClick==null){
            Click click = new Click();
            click.setUserId(couserid);
            click.setCategoryId(category);
            click.setNum(1);
            clickMapper.insert(click);
        }else{
            historyClick.setNum(historyClick.getNum()+1);
            clickMapper.updateByPrimaryKey(historyClick);
        }
    }
}
