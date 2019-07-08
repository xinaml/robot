package com.xinaml.robot.vo.user;

/**
 * @Author: [lgq]
 * @Date: [19-7-8 上午9:44]
 * @Description: 合约账户信息
 * @Version: [1.0.0]
 * @Copy: [com.xinaml]
 */
public class AccountInfo {
    private String margin_mode;//	String	账户类型：全仓 crossed
    private String equity	;//String	账户权益
    private String total_avail_balance	;//String	账户余额
    private String margin;	//	保证金（挂单冻结+持仓已用）
    private String margin_frozen;//	String	持仓已用保证金
    private String margin_for_unfilled	;//String	挂单冻结保证金
    private String realised_pnl;//	String	已实现盈亏
    private String unrealised_pnl;//	String	未实现盈亏
    private String margin_ratio;//	String	保证金率
    private String maint_margin_ratio;//	String	维持保证金率
    private String liqui_mode;//	string;	强平模式：tier（梯度强平）legacy（一次强平）
    private String available_balance;//	string	可划转数量

    public String getMargin_mode() {
        return margin_mode;
    }

    public void setMargin_mode(String margin_mode) {
        this.margin_mode = margin_mode;
    }

    public String getEquity() {
        return equity;
    }

    public void setEquity(String equity) {
        this.equity = equity;
    }

    public String getTotal_avail_balance() {
        return total_avail_balance;
    }

    public void setTotal_avail_balance(String total_avail_balance) {
        this.total_avail_balance = total_avail_balance;
    }

    public String getMargin() {
        return margin;
    }

    public void setMargin(String margin) {
        this.margin = margin;
    }

    public String getMargin_frozen() {
        return margin_frozen;
    }

    public void setMargin_frozen(String margin_frozen) {
        this.margin_frozen = margin_frozen;
    }

    public String getMargin_for_unfilled() {
        return margin_for_unfilled;
    }

    public void setMargin_for_unfilled(String margin_for_unfilled) {
        this.margin_for_unfilled = margin_for_unfilled;
    }

    public String getRealised_pnl() {
        return realised_pnl;
    }

    public void setRealised_pnl(String realised_pnl) {
        this.realised_pnl = realised_pnl;
    }

    public String getUnrealised_pnl() {
        return unrealised_pnl;
    }

    public void setUnrealised_pnl(String unrealised_pnl) {
        this.unrealised_pnl = unrealised_pnl;
    }

    public String getMargin_ratio() {
        return margin_ratio;
    }

    public void setMargin_ratio(String margin_ratio) {
        this.margin_ratio = margin_ratio;
    }

    public String getMaint_margin_ratio() {
        return maint_margin_ratio;
    }

    public void setMaint_margin_ratio(String maint_margin_ratio) {
        this.maint_margin_ratio = maint_margin_ratio;
    }

    public String getLiqui_mode() {
        return liqui_mode;
    }

    public void setLiqui_mode(String liqui_mode) {
        this.liqui_mode = liqui_mode;
    }

    public String getAvailable_balance() {
        return available_balance;
    }

    public void setAvailable_balance(String available_balance) {
        this.available_balance = available_balance;
    }
}
