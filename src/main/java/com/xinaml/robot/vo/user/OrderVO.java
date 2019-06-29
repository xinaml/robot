package com.xinaml.robot.vo.user;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;

import java.util.List;
import java.util.UUID;

public class OrderVO {
    @JSONField(ordinal = 1)
    private String instrument_id;//合约ID

    @JSONField(ordinal = 2)
    private String leverage;//	杠杆倍数

    @JSONField(ordinal = 3)
    private String order_type = "0";//	杠杆倍数

    @JSONField(ordinal = 4)
    private String client_oid = UUID.randomUUID().toString();//	由您设置的订单ID来识别您的订单

    @JSONField(ordinal = 5)
    private List<OrdersData> orders_data;

    public String getInstrument_id() {
        return instrument_id;
    }

    public void setInstrument_id(String instrument_id) {
        this.instrument_id = instrument_id;
    }

    public String getLeverage() {
        return leverage;
    }

    public void setLeverage(String leverage) {
        this.leverage = leverage;
    }

    public String getOrder_type() {
        return order_type;
    }

    public void setOrder_type(String order_type) {
        this.order_type = order_type;
    }

    public String getClient_oid() {
        return client_oid;
    }

    public void setClient_oid(String client_oid) {
        this.client_oid = client_oid;
    }


    public List<OrdersData> getOrders_data() {
        return orders_data;
    }

    public void setOrders_data(List<OrdersData> orders_data) {
        this.orders_data = orders_data;
    }

}
