package com.xinaml.robot.to.quartz;

import com.xinaml.robot.base.entity.ADD;
import com.xinaml.robot.base.entity.EDIT;
import com.xinaml.robot.base.to.BaseTO;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @Author: [liguiqin]
 * @Date:[2018-12-29 10:17]
 * @Description: [ ]
 * @Version: [3.0]
 * @Copy: [com.xinaml]
 */
public class ScheduleJobGroupTO extends BaseTO {

    /**
     * 组名
     */
    @NotBlank(message = "组名不能为空", groups = {ADD.class, EDIT.class})
    private String name;

    /**
     * 描述
     */
    private String description;

    /**
     * 是否启用
     */
    @NotNull(message = "是否启用不能为空", groups = {ADD.class, EDIT.class})
    private Boolean enable;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
}
