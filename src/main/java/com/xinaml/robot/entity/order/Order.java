package com.xinaml.robot.entity.order;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.xinaml.robot.base.entity.BaseEntity;
import com.xinaml.robot.entity.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tb_order")
public class Order extends BaseEntity {
    @JsonIgnore
    @ManyToOne(cascade = CascadeType.REFRESH,fetch=FetchType.LAZY)
    @JoinColumn(name = "user_id", columnDefinition = "VARCHAR(36) COMMENT '所属用户' ")
    private User user;

    @JoinColumn(name = "uid", columnDefinition = "VARCHAR(36) COMMENT '所属用户id' ")
    private String uid;

    @Column(columnDefinition = "VARCHAR(56) COMMENT '合约ID'")
    private String instrumentId;//合约ID

    @Column(columnDefinition = "VARCHAR(56) COMMENT '自定义订单id'")
    private String clientOid;//自定义订单id

    @Column( columnDefinition = "VARCHAR(56) COMMENT '订单id'")
    private String orderId;//

    @Column( columnDefinition = "VARCHAR(255) COMMENT '错误信息'")
    private String errorMessage;

    @Column( columnDefinition = "VARCHAR(56) COMMENT '错误编号'")
    private String errorCode;

    @Column( columnDefinition = "VARCHAR(56) COMMENT '买入价'")
    private String price;

    @Column( columnDefinition = "VARCHAR(56) COMMENT '卖出价'")
    private String sell;

    @Column( columnDefinition = "VARCHAR(56) COMMENT '盈利'")
    private String profit="0";

    @Column( columnDefinition = "VARCHAR(56) COMMENT '卖出id'")
    private String sellId;

    @Column( columnDefinition = "INT(2) COMMENT '订单状态:-2:失败,-1:撤单成功,0:等待成交 ,1:部分成交, 2:完全成交,3:下单中,4:撤单中'")
    private Integer status;//1，下单未成功，2，撤单 ，3成交

    @Column( columnDefinition = "INT(2) COMMENT '订单类型，1：买入，2卖出'")
    private Integer type;//1：买入，2卖出
    @Column(columnDefinition = "DATETIME  COMMENT '卖出时间'")
    private LocalDateTime sellDate;
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getInstrumentId() {
        return instrumentId;
    }

    public void setInstrumentId(String instrumentId) {
        this.instrumentId = instrumentId;
    }

    public String getClientOid() {
        return clientOid;
    }

    public void setClientOid(String clientOid) {
        this.clientOid = clientOid;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getProfit() {
        return profit;
    }

    public void setProfit(String profit) {
        this.profit = profit;
    }

    public String getSellId() {
        return sellId;
    }

    public void setSellId(String sellId) {
        this.sellId = sellId;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getSell() {
        return sell;
    }

    public void setSell(String sell) {
        this.sell = sell;
    }

    public LocalDateTime getSellDate() {
        return sellDate;
    }

    public void setSellDate(LocalDateTime sellDate) {
        this.sellDate = sellDate;
    }
}
