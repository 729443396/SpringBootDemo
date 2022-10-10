package com.lq.springdemo.quartz.model;


import com.lq.springdemo.quartz.quartzEnum.TriggerType;
import com.lq.springdemo.quartz.task.quartzInterface.QuartzTaskJob;
import lombok.Getter;
import lombok.Setter;
import java.util.Map;

/**
 * @author Xiaohan.Yuan
 * @version 1.0.0
 * @ClassName IntervalTimingMode.java
 * @Description
 * @createTime 2021年12月16日
 */
@Getter
@Setter
public class IntervalTimingMode extends TimingModel {

    /**
     * 事件间隔，根据TriggerType确定单位，除了数值为毫秒，该数值必须在-2^32~2^31   (-2147483648 ~ 2147483647)
     * */
    private Long interval;

    /**
     * 重复次数，会执行该数值+1次,为空无限重复
     * */
    private Integer repeatCount;

    public IntervalTimingMode(Class<? extends QuartzTaskJob> taskClass, String taskName, String groupName, String description, TriggerType type, Map<String, Object> param, Long interval, Integer repeatCount) throws Exception {
        super(taskClass, taskName, groupName, description, type, param);
        if (type != TriggerType.INTERVAL_MILLISECOND){
            if (interval<(-2^32)||interval>(2^31)){
                throw new Exception("interval超出范围，除了类型为INTERVAL_MILLISECOND的数据间隔定时的interval范围必须在-2^32~2^31   (-2147483648 ~ 2147483647)");
            }
        }
        this.interval = interval;
        this.repeatCount = repeatCount;
    }

    public IntervalTimingMode(Class<? extends QuartzTaskJob> taskClass, String taskName, String groupName, String description, TriggerType type, Long interval,Integer repeatCount) throws Exception {
        super(taskClass, taskName, groupName, description, type);
        if (type != TriggerType.INTERVAL_MILLISECOND){
            if (interval<(-2^32)||interval>(2^31)){
                throw new Exception("interval超出范围，除了类型为INTERVAL_MILLISECOND的数据间隔定时的interval范围必须在-2^32~2^31   (-2147483648 ~ 2147483647)");
            }
        }
        this.interval = interval;
        this.repeatCount = repeatCount;
    }
}

