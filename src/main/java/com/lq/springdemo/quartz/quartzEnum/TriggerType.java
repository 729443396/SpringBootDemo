package com.lq.springdemo.quartz.quartzEnum;


/**
 * @author Xiaohan.Yuan
 * @version 1.0.0
 * @ClassName TimingTriggerType.java
 * @Description 定时类型触发器类型
 * @createTime 2021年12月16日
 */
public enum TriggerType {
    CRON("标准CRON支持"),
    INTERVAL_MILLISECOND("固定间隔毫秒"),
    INTERVAL_SECOND("固定间隔秒"),
    INTERVAL_MINUTE("固定间隔分钟"),
    INTERVAL_HOUR("固定间隔小时"),
    WEEKDAYS("工作日，跳过节假日"),
    HOLIDAY("节假日")
    ;


    private String describe;

    TriggerType(String describe) {
        this.describe = describe;
    }
}

