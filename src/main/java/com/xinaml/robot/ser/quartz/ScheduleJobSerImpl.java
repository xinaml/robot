package com.xinaml.robot.ser.quartz;

import com.xinaml.robot.base.dto.RT;
import com.xinaml.robot.base.ser.ServiceImpl;
import com.xinaml.robot.common.custom.exception.SerException;
import com.xinaml.robot.common.utils.BeanUtil;
import com.xinaml.robot.common.utils.UserUtil;
import com.xinaml.robot.dto.quartz.ScheduleJobDTO;
import com.xinaml.robot.dto.quartz.ScheduleJobGroupDTO;
import com.xinaml.robot.entity.quartz.ScheduleJob;
import com.xinaml.robot.to.quartz.ScheduleJobTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @Author: [liguiqin]
 * @Date:[2018-12-29 10:32]
 * @Description: [ ]
 * @Version: [3.0]
 * @Copy: [com.xinaml]
 */
@Service
public class ScheduleJobSerImpl extends ServiceImpl<ScheduleJob, ScheduleJobDTO> implements ScheduleJobSer {
    @Autowired
    private ScheduleSer scheduleSer;
    @Autowired
    private ScheduleJobGroupSer scheduleJobGroupSer;


    @Override
    public ScheduleJob add(ScheduleJobTO scheduleJobTO) throws SerException {
        ScheduleJob scheduleJob = BeanUtil.copyProperties(scheduleJobTO, ScheduleJob.class);
        scheduleJob.setUser(UserUtil.getUser());
        scheduleJob.setCreateDate(LocalDateTime.now());
        scheduleSer.verifyTrigger(scheduleJob);//验证执行方法是否正确
        this.verifySchedule(scheduleJob);
        ScheduleJobGroupDTO dto = new ScheduleJobGroupDTO();
        dto.addRT(RT.eq("id", scheduleJobTO.getScheduleJobGroupId()));
        scheduleJob.setScheduleJobGroup(scheduleJobGroupSer.findOne(dto));
        super.save(scheduleJob);
        scheduleSer.start(scheduleJob);
        return scheduleJob;
    }


    @Override
    public void edit(ScheduleJobTO scheduleJobTO) throws SerException {
        ScheduleJob scheduleJob = super.findById(scheduleJobTO.getId());
        BeanUtil.copyProperties(scheduleJobTO, scheduleJob);
        this.verifySchedule(scheduleJob);
        scheduleSer.verifyTrigger(scheduleJob);//验证执行方法是否正确
        super.update(scheduleJob);
        if (scheduleJob.getEnable()) {
            scheduleSer.restart(scheduleJob);
        } else {
            scheduleSer.stop(scheduleJob);
        }


    }

    @Override
    public void delete(String id) throws SerException {
        ScheduleJobDTO dto = new ScheduleJobDTO();
        dto.addRT(RT.eq("id", id));
        ScheduleJob scheduleJob = super.findOne(dto);
        scheduleSer.del(scheduleJob);
        super.remove(id);
    }

    @Override
    public void enable(String id, boolean enable) throws SerException {
        ScheduleJob scheduleJob = super.findById(id);
        if (null != scheduleJob) {
            if (enable) {
                scheduleSer.restart(scheduleJob);
            } else {
                scheduleSer.stop(scheduleJob);
            }
            scheduleJob.setEnable(enable);
            super.update(scheduleJob);
        } else {
            throw new SerException("该任务调度不存在");
        }
    }

    @Override
    public List<ScheduleJob> findScheduleJobs() throws SerException {
        return findAll();
    }

    @Override
    public List<ScheduleJob> findByGroupId(String id) throws SerException {
        ScheduleJobDTO dto = new ScheduleJobDTO();
        dto.addRT(RT.eq("scheduleJobGroup.id", id));
        return findByRTS(dto);
    }

    private void verifySchedule(ScheduleJob scheduleJob) throws SerException {
        Class<?> clazz = classExists(scheduleJob.getClazz());
        if (clazz != null) {
            Method method = methodExists(clazz, scheduleJob.getMethod());
            if (null != method) {
                if (!parameterTypesExists(method)) {
                    throw new SerException("执行方法中不能存有任何参数");
                }
            } else {
                throw new SerException("执行方法不存在此调用类中");
            }
        } else {
            throw new SerException("调用类不存在此系统中");
        }
    }


    private Class<?> classExists(String className) {
        try {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            if (null == classLoader) classLoader = ScheduleJobSerImpl.class.getClassLoader();
            return classLoader.loadClass(className);
        } catch (Exception e) {
            return null;
        }
    }

    private Method methodExists(Class<?> clazz, String methodName) {
        try {
            Method[] methods = clazz.getDeclaredMethods();
            for (Method method : methods) {
                if (method.getName().equals(methodName)) {
                    return method;
                }
            }
            return null;
        } catch (SecurityException e) {
            return null;
        }
    }

    public boolean parameterTypesExists(Method method) {
        try {
            Class<?>[] cc = method.getParameterTypes();
            return 0 == cc.length;
        } catch (SecurityException e) {
            return false;
        }
    }

}
