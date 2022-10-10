package com.lq.springdemo.quartz.model;


import com.lq.springdemo.quartz.quartzEnum.TriggerType;
import com.lq.springdemo.quartz.task.quartzInterface.QuartzTaskJob;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * @author Xiaohan.Yuan
 * @version 1.0.0
 * @ClassName TimingModel.java
 * @Description 构建定时的model
 * @createTime 2021年12月16日
 */
@Getter
@Setter
public class TimingModel {
    /**
     * 该定时的任务处理器
     */
    private Class<? extends QuartzTaskJob> taskClass;

    /**
     * 任务名
     */
    private String taskName;
    /**
     * 任务组名
     * */
    private String groupName;

    /**
     * 任务描述
     * */
    private String description;


    /**
     * 任务类型
     */
    private TriggerType type;


    /**
     * 任务参数,可在具体的QuartzTaskJob实现中获取这些参数
     * */
    private Map<String, Object> param;

    /**
     * 任务状态
     * */
    private String taskStatus;

    public TimingModel(Class<? extends QuartzTaskJob> taskClass, String taskName, String groupName, String description, TriggerType type, Map<String, Object> param) {
        this.taskClass = taskClass;
        this.taskName = taskName;
        this.groupName = groupName;
        this.description = description;
        this.type = type;
        this.param = param;
    }

    public TimingModel(Class<? extends QuartzTaskJob> taskClass, String taskName, String groupName, String description, TriggerType type) {
        this.taskClass = taskClass;
        this.taskName = taskName;
        this.groupName = groupName;
        this.description = description;
        this.type = type;
    }
}


