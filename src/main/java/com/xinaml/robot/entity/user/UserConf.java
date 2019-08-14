package com.xinaml.robot.entity.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.xinaml.robot.base.entity.BaseEntity;
import com.xinaml.robot.common.okex.utils.DateUtils;

import javax.persistence.*;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

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

    @Column(length = 20, columnDefinition = "VARCHAR(25) COMMENT 'k线的粒度'")
    private String time; //获取k线的粒度设置

    @Column(length = 20, columnDefinition = "INT(3) COMMENT '挂单时间'")
    private Integer orderTime = 10; //挂单时间,默认10分钟

    @Column(length = 20, columnDefinition = "INT(3) COMMENT '等待卖出时间'")
    private Integer sellTime = 10; //等待卖出时间,默认10分钟

    @Column(length = 20, columnDefinition = "INT(3) COMMENT '止损停止交易等待时间'")
    private Integer stopTime = 10; //止损停止交易等待时间

    @Column(columnDefinition = " DECIMAL(10,5) COMMENT '保留金额'")
    private Double account;//保留金额

    @Column(columnDefinition = " INT(5) COMMENT '每次开张数'")
    private Integer count;//每次开张数

    @Column(columnDefinition = " INT(5) COMMENT '每次买空开张数'")
    private Integer downCount;//每次开张数

    @Column(length = 20, columnDefinition = "VARCHAR(25) COMMENT '币种'")
    private String type;//币种

    @Column(length = 20, columnDefinition = "VARCHAR(25) COMMENT '合约id'")
    private String contract;//合约id

    @Column(columnDefinition = " DECIMAL(10,5) COMMENT '买入价倍率'")
    private Double buyMultiple;//买入价倍率

    @Column(columnDefinition = " DECIMAL(10,5) COMMENT '卖出价倍率'")
    private Double selfMultiple;//卖出价倍率
    @Column(columnDefinition = " DECIMAL(10,5) COMMENT '买空买入价倍率'")
    private Double downBuyMultiple;//买入价倍率

    @Column(columnDefinition = " DECIMAL(10,5) COMMENT '买空卖出价倍率'")
    private Double downSelfMultiple;//卖出价倍率

    @Column(columnDefinition = "INT(5) COMMENT '杠杆倍数'")
    private Integer leverage = 1;//杠杆倍数

    //常用类型 ：只等待卖出
    @Column(name = "is_only_sell", columnDefinition = "TINYINT(2) DEFAULT 1 COMMENT '只等待卖出'", nullable = false, insertable = false)
    private Boolean onlySell;

    //常用类型 ：只等待卖出
    @Column(name = "is_down_only_sell", columnDefinition = "TINYINT(2) DEFAULT 1 COMMENT '买空，只等待卖出'", nullable = false, insertable = false)
    private Boolean downOnlySell;
    /**
     * 买空或者买多,默认买多
     */
    @Column(name = "is_up", columnDefinition = "TINYINT(2) DEFAULT 1 COMMENT '买多'", nullable = true, insertable = false)
    private Boolean up;
    /**
     * 买空或者买多,默认买多
     */
    @Column(name = "is_down", columnDefinition = "TINYINT(2) DEFAULT 1 COMMENT '买空'", nullable = true, insertable = false)
    private Boolean down;

    @Column(columnDefinition = " DECIMAL(10,5) COMMENT '亏损多少卖出'")
    private Double loss = 0.1;//亏损多少卖出
    @Column(columnDefinition = " DECIMAL(10,5) COMMENT '买空亏损多少卖出'")
    private Double downLoss = 0.1;//亏损多少卖出
    @Column(columnDefinition = " DECIMAL(10,5) COMMENT '收益达到多少全部卖出'")
    private Double profit = 20.0;//收益达到多少全部卖出
    @Column(columnDefinition = " DECIMAL(10,5) COMMENT '买空收益达到多少全部卖出'")
    private Double downProfit = 20.0;//收益达到多少全部卖出
    @Column(columnDefinition = " DECIMAL(10,5) COMMENT '买入值阀=收盘价-开仓平均价'")
    private Double buyVal;//买入值阀

    @Transient
    private Integer seconds = 60;//秒
    @Transient
    private String startDate = DateUtils.getUnixTime();
    @Transient
    private String endDate = DateUtils.getUnixTime();

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
        String time = Instant.now().minus(seconds * 2, ChronoUnit.SECONDS).toString();
        return time;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {

        String time = Instant.now().minus(seconds, ChronoUnit.SECONDS).toString();
        return time;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getContract() {
        return contract;
    }

    public void setContract(String contract) {
        this.contract = contract;
    }

    public String getInstrumentId() {
        return this.type + "-" + contract;
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

    public Boolean getDownOnlySell() {
        return downOnlySell;
    }

    public void setDownOnlySell(Boolean downOnlySell) {
        this.downOnlySell = downOnlySell;
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

    public Integer getStopTime() {
        return stopTime;
    }

    public void setStopTime(Integer stopTime) {
        this.stopTime = stopTime;
    }
}
