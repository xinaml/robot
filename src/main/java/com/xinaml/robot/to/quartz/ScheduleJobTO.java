package com.xinaml.robot.to.quartz;

import com.xinaml.robot.base.entity.ADD;
import com.xinaml.robot.base.entity.EDIT;
import com.xinaml.robot.base.to.BaseTO;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @Author: [liguiqin]
 * @Date:[2018-12-29 10:18]
 * @Description: [ ]
 * @Version: [3.0]
 * @Copy: [com.changbei]
 */
public class ScheduleJobTO extends BaseTO {


    /**
     * 执行类
     */
    @NotBlank(message = "执行类不能为空", groups = {ADD.class, EDIT.class})
    private String clazz;

    /**
     * 任务名
     */
    @NotBlank(message = "任务名不能为空", groups = {ADD.class, EDIT.class})
    private String name;

    /**
     * 执行方法
     */
    @NotBlank(message = "执行方法不能为空", groups = {ADD.class, EDIT.class})
    private String method;

    /**
     * 表达式
     */
    @NotBlank(message = "表达式不能为空", groups = {ADD.class, EDIT.class})
    private String expression;

    /**
     * 描述
     */
    @NotBlank(message = "描述不能为空", groups = {ADD.class, EDIT.class})
    private String description;

    /**
     * 是否启用
     */
    @NotNull(message = "是否启用不能为空", groups = {ADD.class, EDIT.class})
    private Boolean enable;


    /**
     * 任务所属调度组
     */
    @NotBlank(message = "任务所属调度组不能为空", groups = {ADD.class, EDIT.class})
    private String scheduleJobGroupId;

    public String getClazz() {
        return clazz;
    }

    public void setClazz(String clazz) {
        this.clazz = clazz;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

    public String getScheduleJobGroupId() {
        return scheduleJobGroupId;
    }

    public void setScheduleJobGroupId(String scheduleJobGroupId) {
        this.scheduleJobGroupId = scheduleJobGroupId;
    }

}
