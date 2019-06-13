package com.xinaml.robot.ser.quartz;

import com.xinaml.robot.common.custom.exception.SerException;
import com.xinaml.robot.entity.quartz.ScheduleJob;
import org.quartz.CronScheduleBuilder;

import java.util.List;

/**
 * @Author: [liguiqin]
 * @Date:[2018-12-29 10:08]
 * @Description: [ 定时器实现逻辑]
 * @Version: [3.0]
 * @Copy: [com.xinaml]
 */
public interface ScheduleSer {
    /**
     * 验证
     * @param scheduleJob
     * @return
     * @throws SerException
     */
    default CronScheduleBuilder verifyTrigger(ScheduleJob scheduleJob) throws SerException {
        return null;
    }

    /**
     * 正在运行的任务
     * @return
     * @throws SerException
     */
    default List<ScheduleJob> jobs() throws SerException {
        return null;
    }

    /**
     * 启动
     * @param scheduleJob
     * @throws SerException
     */
    default void start(ScheduleJob scheduleJob) throws SerException {


    }

    /**
     * 暂停
     * @param scheduleJob
     * @throws SerException
     */
    default void stop(ScheduleJob scheduleJob) throws SerException {

    }

    /**
     * 重启
     * @param scheduledJob
     * @throws SerException
     */
    default void restart(ScheduleJob scheduledJob) throws SerException {

    }

    /**
     * 删除
     * @param scheduledJob
     * @throws SerException
     */
    default void del(ScheduleJob scheduledJob) throws SerException {


    }
}
