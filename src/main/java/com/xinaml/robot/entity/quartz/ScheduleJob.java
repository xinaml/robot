package com.xinaml.robot.entity.quartz;

import com.xinaml.robot.base.entity.BaseEntity;
import com.xinaml.robot.entity.user.User;

import javax.persistence.*;

/**todo 定时器可加执行方法参数等 ，暂不实现
 * @Author: [liguiqin]
 * @Date:[2018-12-29 09:42]
 * @Description: [ ]
 * @Version: [3.0]
 * @Copy: [com.xinaml]
 */
@Entity
@Table(name = "tb_schedule_job")
public class ScheduleJob extends BaseEntity {


    /**
     * 制定人
     */
    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false, columnDefinition = "VARCHAR(36) COMMENT '定制人' ")
    private User user;


    /**
     * 执行类
     */
    @Column(name = "clazz", nullable = false, columnDefinition = "VARCHAR(255)   COMMENT '执行类'")
    private String clazz;

    /**
     * 任务名
     */
    @Column(name = "name", nullable = false, columnDefinition = "VARCHAR(255)   COMMENT '任务名'")
    private String name;

    /**
     * 执行方法
     */
    @Column(name = "method", nullable = false, columnDefinition = "VARCHAR(255)   COMMENT '执行方法'")
    private String method;

    /**
     * 表达式
     */
    @Column(name = "expression", nullable = false, columnDefinition = "VARCHAR(255)   COMMENT '表达式'")
    private String expression;

    /**
     * 描述
     */
    @Column(name = "description", nullable = false, columnDefinition = "VARCHAR(255)   COMMENT '描述'")
    private String description;


    /**
     * 是否启用
     */
    @Column(name = "is_enable", nullable = false, columnDefinition = "TINYINT(1)   COMMENT '是否启用'")
    private Boolean enable;

    /**
     * 任务调度组
     */
    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    @JoinColumn(name = "schedule_job_group", nullable = false, columnDefinition = "VARCHAR(36) COMMENT '任务调度组' ")
    private ScheduleJobGroup scheduleJobGroup;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

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

    public ScheduleJobGroup getScheduleJobGroup() {
        return scheduleJobGroup;
    }

    public void setScheduleJobGroup(ScheduleJobGroup scheduleJobGroup) {
        this.scheduleJobGroup = scheduleJobGroup;
    }
}
