package com.xinaml.robot.vo.user;

import java.io.Serializable;

/**
 * @Author: [lgq]
 * @Date: [19-7-5 上午9:29]
 * @Description: 持仓信息
 * @Version: [1.0.0]
 * @Copy: [com.xinaml]
 */
public class HoldInfo implements Serializable {

//    全仓参数名	参数类型	描述
    private String margin_mode;	//	账户类型：全仓 crossed
    private String liquidation_price	;	//	预估强平价
    private String long_qty	;	//	多仓数量
    private String long_avail_qty;	//	多仓可平仓数量
    private String long_avg_cost;	//	开仓平均价
    private String long_settlement_price;	//	多仓结算基准价
    private String realised_pnl;	//	已实现盈余
    private String short_qty;	//	空仓数量
    private String short_avail_qty;	//	空仓可平仓数量
    private String short_avg_cost;	//	开仓平均价
    private String short_settlement_price;	//	空仓结算基准价
    private String instrument_id;	//	合约ID，如BTC-USD-180213
    private String leverage	;	//	杠杆倍数
    private String created_at;	//	创建时间
    private String updated_at;	//	最近一次加减仓的更新时间
    private String short_margi;	//	空仓保证金
    private String short_pnl;	//	空仓收益
    private String short_pnl_ratio;	//	空仓收益率
    private String short_unrealised_pnl;	//	空仓未实现盈亏
    private String long_margin;	//	多仓保证金
    private String long_pnl;	//	多仓收益
    private String long_pnl_ratio;	//	多仓收益率
    private String long_unrealised_pnl;	//	多仓未实现盈亏

    public String getMargin_mode() {
        return margin_mode;
    }

    public void setMargin_mode(String margin_mode) {
        this.margin_mode = margin_mode;
    }

    public String getLiquidation_price() {
        return liquidation_price;
    }

    public void setLiquidation_price(String liquidation_price) {
        this.liquidation_price = liquidation_price;
    }

    public String getLong_qty() {
        return long_qty;
    }

    public void setLong_qty(String long_qty) {
        this.long_qty = long_qty;
    }

    public String getLong_avail_qty() {
        return long_avail_qty;
    }

    public void setLong_avail_qty(String long_avail_qty) {
        this.long_avail_qty = long_avail_qty;
    }

    public String getLong_avg_cost() {
        return long_avg_cost;
    }

    public void setLong_avg_cost(String long_avg_cost) {
        this.long_avg_cost = long_avg_cost;
    }

    public String getLong_settlement_price() {
        return long_settlement_price;
    }

    public void setLong_settlement_price(String long_settlement_price) {
        this.long_settlement_price = long_settlement_price;
    }

    public String getRealised_pnl() {
        return realised_pnl;
    }

    public void setRealised_pnl(String realised_pnl) {
        this.realised_pnl = realised_pnl;
    }

    public String getShort_qty() {
        return short_qty;
    }

    public void setShort_qty(String short_qty) {
        this.short_qty = short_qty;
    }

    public String getShort_avail_qty() {
        return short_avail_qty;
    }

    public void setShort_avail_qty(String short_avail_qty) {
        this.short_avail_qty = short_avail_qty;
    }

    public String getShort_avg_cost() {
        return short_avg_cost;
    }

    public void setShort_avg_cost(String short_avg_cost) {
        this.short_avg_cost = short_avg_cost;
    }

    public String getShort_settlement_price() {
        return short_settlement_price;
    }

    public void setShort_settlement_price(String short_settlement_price) {
        this.short_settlement_price = short_settlement_price;
    }

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

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getShort_margi() {
        return short_margi;
    }

    public void setShort_margi(String short_margi) {
        this.short_margi = short_margi;
    }

    public String getShort_pnl() {
        return short_pnl;
    }

    public void setShort_pnl(String short_pnl) {
        this.short_pnl = short_pnl;
    }

    public String getShort_pnl_ratio() {
        return short_pnl_ratio;
    }

    public void setShort_pnl_ratio(String short_pnl_ratio) {
        this.short_pnl_ratio = short_pnl_ratio;
    }

    public String getShort_unrealised_pnl() {
        return short_unrealised_pnl;
    }

    public void setShort_unrealised_pnl(String short_unrealised_pnl) {
        this.short_unrealised_pnl = short_unrealised_pnl;
    }

    public String getLong_margin() {
        return long_margin;
    }

    public void setLong_margin(String long_margin) {
        this.long_margin = long_margin;
    }

    public String getLong_pnl() {
        return long_pnl;
    }

    public void setLong_pnl(String long_pnl) {
        this.long_pnl = long_pnl;
    }

    public String getLong_pnl_ratio() {
        return long_pnl_ratio;
    }

    public void setLong_pnl_ratio(String long_pnl_ratio) {
        this.long_pnl_ratio = long_pnl_ratio;
    }

    public String getLong_unrealised_pnl() {
        return long_unrealised_pnl;
    }

    public void setLong_unrealised_pnl(String long_unrealised_pnl) {
        this.long_unrealised_pnl = long_unrealised_pnl;
    }
}
