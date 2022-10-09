package com.lq.springdemo.quartz.model;


import com.lq.springdemo.quartz.quartzEnum.TriggerType;
import com.lq.springdemo.quartz.task.quartzInterface.QuartzTaskJob;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * @author Xiaohan.Yuan
 * @version 1.0.0
 * @ClassName CronTimingModel.java
 * @Description cron触发器model
 * @createTime 2021年12月16日
 */
@Getter
@Setter
public class CronTimingModel extends TimingModel{
    /**
     * cron表达式
     * */
    private String cronExpression;

    public CronTimingModel(Class<? extends QuartzTaskJob> taskClass, String taskName, String groupName, String description, Map<String, Object> param, String cronExpression) {
        super(taskClass, taskName, groupName, description, TriggerType.CRON, param);
        this.cronExpression = cronExpression;
    }

    public CronTimingModel(Class<? extends QuartzTaskJob> taskClass, String taskName, String groupName, String description,String cronExpression) {
        super(taskClass, taskName, groupName, description, TriggerType.CRON);
        this.cronExpression = cronExpression;
    }
}
