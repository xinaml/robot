package com.xinaml.robot.ser.quartz;

import com.xinaml.robot.base.ser.Ser;
import com.xinaml.robot.common.custom.exception.SerException;
import com.xinaml.robot.dto.quartz.ScheduleJobDTO;
import com.xinaml.robot.entity.quartz.ScheduleJob;
import com.xinaml.robot.to.quartz.ScheduleJobTO;

import java.util.List;

/**
 * @Author: [liguiqin]
 * @Date:[2018-12-29 10:10]
 * @Description: [ ]
 * @Version: [3.0]
 * @Copy: [com.xinaml]
 */
public interface ScheduleJobSer  extends Ser<ScheduleJob, ScheduleJobDTO> {
    /**
     * 添加任务调度
     *
     * @param scheduleJobTO
     * @return
     */
    default ScheduleJob add(ScheduleJobTO scheduleJobTO) throws SerException {
        return null;
    }

    /**
     * 编辑任务调度
     *
     * @param scheduleJobTO
     */
    default void edit(ScheduleJobTO scheduleJobTO) throws SerException {

    }

    /**
     * 删除任务调度
     *
     * @param id
     */
    default void delete(String id) throws SerException {

    }

    /**
     * 启用关闭任务调度
     *
     * @param enable
     */
    default void enable(String id, boolean enable) throws SerException {

    }

    /**
     * 获取所有的调度任务
     *
     * @return
     * @throws SerException
     */
    default List<ScheduleJob> findScheduleJobs() throws SerException {
        return null;
    }

    /**
     * 通过组id获取定制任务
     *
     * @return
     * @throws SerException
     */
    default List<ScheduleJob> findByGroupId(String id) throws SerException {
        return null;
    }
}
