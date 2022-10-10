package com.lq.springdemo.quartz.task.quartzInterface;


import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * @author Xiaohan.Yuan
 * @version 1.0.0
 * @ClassName QuartzTaskJob.java
 * @Description
 * @createTime 2021年12月16日
 */
public interface QuartzTaskJob extends Job {
    @Override
    void execute(JobExecutionContext context) throws JobExecutionException;
}


