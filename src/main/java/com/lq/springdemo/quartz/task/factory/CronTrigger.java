package com.lq.springdemo.quartz.task.factory;

import com.itextpdf.text.ExceptionConverter;
import com.lq.springdemo.quartz.model.CronTimingModel;
import com.lq.springdemo.quartz.model.TimingModel;
import com.lq.springdemo.quartz.quartzEnum.TriggerType;
import com.lq.springdemo.quartz.task.quartzInterface.ITriggerFactory;
import org.quartz.CronScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.stereotype.Component;

@Component
public class CronTrigger implements ITriggerFactory {
    @Override
    public boolean check(TriggerType triggerType) {
        return triggerType== TriggerType.CRON;
    }

    @Override
    public Trigger build(TimingModel timingModel) throws Exception {
        if (!(timingModel instanceof CronTimingModel)){
            throw  new Exception("构建类型为CRON定时必须传入CronTimingModel.class的实现类");
            //throw new TimingException("构建类型为CRON定时必须传入CronTimingModel.class的实现类");
        }
        //按新的cronExpression表达式构建一个新的trigger
        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(((CronTimingModel) timingModel).getCronExpression());
        return TriggerBuilder.newTrigger().withIdentity(timingModel.getTaskName(), timingModel.getTaskName())
                .withSchedule(scheduleBuilder).build();
    }
}
