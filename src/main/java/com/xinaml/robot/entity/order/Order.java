package com.xinaml.robot.entity.order;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.xinaml.robot.base.entity.BaseEntity;
import com.xinaml.robot.entity.user.User;

import javax.persistence.*;

@Entity
@Table(name = "tb_order")
public class Order extends BaseEntity {
    @JsonIgnore
    @ManyToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "user_id", columnDefinition = "VARCHAR(36) COMMENT '所属用户' ")
    private User user;

    @Column(columnDefinition = "VARCHAR(56) COMMENT '合约ID'")
    private String instrumentId;//合约ID

    @Column(nullable = false, columnDefinition = "VARCHAR(56) COMMENT '自定义订单id'")
    private String clientOid;//自定义订单id

    @Column( columnDefinition = "VARCHAR(56) COMMENT '订单id'")
    private String orderId;//

    @Column( columnDefinition = "VARCHAR(255) COMMENT '错误信息'")
    private String errorMessage;

    @Column( columnDefinition = "VARCHAR(56) COMMENT '错误编号'")
    private String errorCode;

    @Column( columnDefinition = "INT(2) COMMENT '订单状态:-2:失败,-1:撤单成功,0:等待成交 ,1:部分成交, 2:完全成交,3:下单中,4:撤单中'")
    private Integer status;//1，下单未成功，2，撤单 ，3成交

    @Column( columnDefinition = "INT(2) COMMENT '订单类型，1：买入，2卖出'")
    private Integer type;//1：买入，2卖出

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
}
