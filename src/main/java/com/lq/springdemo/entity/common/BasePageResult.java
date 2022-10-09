package com.lq.springdemo.entity.common;

import lombok.Data;

@Data
public class BasePageResult<T>{

    private int rtnCode;

    private T result;

    private Page page;


}
