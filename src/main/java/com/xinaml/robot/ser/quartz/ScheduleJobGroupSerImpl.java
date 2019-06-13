package com.xinaml.robot.ser.quartz;

import com.xinaml.robot.base.ser.ServiceImpl;
import com.xinaml.robot.common.custom.exception.SerException;
import com.xinaml.robot.common.utils.BeanUtil;
import com.xinaml.robot.dto.quartz.ScheduleJobGroupDTO;
import com.xinaml.robot.entity.quartz.ScheduleJob;
import com.xinaml.robot.entity.quartz.ScheduleJobGroup;
import com.xinaml.robot.to.quartz.ScheduleJobGroupTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: [liguiqin]
 * @Date:[2018-12-29 10:46]
 * @Description: [ ]
 * @Version: [3.0]
 * @Copy: [com.xinaml]
 */
@Service
public class ScheduleJobGroupSerImpl extends ServiceImpl<ScheduleJobGroup, ScheduleJobGroupDTO> implements ScheduleJobGroupSer {
    @Autowired
    private ScheduleJobSer scheduleJobSer;
    @Autowired
    private ScheduleSer scheduleSer;


    @Override
    public ScheduleJobGroup add(ScheduleJobGroupTO jobGroupTO) throws SerException {
        ScheduleJobGroup jobGroup = BeanUtil.copyProperties(jobGroupTO, ScheduleJobGroup.class);
        super.save(jobGroup);
        return jobGroup;
    }


    @Override
    public void edit(ScheduleJobGroupTO jobGroupTO) throws SerException {
        ScheduleJobGroup jobGroup = super.findById(jobGroupTO.getId());
        BeanUtil.copyProperties(jobGroupTO, jobGroup);
        if (!jobGroupTO.getEnable()) {
            List<ScheduleJob> jobs = scheduleJobSer.findByGroupId(jobGroupTO.getId());
            for (ScheduleJob scheduleJob : jobs) {
                scheduleJob.setEnable(false);
                scheduleSer.stop(scheduleJob);
            }
            scheduleJobSer.update(jobs);
        }

        super.update(jobGroup);
    }

    @Override
    public void delete(String id) throws SerException {
        List<ScheduleJob> jobs = scheduleJobSer.findByGroupId(id);
        for (ScheduleJob scheduleJob : jobs) {
            scheduleSer.stop(scheduleJob);
        }
        super.remove(id);
    }

    @Override
    public void enable(String id, boolean enable) throws SerException {
        ScheduleJobGroup jobGroup = super.findById(id);
        if (null != jobGroup) {
            jobGroup.setEnable(enable);
            if (!enable) { //如果停用,则停用改组所有任务
                List<ScheduleJob> scheduleJobs = scheduleJobSer.findByGroupId(id);
                for (ScheduleJob scheduleJob : scheduleJobs) {
                    scheduleSer.stop(scheduleJob);
                    scheduleJob.setEnable(false);
                }
                scheduleJobSer.update(scheduleJobs);
            }

        } else {
            throw new SerException("该任务调度组不存在");
        }
        super.update(jobGroup);
    }

}
