package com.xinaml.robot.entity.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.xinaml.robot.base.entity.BaseEntity;

import javax.persistence.*;

/**
 * @Author: [lgq]
 * @Date: [19-6-25 下午4:31]
 * @Description:
 * @Version: [1.0.0]
 * @Copy: [com.changbei]
 */
@Entity
@Table(name = "tb_conf")
public class UserConf extends BaseEntity {
    @JsonIgnore
    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", columnDefinition = "VARCHAR(36) COMMENT '所属用户' ")
    private User user;

    @Column(columnDefinition = " INT(10) COMMENT 'k线的粒度'")
    private Integer minute; //获取k线的粒度设置

    @Column(columnDefinition = " DECIMAL(10,2) COMMENT '保留金额'")
    private Double account;//保留金额

    @Column(unique = true, length = 20, columnDefinition = "VARCHAR(25) COMMENT '币种'")
    private String type;//币种

    @Column(columnDefinition = " DECIMAL(10,2) COMMENT '买入价倍率'")
    private Double buyMultiple;//买入价倍率

    @Column(columnDefinition = " DECIMAL(10,2) COMMENT '卖出价倍率'")
    private Double selfMultiple;//卖出价倍率

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Integer getMinute() {
        return minute;
    }

    public void setMinute(Integer minute) {
        this.minute = minute;
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
}
