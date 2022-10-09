package com.lq.springdemo.quartz.task.factory;

import com.lq.springdemo.quartz.model.IntervalTimingMode;
import com.lq.springdemo.quartz.model.TimingModel;
import com.lq.springdemo.quartz.quartzEnum.TriggerType;
import com.lq.springdemo.quartz.task.quartzInterface.ITriggerFactory;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.stereotype.Component;

@Component
public class IntervalTrigger implements ITriggerFactory {
    /**
     * 判断是否为该类型的触发器
     *
     * @param triggerType 触发器类型
     * @return boolean 如果是该类型的触发器返回true 否则返回false
     * @author YuanXiaohan
     * @date 2021/12/16 2:33 下午
     */
    @Override
    public boolean check(TriggerType triggerType) {
        return triggerType == TriggerType.INTERVAL_MINUTE || triggerType == TriggerType.INTERVAL_SECOND || triggerType == TriggerType.INTERVAL_MILLISECOND||triggerType == TriggerType.INTERVAL_HOUR;
    }

    @Override
    public Trigger build(TimingModel timingModel) throws Exception {
        if (!(timingModel instanceof IntervalTimingMode)){
            throw new Exception("构建类型为INTERVAL定时必须传入IntervalTimingMode.class的实现类");
//            throw new TimingException("构建类型为INTERVAL定时必须传入IntervalTimingMode.class的实现类");
        }
        //创建触发器
        SimpleScheduleBuilder simpleScheduleBuilder = SimpleScheduleBuilder.simpleSchedule();
        Long interval = ((IntervalTimingMode) timingModel).getInterval();
        Integer repeatCount = ((IntervalTimingMode) timingModel).getRepeatCount();
        switch (timingModel.getType()){
            case INTERVAL_MINUTE:
                simpleScheduleBuilder.withIntervalInMinutes(Math.toIntExact(interval));
                break;
            case INTERVAL_HOUR:
                simpleScheduleBuilder.withIntervalInHours(Math.toIntExact(interval));
                break;
            case INTERVAL_SECOND:
                simpleScheduleBuilder.withIntervalInSeconds(Math.toIntExact(interval));
                break;
            case INTERVAL_MILLISECOND:
                simpleScheduleBuilder.withIntervalInMilliseconds(interval);
                break;
        }
        if (repeatCount==null){
            // 无限重复
            simpleScheduleBuilder.repeatForever();
        }else {
            simpleScheduleBuilder.withRepeatCount(repeatCount);
        }
        return TriggerBuilder.newTrigger().withIdentity(timingModel.getTaskName(), timingModel.getTaskName())
                .withSchedule(simpleScheduleBuilder).build();
    }
}
