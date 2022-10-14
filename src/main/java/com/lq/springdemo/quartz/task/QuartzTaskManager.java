package com.lq.springdemo.quartz.task;


import com.lq.springdemo.quartz.model.CronTimingModel;
import com.lq.springdemo.quartz.model.TimingModel;
import com.lq.springdemo.quartz.task.factory.TriggerManager;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * @author Xiaohan.Yuan
 * @version 1.0.0
 * @ClassName QuartzTaskManager.java
 * @Description
 * @createTime 2021年12月16日
 */
@Configuration
@Slf4j
public class QuartzTaskManager {

    private final Scheduler scheduler;

    private final Boolean initStatus;

    private final TriggerManager triggerManager;

    private static QuartzTaskManager taskManager;






    public QuartzTaskManager(Scheduler scheduler, TriggerManager triggerManager) {
        this.scheduler = scheduler;
        taskManager = this;
        boolean status = true;
        try {
            // 启动调度器
            scheduler.start();
        } catch (SchedulerException e) {
            log.error("定时器调度器启动失败，定时器不可用！", e);
            status = false;
        }
        initStatus = status;
        this.triggerManager = triggerManager;
    }

    public static QuartzTaskManager getInstance(){

        return taskManager;
    }


    /**
     * 添加定时任务
     *
     * @param timingModel 任务model
     * @author YuanXiaohan
     * @date 2021/12/16 3:09 下午
     */
    public void addTask(TimingModel timingModel) throws Exception {
        checkTimingInit();
        // 构建任务信息
        JobDetail jobDetail = JobBuilder.newJob(timingModel.getTaskClass().getDeclaredConstructor().newInstance().getClass())
                .withDescription(timingModel.getDescription())
                .withIdentity(timingModel.getTaskName(), timingModel.getGroupName())
                .build();

        // 构建触发器
        Trigger trigger = triggerManager.build(timingModel);
        // 将任务参数放入触发器中
        if (timingModel.getParam() != null && !timingModel.getParam().isEmpty()) {
            trigger.getJobDataMap().putAll(timingModel.getParam());
        }
        // 启动任务
        scheduler.scheduleJob(jobDetail, trigger);
    }

    /**
     * 更新任务，任务的标示（由taskName和groupName组成）不变，任务的触发器（触发频率）发生变化
     *
     * @param timingModel 任务model
     * @author YuanXiaohan
     * @date 2021/12/16 3:15 下午
     */
    public void updateTask(TimingModel timingModel) throws Exception {
        // 获取到任务
        TriggerKey triggerKey = TriggerKey.triggerKey(timingModel.getTaskName(), timingModel.getGroupName());

        // 构建触发器
        Trigger trigger = triggerManager.build(timingModel);
        // 将任务参数放入触发器中
        if (timingModel.getParam() != null && !timingModel.getParam().isEmpty()) {
            trigger.getJobDataMap().putAll(timingModel.getParam());
        }
        // 将新的触发器绑定到任务标示上重新执行
        scheduler.rescheduleJob(triggerKey, trigger);
    }

    /**
     * 更新任务参数
     *
     * @param taskName  任务名
     * @param groupName 任务组名
     * @param param     参数
     * @author YuanXiaohan
     * @date 2021/12/16 3:20 下午
     */
    public void updateTask(String taskName, String groupName, Map<String, Object> param) throws SchedulerException {
        // 获取到任务
        TriggerKey triggerKey = TriggerKey.triggerKey(taskName, groupName);
        Trigger trigger = scheduler.getTrigger(triggerKey);

        //修改参数
        trigger.getJobDataMap().putAll(param);

        // 将新的触发器绑定到任务标示上重新执行
        scheduler.rescheduleJob(triggerKey, trigger);
    }

    /**
     * 删除任务
     *
     * @param taskName  任务名
     * @param groupName 任务组
     * @author YuanXiaohan
     * @date 2021/12/16 3:23 下午
     */
    public void deleteTask(String taskName, String groupName) throws SchedulerException {
        // 暂停任务对应的触发器
        scheduler.pauseTrigger(TriggerKey.triggerKey(taskName, groupName));
        // 删除任务对应的触发器
        scheduler.unscheduleJob(TriggerKey.triggerKey(taskName, groupName));
        // 删除任务
        scheduler.deleteJob(JobKey.jobKey(taskName, groupName));
    }

    /**
     * 暂停任务
     *
     * @param taskName  添加任务时timingMode中的taskName
     * @param groupName 添加任务时timingMode中的groupName
     * @author YuanXiaohan
     * @date 2021/12/16 3:11 下午
     */
    public void pauseTask(String taskName, String groupName) throws SchedulerException {
        scheduler.pauseJob(JobKey.jobKey(taskName, groupName));
    }


    /**
     * 将暂停的任务恢复执行
     *
     * @param taskName  添加任务时timingMode中的taskName
     * @param groupName 添加任务时timingMode中的groupName
     * @author YuanXiaohan
     * @date 2021/12/16 3:13 下午
     */
    public void resumeTask(String taskName, String groupName) throws SchedulerException {
        scheduler.resumeJob(JobKey.jobKey(taskName, groupName));
    }

    /**
     * 启动所有任务
     *
     * @author YuanXiaohan
     * @date 2021/12/16 3:25 下午
     */
    public void startAllTasks() {
        try {
            scheduler.start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 关闭定时任务，回收所有的触发器资源
     *
     * @author YuanXiaohan
     * @date 2021/12/16 3:26 下午
     */
    public void shutdownAllTasks() {
        try {
            if (!scheduler.isShutdown()) {
                scheduler.shutdown();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * 获取所有的任务,暂时无法获取到任务执行类和任务描述
     *
     * @return java.util.List<org.demo.quartz.mode.TimingModel>
     * @author YuanXiaohan
     * @date 2021/12/16 3:37 下午
     */
    public List<TimingModel> getTaskList() throws SchedulerException {
        GroupMatcher<JobKey> matcher = GroupMatcher.anyJobGroup();
        Set<JobKey> jobKeys = scheduler.getJobKeys(matcher);
        List<TimingModel> taskList = new ArrayList<>();
        for (JobKey jobKey : jobKeys) {
            List<? extends Trigger> triggers = scheduler.getTriggersOfJob(jobKey);
            for (Trigger trigger : triggers) {
                TimingModel timingModel;
                if (trigger instanceof CronTrigger) {
                    timingModel = new CronTimingModel(null, jobKey.getName(), jobKey.getGroup(), null, ((CronTrigger) trigger).getCronExpression());
                    timingModel.setTaskStatus(scheduler.getTriggerState(trigger.getKey()).name());
                    taskList.add(timingModel);
                } else {
                    log.warn("name:{},group:{}的定时任务类型未知，请拓展QuartzTaskManager.getTaskList的任务类型解析", jobKey.getName(), jobKey.getGroup());
                }
            }
        }
        return taskList;
    }


    /**
     * 校验定时调度器是否初始化完成
     *
     * @author YuanXiaohan
     * @date 2021/12/16 2:28 下午
     */
    private void checkTimingInit() throws Exception {
        if (!initStatus) {
            throw new Exception("定时器未初始化，添加定时器失败!");
        }
    }


}

