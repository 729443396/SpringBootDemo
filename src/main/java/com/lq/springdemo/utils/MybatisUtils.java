package com.lq.springdemo.utils;

import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;

public class MybatisUtils {
    /**
     * 获取非代理对象
     * @param target
     * @param <T>
     * @return
     */
    public static <T> T getNoProxyTarget(Object target) {
        MetaObject invocationMetaObject = SystemMetaObject.forObject(target);
        while (invocationMetaObject.hasGetter("h")) {
            // 说明获得的是代理对象
            target = invocationMetaObject.getValue("h.target");
            invocationMetaObject = SystemMetaObject.forObject(target);
        }
        return (T) target;
    }
}
