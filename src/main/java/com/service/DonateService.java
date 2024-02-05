package com.service;

import com.entity.Category;
import com.entity.Donate;

import java.util.List;

public interface DonateService {
    Integer insert(Donate donate);

    List<Donate> queryPageList(int page, int size, String userid);

    int getCount(String userid);

    List<Donate> queryPageList(int page, int size);

    int getCount();
}
