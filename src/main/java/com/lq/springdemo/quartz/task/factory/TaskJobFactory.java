package com.lq.springdemo.quartz.task.factory;


import org.quartz.spi.TriggerFiredBundle;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.scheduling.quartz.AdaptableJobFactory;
import org.springframework.stereotype.Component;

/**
 * @author Xiaohan.Yuan
 * @version 1.0.0
 * @ClassName TaskJobFactory.java
 * @Description 将Quartz注入springboot
 * @createTime 2021年12月16日
 */

@Component
public class TaskJobFactory extends AdaptableJobFactory {
    private final AutowireCapableBeanFactory capableBeanFactory;

    public TaskJobFactory(AutowireCapableBeanFactory capableBeanFactory) {
        this.capableBeanFactory = capableBeanFactory;
    }

    @Override
    protected Object createJobInstance(TriggerFiredBundle bundle) throws Exception {
        //调用父类的方法
        Object jobInstance = super.createJobInstance(bundle);
        //进行注入
        capableBeanFactory.autowireBean(jobInstance);
        return jobInstance;
    }
}

