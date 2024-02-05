package com.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)//链式写法
public class Donate {
    private static final long serialVersionUID = 1L;

    private int id;
    private String commodityName;
    private String commodityId;

    private String commodityDesc;
    private String userId;
    private String receiveName;
    private String phone;
    private Date donateTime;
    private String address;
}
