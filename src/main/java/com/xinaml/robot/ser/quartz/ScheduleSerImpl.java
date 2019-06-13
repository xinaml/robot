package com.xinaml.robot.ser.quartz;

import com.xinaml.robot.common.custom.exception.SerException;
import com.xinaml.robot.common.quartz.JobFactory;
import com.xinaml.robot.entity.quartz.ScheduleJob;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @Author: [liguiqin]
 * @Date:[2018-12-29 10:09]
 * @Description: [定时器实现逻辑 ]
 * @Version: [3.0]
 * @Copy: [com.xinaml]
 */
@Service
public class ScheduleSerImpl implements ScheduleSer {

    @Autowired
    private SchedulerFactoryBean schedulerFactoryBean;

    @Autowired
    private ScheduleJobSer scheduleJobSer;

    /**
     * 启动所用定时器
     *
     * @throws SerException
     */
    @PostConstruct
    private void init() throws SerException {
        List<ScheduleJob> scheduleJobs = scheduleJobSer.findScheduleJobs();
        for (ScheduleJob scheduleJob : scheduleJobs) {
            if(scheduleJob.getEnable()==Boolean.TRUE){
                this.start(scheduleJob);
            }
        }
    }

    public CronScheduleBuilder verifyTrigger(ScheduleJob scheduleJob) throws SerException {
        CronScheduleBuilder scheduleBuilder = null;
        try {
            scheduleBuilder = CronScheduleBuilder.cronSchedule(scheduleJob.getExpression());
        } catch (Exception e) {
            throw new SerException("表达式不正确,请重新输入.");
        }
        return scheduleBuilder;
    }

    public List<ScheduleJob> jobs() throws SerException {
        List<ScheduleJob> jobList = new ArrayList<>();
        try {
            Scheduler scheduler = schedulerFactoryBean.getScheduler();
            GroupMatcher<JobKey> matcher = GroupMatcher.anyJobGroup();
            Set<JobKey> jobKeys = scheduler.getJobKeys(matcher);
            for (JobKey jobKey : jobKeys) {
                List<? extends Trigger> triggers = scheduler.getTriggersOfJob(jobKey);
                for (Trigger trigger : triggers) {
                    ScheduleJob scheduleJob = new ScheduleJob();
                    scheduleJob.setId(jobKey.getName());
                    //scheduleJob.setScheduleJobGroup(new ScheduleJobGroup(jobKey.getGroup()));
                    scheduleJob.setDescription(String.valueOf(trigger.getKey()));
                    Trigger.TriggerState triggerState = scheduler.getTriggerState(trigger.getKey());
                    scheduleJob.setEnable(triggerState.name().equals("NORMAL"));
                    if (trigger instanceof CronTrigger) {
                        CronTrigger cronTrigger = (CronTrigger) trigger;
                        String cronExpression = cronTrigger.getCronExpression();
                        scheduleJob.setExpression(cronExpression);
                    }
                    jobList.add(scheduleJob);
                }
            }
        } catch (Exception e) {
            throw new SerException("获取失败");
        }
        return jobList;
    }

    public void start(ScheduleJob myJob) throws SerException {
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        TriggerKey triggerKey = TriggerKey.triggerKey(String.valueOf(myJob.getId()), String.valueOf(myJob.getScheduleJobGroup().getId()));//组合名称（定时器名称+分组名称）

        // 获取trigger，即在spring配置文件中定义的 bean id="myTrigger"
        CronTrigger trigger = null;
        try {
            trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
        } catch (SchedulerException e) {
            throw new SerException("表达式不正确,请重新输入.");
        }

        // 不存在，创建一个
        if (null == trigger) {
            JobDetail jobDetail = JobBuilder.newJob(JobFactory.class)
                    .withIdentity(String.valueOf(myJob.getId()), String.valueOf(myJob.getScheduleJobGroup().getId())).build();
            jobDetail.getJobDataMap().put("scheduleJob", myJob);

            // 表达式调度构建器
            CronScheduleBuilder scheduleBuilder = verifyTrigger(myJob);

            // 按新的cronExpression表达式构建一个新的trigger
            trigger = TriggerBuilder.newTrigger().withIdentity(String.valueOf(myJob.getId()), myJob.getScheduleJobGroup().getId().toString())
                    .withSchedule(scheduleBuilder).build();
            try {
                scheduler.scheduleJob(jobDetail, trigger);
            } catch (SchedulerException e) {
                throw new SerException(e.getMessage());
            }
        } else {
            // Trigger已存在，那么更新相应的定时设置
            // 表达式调度构建器
            CronScheduleBuilder scheduleBuilder = verifyTrigger(myJob);

            // 按新的cronExpression表达式重新构建trigger
            trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();

            // 按新的trigger重新设置job执行
            try {
                scheduler.rescheduleJob(triggerKey, trigger);
            } catch (SchedulerException e) {
                throw new SerException("trigger执行失败.");
            }
        }

    }

    public void stop(ScheduleJob scheduleJob) throws SerException {
        try {
            Scheduler scheduler = schedulerFactoryBean.getScheduler();
            JobKey jobKey = JobKey.jobKey(String.valueOf(scheduleJob.getId()), String.valueOf(scheduleJob.getScheduleJobGroup().getId()));
            if(scheduler.checkExists(jobKey)){
                scheduler.pauseJob(jobKey);
            }
        } catch (Exception e) {
            throw new SerException("定时器停止失败.");
        }
    }

    public void restart(ScheduleJob scheduledJob) throws SerException {
        try {
            Scheduler scheduler = schedulerFactoryBean.getScheduler();
            JobKey jobKey = JobKey.jobKey(String.valueOf(scheduledJob.getId()), String.valueOf(scheduledJob.getScheduleJobGroup().getId()));
            if(scheduler.checkExists(jobKey)){
                scheduler.resumeJob(jobKey);
            }else {
                start(scheduledJob);
            }
        } catch (Exception e) {
            throw new SerException("定时器重启失败.");
        }
    }

    public void del(ScheduleJob scheduledJob) throws SerException {
        try {
            Scheduler scheduler = schedulerFactoryBean.getScheduler();
            JobKey jobKey = JobKey.jobKey(String.valueOf(scheduledJob.getId()), String.valueOf(scheduledJob.getScheduleJobGroup().getId()));
            scheduler.deleteJob(jobKey);
        } catch (Exception e) {
            throw new SerException("定时器删除失败.");
        }

    }

}
