package com.xinaml.robot.common.quartz;

import com.xinaml.robot.entity.quartz.ScheduleJob;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

/**
 * @Author: [liguiqin]
 * @Date:[2018-12-29 09:59]
 * @Description: [ ]
 * @Version: [3.0]
 * @Copy: [com.changbei]
 */
@DisallowConcurrentExecution
public class JobFactory implements org.quartz.Job {
    private static final Logger CONSOLE = LoggerFactory.getLogger(JobFactory.class);

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        ScheduleJob scheduledJob = (ScheduleJob) context.getMergedJobDataMap().get("scheduleJob");
        CONSOLE.info("执行任务：" + scheduledJob.getName());
        try {
            invoke(scheduledJob);
        } catch (Exception e1) {
            e1.printStackTrace();
        }

    }

    private static void invoke(ScheduleJob scheduleJob) throws Exception {
        Class clazz = Class.forName(scheduleJob.getClazz());
        Object obj = clazz.newInstance();
        Method method = clazz.getDeclaredMethod(scheduleJob.getMethod());
        method.invoke(obj);
    }


}
