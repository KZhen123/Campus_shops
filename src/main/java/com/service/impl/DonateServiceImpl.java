package com.service.impl;

import com.entity.Category;
import com.entity.Donate;
import com.mapper.DonateMapper;
import com.service.DonateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DonateServiceImpl implements DonateService {

    @Autowired
    private DonateMapper donateMapper;

    @Override
    public Integer insert(Donate donate) {
        return donateMapper.insert(donate);
    }

    @Override
    public List<Donate> queryPageList(int page, int size, String userId) {
        int begin = (page - 1) * size;
        List<Donate> donates = donateMapper.queryPageList(begin,size,userId);
        return donates;
    }

    @Override
    public int getCount(String userId) {
        return donateMapper.getCount(userId).intValue();
    }

    @Override
    public List<Donate> queryPageList(int page, int size) {
        int begin = (page - 1) * size;
        List<Donate> donates = donateMapper.queryAllPageList(begin,size);
        return donates;
    }

    @Override
    public int getCount() {
        return donateMapper.getAllCount().intValue();
    }
}
