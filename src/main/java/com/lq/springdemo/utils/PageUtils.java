package com.lq.springdemo.utils;

import com.lq.springdemo.entity.common.Page;

public class PageUtils {
    private static ThreadLocal<Page> pageThreadLocal = new ThreadLocal<>();
    /**
     * 设置分页对象
     * @param pageNo
     * @param pageSize
     */
    public static void setPage(Integer pageNo, Integer pageSize){
        pageThreadLocal.set(new Page().setPageNo(pageNo).setPageSize(pageSize));
    }
    /**
     * 获取分页对象
     * @return
     */
    public static Page getPage(){
        return pageThreadLocal.get();
    }

    /**
     * 清理分页信息
     */
    public static void clear(){
        pageThreadLocal.remove();
    }

}
