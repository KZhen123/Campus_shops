package com.entity;

import jnr.ffi.annotations.In;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)//链式写法
public class Click implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * id
     * */
    private Integer id;

    private String userId;

    private Integer categoryId;

    private Integer num;
}