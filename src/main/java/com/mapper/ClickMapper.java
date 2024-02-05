package com.mapper;

import com.entity.Click;
import org.apache.ibatis.annotations.Param;

public interface ClickMapper {
    Click getClick(@Param("couserid") String couserid,@Param("category") Integer category);

    void insert(Click click);

    void updateByPrimaryKey(Click historyClick);
}
