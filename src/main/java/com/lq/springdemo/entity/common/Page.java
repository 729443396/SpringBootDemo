package com.lq.springdemo.entity.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
public class Page implements Serializable {
    /**
     * 当前页
     */
    private Integer pageNo;
    /**
     * 每页多少条
     */
    private Integer pageSize;
    /**
     * 总页码
     */
    private Integer pageTotal;
    /**
     * 总条数
     */
    private Integer pageCount;

    /**
     * 是否开启分页
     */
    @JsonIgnore
    private boolean enable;
}
