package com.xinaml.robot.entity.user;

import com.xinaml.robot.base.entity.BaseEntity;

import javax.persistence.*;

/**
 * @Author: [lgq]
 * @Date: [19-6-13 上午10:53]
 * @Description:
 * @Version: [1.0.0]
 * @Copy: [com.xinaml]
 */
@Entity
@Table(name = "tb_account")
public class Account extends BaseEntity {
    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", columnDefinition = "VARCHAR(36) COMMENT '所属用户' ")
    private User user;

    @Column(unique = true, length = 10, columnDefinition = "VARCHAR(10) COMMENT '币种'")
    private String currency;//币种，如btc

    @Column(columnDefinition = " DECIMAL(10,2) COMMENT '余额'")
    private Double balance;//余额

    @Column(columnDefinition = " DECIMAL(10,2) COMMENT '余额'")
    private Double hold;//	冻结(不可用)

    @Column(columnDefinition = " DECIMAL(10,2) COMMENT '余额'")
    private Double available;//可用余额

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public Double getHold() {
        return hold;
    }

    public void setHold(Double hold) {
        this.hold = hold;
    }

    public Double getAvailable() {
        return available;
    }

    public void setAvailable(Double available) {
        this.available = available;
    }
}
