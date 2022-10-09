package com.lq.springdemo.quartz.task.factory;

import com.lq.springdemo.quartz.model.TimingModel;
import com.lq.springdemo.quartz.task.quartzInterface.ITriggerFactory;
import org.quartz.Trigger;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TriggerManager {

    private final List<ITriggerFactory> triggerFactories;

    public TriggerManager(List<ITriggerFactory> triggerFactories) {
        this.triggerFactories = triggerFactories;
    }

    /**
     * 生成对应的触发器
     *
     * @param timingModel 触发器model
     * @return org.quartz.Trigger
     * @author YuanXiaohan
     * @date 2021/12/16 2:53 下午
     */
    public Trigger build(TimingModel timingModel) throws Exception {
        for (ITriggerFactory triggerFactory : triggerFactories) {
            if (triggerFactory.check(timingModel.getType())) {
                return triggerFactory.build(timingModel);
            }
        }
        return null;
    }

}
