package com.lq.springdemo.quartz.task.factory;

import com.lq.springdemo.quartz.task.quartzInterface.QuartzTaskJob;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SimpleTrigger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class TestQuartz implements QuartzTaskJob {

    @Value("${driver-class-name}")
    private String logPath;

    @Value("${Configuration.status}")
    private String name;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        // 获取参数
        JobDataMap jobDataMap = context.getTrigger().getJobDataMap();
        // 获取任务名
        String name = context.getJobDetail().getJobBuilder().build().getKey().getName();
        // 获取任务分组
        String group = context.getJobDetail().getJobBuilder().build().getKey().getGroup();
        // 获取任务描述
        String description = context.getJobDetail().getDescription();
        if (context.getTrigger() instanceof SimpleTrigger){
            // 运行次数
            System.out.println(((SimpleTrigger)context.getTrigger()).getTimesTriggered());
        }
        log.info("----------------------" +
                        "\n任务组:{}\n任务名:{}\n任务描述:{}\n获取参数paramKey:{}\n" +
                        "----------------------"
                ,name,group,description,jobDataMap.getString("paramKey"));
        log.error(this.name+this.logPath);

        try {
//            QuartzJobManager.getInstance().jobdelete(this.getClass().getSimpleName(),"ah");//执行完此任务就删除自己
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
