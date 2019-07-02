package com.xinaml.robot.vo.user;

/**
 * 订单信息返回数据
 */
public class OrderInfo {
    private String instrument_id;	//String	合约ID，如BTC-USD-180213
    private String client_oid	;	//String	由您设置的订单ID来识别您的订单
    private String size;	//	String	委托数量
    private String timestamp;	//	String	委托时间
    private String filled_qty;	//	String	成交数量
    private String fee;	//	String	手续费
    private String order_id;	//	String	订单ID
    private String price;	//	String	委托价格
    private String  price_avg;	//	String	成交均价
    private String type;	//	String	订单类型(1:开多 2:开空 3:平多 4:平空)
    private String contract;	//_val	String	合约面值
    private String  leverage;	//	String	杠杆倍数，1-100的整数
    private String order_type;	//	String	0：普通委托 1：只做Maker（Post only） 2：全部成交或立即取消（FOK） 3：立即成交并取消剩余（IOC）
    private String pnl;	//	String	收益
    private String state;	//	String	订单状态("-2":失败,"-1":撤单成功,"0":等待成交 ,"1":部分成交, "2":完全成交,"3":下单中,"4":撤单中,）

    public String getInstrument_id() {
        return instrument_id;
    }

    public void setInstrument_id(String instrument_id) {
        this.instrument_id = instrument_id;
    }

    public String getClient_oid() {
        return client_oid;
    }

    public void setClient_oid(String client_oid) {
        this.client_oid = client_oid;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getFilled_qty() {
        return filled_qty;
    }

    public void setFilled_qty(String filled_qty) {
        this.filled_qty = filled_qty;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPrice_avg() {
        return price_avg;
    }

    public void setPrice_avg(String price_avg) {
        this.price_avg = price_avg;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContract() {
        return contract;
    }

    public void setContract(String contract) {
        this.contract = contract;
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

    public String getPnl() {
        return pnl;
    }

    public void setPnl(String pnl) {
        this.pnl = pnl;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
