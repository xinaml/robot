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
    private Double account;//保留金额
    @NotNull(groups = {ADD.class}, message = "请填写开多每次开张数！")
    private Integer count;//开张数
     @NotNull(groups = {ADD.class}, message = "请填写开空每次开张数！")
    private Integer downCount;//开张数
    @NotBlank(groups = {ADD.class}, message = "请填写币种！")
    private String type;//币种
    @NotNull(groups = {ADD.class}, message = "请填写挂单时间！")
    private Integer orderTime;//挂单时间
     @NotNull(groups = {ADD.class}, message = "请填写止损后停止时间！")
    private Integer stopTime;//止损后停止时间
     @NotNull(groups = {ADD.class}, message = "请填写等待卖出时间！")
    private Integer sellTime;//等待卖出时间
    @NotBlank(groups = {ADD.class}, message = "请填写合约id！")
    private String contract;//合约id
    @NotNull(groups = {ADD.class}, message = "请填写开多买入价倍率！")
    private Double buyMultiple;//买入价倍率
    @NotNull(groups = {ADD.class}, message = "请填写开多卖出价倍率！")
    private Double selfMultiple;//卖出价倍率
    @NotNull(groups = {ADD.class}, message = "请填写开空买入价倍率！")
    private Double downBuyMultiple;//买入价倍率
    @NotNull(groups = {ADD.class}, message = "请填写开空卖出价倍率！")
    private Double downSelfMultiple;//卖出价倍率
    @Range(max = 100, min = 1, message = "杠杆倍数:1-100")
    @NotNull(groups = {ADD.class}, message = "请填写杠杆倍数！")
    private Integer leverage;//杠杆倍数
    @NotNull(groups = {ADD.class}, message = "请填写开多亏损率值！")
    private Double loss;//亏损多少卖出
    @NotNull(groups = {ADD.class}, message = "请填写开多收益率值！")
    private Double profit;//收益达到多少卖出
    @NotNull(groups = {ADD.class}, message = "请填写开空亏损率值！")
    private Double downLoss;//亏损多少卖出
    @NotNull(groups = {ADD.class}, message = "请填写开空收益率值！")
    private Double downProfit;//收益达到多少卖出
    private Boolean downOnlySell=false;//是否仅卖出

    private Boolean onlySell=false;//是否仅卖出
    private Boolean up=false;//买多
    private Boolean down=false;//买空
    private Double buyVal;//买入值阀


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

    public Double getProfit() {
        return profit;
    }

    public void setProfit(Double profit) {
        this.profit = profit;
    }

    public Double getBuyVal() {
        return buyVal;
    }

    public void setBuyVal(Double buyVal) {
        this.buyVal = buyVal;
    }

    public Boolean getUp() {
        return up;
    }

    public void setUp(Boolean up) {
        this.up = up;
    }

    public Boolean getDown() {
        return down;
    }

    public void setDown(Boolean down) {
        this.down = down;
    }

    public Integer getDownCount() {
        return downCount;
    }

    public void setDownCount(Integer downCount) {
        this.downCount = downCount;
    }

    public Double getDownBuyMultiple() {
        return downBuyMultiple;
    }

    public void setDownBuyMultiple(Double downBuyMultiple) {
        this.downBuyMultiple = downBuyMultiple;
    }

    public Double getDownSelfMultiple() {
        return downSelfMultiple;
    }

    public void setDownSelfMultiple(Double downSelfMultiple) {
        this.downSelfMultiple = downSelfMultiple;
    }

    public Double getDownLoss() {
        return downLoss;
    }

    public void setDownLoss(Double downLoss) {
        this.downLoss = downLoss;
    }

    public Double getDownProfit() {
        return downProfit;
    }

    public void setDownProfit(Double downProfit) {
        this.downProfit = downProfit;
    }

    public Boolean getDownOnlySell() {
        return downOnlySell;
    }

    public void setDownOnlySell(Boolean downOnlySell) {
        this.downOnlySell = downOnlySell;
    }

    public Integer getStopTime() {
        return stopTime;
    }

    public void setStopTime(Integer stopTime) {
        this.stopTime = stopTime;
    }
}
