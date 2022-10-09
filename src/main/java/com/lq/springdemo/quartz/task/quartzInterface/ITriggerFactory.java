package com.lq.springdemo.quartz.task.quartzInterface;

import com.lq.springdemo.quartz.model.TimingModel;
import com.lq.springdemo.quartz.quartzEnum.TriggerType;
import org.quartz.Trigger;

public interface ITriggerFactory {

    /**
     * 判断是否为该类型的触发器
     *
     * @param triggerType 触发器类型
     * @return boolean 如果是该类型的触发器返回true 否则返回false
     * @author YuanXiaohan
     * @date 2021/12/16 2:33 下午
     */
    public boolean check(TriggerType triggerType);


    public Trigger build(TimingModel timingModel) throws Exception;
}
