package com.mapper;

import com.entity.Donate;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DonateMapper {
    Integer insert(Donate donate);

    List<Donate> queryPageList(@Param("begin") int begin, @Param("size") int size,@Param("userId") String userId);

    Long getCount(String userId);

    List<Donate> queryAllPageList(@Param("begin") int begin, @Param("size") int size);

    Long getAllCount();
}
