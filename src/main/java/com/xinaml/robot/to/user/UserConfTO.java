package com.xinaml.robot.to.user;

import com.xinaml.robot.base.entity.ADD;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @Author: [lgq]
 * @Date: [19-6-25 下午5:28]
 * @Description:
 * @Version: [1.0.0]
 * @Copy: [com.changbei]
 */
public class UserConfTO {

    @NotNull(groups = {ADD.class}, message = "k线的粒度不能为空！")
    private Integer minute; //获取k线的粒度设置
    @NotNull(groups = {ADD.class}, message = "保留金额不能为空！")
    private Double account;//保留金额
    @NotBlank(groups = {ADD.class}, message = "币种不能为空！")
    private String type;//币种
    @NotNull(groups = {ADD.class}, message = "买入价倍率不能为空！")
    private Double buyMultiple;//买入价倍率
    @NotNull(groups = {ADD.class}, message = "卖出价倍率不能为空！")
    private Double selfMultiple;//卖出价倍率

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
