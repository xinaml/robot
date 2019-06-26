package com.xinaml.robot.to.user;

import com.xinaml.robot.base.entity.ADD;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @Author: [lgq]
 * @Date: [19-6-25 下午5:28]
 * @Description:
 * @Version: [1.0.0]
 * @Copy: [com.xinaml]
 */
public class UserConfTO {
    @NotNull(groups = {ADD.class}, message = "请填写k线的粒度！")
    private String time; //获取k线的粒度设置
    @NotNull(groups = {ADD.class}, message = "请填写保留金额！")
    private Double account;//保留金额
    @NotBlank(groups = {ADD.class}, message = "请填写币种！")
    private String type;//币种
    @NotNull(groups = {ADD.class}, message = "请填写买入价倍率！")
    private Double buyMultiple;//买入价倍率
    @NotNull(groups = {ADD.class}, message = "请填写卖出价倍率！")
    private Double selfMultiple;//卖出价倍率

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

}
