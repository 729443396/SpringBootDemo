package com.lq.springdemo;


import com.lq.springdemo.quartz.model.IntervalTimingMode;
import com.lq.springdemo.quartz.quartzEnum.TriggerType;
import com.lq.springdemo.quartz.task.QuartzTaskManager;
import com.lq.springdemo.quartz.task.factory.TestQuartz;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@SpringBootApplication
@Slf4j
@Component
public class SpringBootMain implements CommandLineRunner {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(SpringBootMain.class,args);
    }

    /**
     * Callback used to run the bean.
     *
     * @param args incoming main method arguments
     * @throws Exception on error
     */
    @Override
    public void run(String... args) throws Exception {
//        System.out.println(this.name+this.logPath);
        //构建CRON定时
        //CronTimingModel cronTimingModel = new CronTimingModel(TestQuartz.class,
        // "测试名", "测试组", "测试描述", "*/1 * * * * ?");

        // 构建固定间隔定时
        IntervalTimingMode intervalTimingMode = new IntervalTimingMode(TestQuartz.class,
                "测试名", "测试组", "测试描述",
                TriggerType.INTERVAL_SECOND, 5L,null);
        HashMap<String, Object> param = new HashMap<>();
        param.put("paramKey","获取到参数了");
        intervalTimingMode.setParam(param);
        QuartzTaskManager.getInstance().addTask(intervalTimingMode);
    }
}
