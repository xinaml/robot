package com.xinaml.robot.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

/**
 * @Author: [liguiqin]
 * @Date:[2018-12-29 09:56]
 * @Description: [ ]
 * @Version: [3.0]
 * @Copy: [com.xinaml]
 */

@Configuration
public class QuartzConf {

    public SchedulerFactoryBean getSchedulerFactoryBean() {
        SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();
        schedulerFactoryBean.setOverwriteExistingJobs(true);
        schedulerFactoryBean.setStartupDelay(20);
        return schedulerFactoryBean;
    }
}
