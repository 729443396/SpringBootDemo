package com.lq.springdemo.entity.common;

import lombok.Data;

@Data
public class ResultView<T> {


    public ResultView(T t){
        this.result = t;
        this.rtnCode =200;
    }

    private int rtnCode;
    private T result;
}
