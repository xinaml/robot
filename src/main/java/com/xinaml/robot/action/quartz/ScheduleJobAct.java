package com.xinaml.robot.action.quartz;

import com.xinaml.robot.base.entity.ADD;
import com.xinaml.robot.base.entity.EDIT;
import com.xinaml.robot.common.custom.annotation.Login;
import com.xinaml.robot.common.custom.exception.ActException;
import com.xinaml.robot.common.custom.exception.SerException;
import com.xinaml.robot.common.custom.result.ActResult;
import com.xinaml.robot.common.custom.result.Result;
import com.xinaml.robot.entity.quartz.ScheduleJob;
import com.xinaml.robot.ser.quartz.ScheduleJobSer;
import com.xinaml.robot.to.quartz.ScheduleJobTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @Author: [liguiqin]
 * @Date:[2018-12-29 11:17]
 * @Description: [ ]
 * @Version: [3.0]
 * @Copy: [com.xinaml]
 */
@Login
@RestController
@RequestMapping("schedule/job")
public class ScheduleJobAct {

    @Autowired
    private ScheduleJobSer scheduleJobSer;

    /**
     * 添加
     *
     * @param scheduleJobTO 实体数据
     * @version v1
     */
    @PostMapping("add")
    public Result add(@Validated(ADD.class) ScheduleJobTO scheduleJobTO, BindingResult result) throws ActException {
        try {
            ScheduleJob job = scheduleJobSer.add(scheduleJobTO);
            return new ActResult(job);
        } catch (SerException e) {
            throw new ActException(e.getMessage());
        }
    }

    /**
     * 编辑
     *
     * @param scheduleJobTO 实体数据
     * @version v1
     */
    @PutMapping("edit")
    public Result edit(@Validated(EDIT.class) ScheduleJobTO scheduleJobTO, BindingResult result) throws ActException {
        try {
            scheduleJobSer.edit(scheduleJobTO);
            return new ActResult("编辑成功");
        } catch (SerException e) {
            throw new ActException(e.getMessage());
        }
    }

    /**
     * 删除
     *
     * @param id
     * @version v1
     */
    @DeleteMapping("delete/{id}")
    public Result delete(@PathVariable String id) throws ActException {
        try {
            scheduleJobSer.delete(id);
            return new ActResult("删除成功");
        } catch (SerException e) {
            throw new ActException(e.getMessage());
        }
    }

    /**
     * 开启关闭
     *
     * @param id
     * @param enable 开启
     * @version v1
     */
    @PutMapping("enable/{id}/{enable}")
    public Result enable(@PathVariable String id, @PathVariable boolean enable) throws ActException {
        try {
            scheduleJobSer.enable(id, enable);
            return new ActResult("操作成功");
        } catch (SerException e) {
            throw new ActException(e.getMessage());
        }
    }
}
