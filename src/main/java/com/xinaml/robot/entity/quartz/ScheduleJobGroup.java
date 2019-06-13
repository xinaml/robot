package com.xinaml.robot.entity.quartz;

import com.xinaml.robot.base.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @Author: [liguiqin]
 * @Date:[2018-12-29 09:44]
 * @Description: [ ]
 * @Version: [3.0]
 * @Copy: [com.xinaml]
 */
@Entity
@Table(name = "tb_schedule_job_group")
public class ScheduleJobGroup extends BaseEntity {

    /**
     * 组名
     */
    @Column(name = "name", nullable = false, columnDefinition = "VARCHAR(255)   COMMENT '组名'")
    private String name;

    /**
     * 描述
     */
    @Column(name = "description", columnDefinition = "VARCHAR(255)   COMMENT '描述'")
    private String description;

    /**
     * 是否启用
     */
    @Column(name = "is_enable", nullable = false, columnDefinition = "TINYINT(1)   COMMENT '是否启用'")
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
