package com.xinaml.robot.entity.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.xinaml.robot.base.entity.BaseEntity;
import com.xinaml.robot.common.okex.utils.DateUtils;
import com.xinaml.robot.common.utils.DateUtil;

import javax.persistence.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.concurrent.TimeUnit;

/**
 * @Author: [lgq]
 * @Date: [19-6-25 下午4:31]
 * @Description:
 * @Version: [1.0.0]
 * @Copy: [com.xinaml]
 */
@Entity
@Table(name = "tb_conf")
public class UserConf extends BaseEntity {
    @JsonIgnore
    @ManyToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "user_id", columnDefinition = "VARCHAR(36) COMMENT '所属用户' ")
    private User user;

    @Column(unique = true, length = 20, columnDefinition = "VARCHAR(25) COMMENT 'k线的粒度'")
    private String time; //获取k线的粒度设置

    @Column(columnDefinition = " DECIMAL(10,2) COMMENT '保留金额'")
    private Double account;//保留金额

    @Column(unique = true, length = 20, columnDefinition = "VARCHAR(25) COMMENT '币种'")
    private String type;//币种

    @Column(columnDefinition = " DECIMAL(10,2) COMMENT '买入价倍率'")
    private Double buyMultiple;//买入价倍率

    @Column(columnDefinition = " DECIMAL(10,2) COMMENT '卖出价倍率'")
    private Double selfMultiple;//卖出价倍率

    @Transient
    private Integer seconds=60;//秒
    @Transient
    private String startDate= DateUtils.getUnixTime();
    @Transient
    private String endDate=DateUtils.getUnixTime();

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Double getAccount() {
        return account;
    }

    public void setAccount(Double account) {
        this.account = account;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Double getBuyMultiple() {
        return buyMultiple;
    }

    public void setBuyMultiple(Double buyMultiple) {
        this.buyMultiple = buyMultiple;
    }

    public Double getSelfMultiple() {
        return selfMultiple;
    }

    public void setSelfMultiple(Double selfMultiple) {
        this.selfMultiple = selfMultiple;
    }

    public Integer getSeconds() {
        return seconds;
    }

    public void setSeconds(Integer seconds) {
        this.seconds = seconds;
    }

    public String getStartDate() {
        return  Instant.now().minus(this.seconds,ChronoUnit.SECONDS).toString();
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
}
