package com.xinaml.robot.to.user;

import com.xinaml.robot.base.entity.ADD;
import org.hibernate.validator.constraints.Range;

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
    @NotNull(groups = {ADD.class}, message = "请填写每次开张数！")
    private Integer count;//开张数
    @NotBlank(groups = {ADD.class}, message = "请填写币种！")
    private String type;//币种
    @NotNull(groups = {ADD.class}, message = "请填写挂单时间！")
    private Integer orderTime;//挂单时间
     @NotNull(groups = {ADD.class}, message = "请填写等待卖出时间！")
    private Integer sellTime;//等待卖出时间
    @NotBlank(groups = {ADD.class}, message = "请填写币种！")
    private String contract;//合约id
    @NotNull(groups = {ADD.class}, message = "请填写合约id！")
    private Double buyMultiple;//买入价倍率
    @NotNull(groups = {ADD.class}, message = "请填写卖出价倍率！")
    private Double selfMultiple;//卖出价倍率
    @Range(max = 100, min = 1, message = "杠杆倍数:1-100")
    @NotNull(groups = {ADD.class}, message = "请填写杠杆倍数！")
    private Integer leverage;//杠杆倍数
    private Double loss;//亏损多少卖出
    private Boolean onlySell=false;


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

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public void setSelfMultiple(Double selfMultiple) {
        this.selfMultiple = selfMultiple;
    }

    public String getContract() {
        return contract;
    }

    public void setContract(String contract) {
        this.contract = contract;
    }

    public Integer getLeverage() {
        return leverage;
    }

    public void setLeverage(Integer leverage) {
        this.leverage = leverage;
    }

    public Integer getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(Integer orderTime) {
        this.orderTime = orderTime;
    }

    public Integer getSellTime() {
        return sellTime;
    }

    public void setSellTime(Integer sellTime) {
        this.sellTime = sellTime;
    }

    public Boolean getOnlySell() {
        return onlySell;
    }

    public void setOnlySell(Boolean onlySell) {
        this.onlySell = onlySell;
    }

    public Double getLoss() {
        return loss;
    }

    public void setLoss(Double loss) {
        this.loss = loss;
    }
}
