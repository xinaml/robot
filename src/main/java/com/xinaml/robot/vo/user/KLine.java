package com.xinaml.robot.vo.user;

public class KLine {

//    参数名	参数类型	描述
//    timestamp	String	系统时间
//    open	String	开盘价格
//    high	String	最高价格
//    low	String	最低价格
//    close	String	收盘价格
//    volume	String	交易量(按张折算)
//    currency_volume	String	交易量（按币折算）
    private String timestamp;
    private Double open;
    private Double high;
    private Double low;
    private Double close;

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public Double getOpen() {
        return open;
    }

    public void setOpen(Double open) {
        this.open = open;
    }

    public Double getHigh() {
        return high;
    }

    public void setHigh(Double high) {
        this.high = high;
    }

    public Double getLow() {
        return low;
    }

    public void setLow(Double low) {
        this.low = low;
    }

    public Double getClose() {
        return close;
    }

    public void setClose(Double close) {
        this.close = close;
    }

    public Double getVolume() {
        return volume;
    }

    public void setVolume(Double volume) {
        this.volume = volume;
    }

    public Double getCurrencyVolume() {
        return currencyVolume;
    }

    public void setCurrencyVolume(Double currencyVolume) {
        this.currencyVolume = currencyVolume;
    }

    private Double volume;
    private Double currencyVolume;

}
