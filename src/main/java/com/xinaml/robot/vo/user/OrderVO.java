package com.xinaml.robot.vo.user;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.Random;

/**
 * 提交订单构建数据
 */
public class OrderVO {
    @JSONField(ordinal = 1)
    private String client_oid = "RB" + new Random().nextInt(1000000);//	由您设置的订单ID来识别您的订单

    @JSONField(ordinal = 2)
    private String order_type = "0";//	杠杆倍数

    @JSONField(ordinal = 3)
    private String instrument_id = "1";//合约ID

    @JSONField(ordinal = 4)
    private String type = "1";//		1:开多2:开空3:平多4:平空

    @JSONField(ordinal = 5)
    private String price = "1.0";//	每张合约的价格

    @JSONField(ordinal = 6)
    private String size = "1";//	买入或卖出合约的数量（以张计数）

    @JSONField(ordinal = 7)
    private String match_price = "0";//	是否以对手价下单(0:不是 1:是)，默认为0，当取值为1时。price字段无效

    @JSONField(ordinal = 8)
    private String leverage = "1";//	是否以对手价下单(0:不是 1:是)，默认为0，当取值为1时。price字段无效

    public String getClient_oid() {
        return client_oid;
    }

    public void setClient_oid(String client_oid) {
        this.client_oid = client_oid;
    }

    public String getOrder_type() {
        return order_type;
    }

    public void setOrder_type(String order_type) {
        this.order_type = order_type;
    }

    public String getInstrument_id() {
        return instrument_id;
    }

    public void setInstrument_id(String instrument_id) {
        this.instrument_id = instrument_id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getMatch_price() {
        return match_price;
    }

    public void setMatch_price(String match_price) {
        this.match_price = match_price;
    }

    public String getLeverage() {
        return leverage;
    }

    public void setLeverage(String leverage) {
        this.leverage = leverage;
    }
}
