package com.xinaml.robot.ser.okex;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xinaml.robot.common.okex.Client;
import com.xinaml.robot.entity.user.User;
import com.xinaml.robot.entity.user.UserConf;
import com.xinaml.robot.vo.user.KLine;
import com.xinaml.robot.vo.user.OrderVO;
import org.springframework.stereotype.Service;

import static com.xinaml.robot.common.constant.UrlConst.*;

/**
 * @Author: [lgq]
 * @Date: [19-6-26 下午1:53]
 * @Description: 自动交易
 * @Version: [1.0.0]
 * @Copy: [com.xinaml]
 */
@Service
public class AutoTradeSerImpl implements AutoTradeSer {

    @Override
    public void trade(UserConf conf) {
        KLine line = getLine(conf);
        Double last = getLast(conf); //最新成交价
        if (line != null && last != null) {
            double buy = conf.getBuyMultiple() * line.getClose();//买入价=买入价倍率*收盘价
            if (buy >= last) {//买入价>=最新成交价
                System.out.println("buy:" + buy + "---last:" + last);
            }
            commitOrder(conf);
        }
    }

    /**
     * 下单
     * @param conf
     */
    public void commitOrder(UserConf conf){
        String commitOrderUrl =COMMIT_ORDER;
        OrderVO orderVO = new OrderVO();
        orderVO.setInstrument_id(conf.getInstrumentId());//合约id
        orderVO.setSize(conf.getCount()+"");//每次开张数
        String rs = Client.httpPost(commitOrderUrl,JSON.toJSONString(orderVO),conf.getUser());
        System.out.println(rs);
    }

    /**
     * 获取k线数据
     * @param conf
     * @return
     */
    public KLine getLine( UserConf conf) {
        String kLineUrl = K_LINE + "/" + conf.getInstrumentId()+ "/candles?start=" + conf.getStartDate() + "&end=" + conf.getEndDate() + "&granularity=" + conf.getSeconds();
        String rs = Client.httpGet(kLineUrl, conf.getUser());
        if (rs.length() > 5) {
            JSONArray array = JSON.parseArray(rs);
            for (Object o : array.toArray()) {
                JSONArray list = (JSONArray) o;
                KLine kLine = new KLine();
                kLine.setTimestamp(list.getString(0));
                kLine.setOpen(list.getDouble(1));
                kLine.setHigh(list.getDouble(2));
                kLine.setLow(list.getDouble(3));
                kLine.setClose(list.getDouble(4));
                kLine.setVolume(list.getDouble(5));
                kLine.setCurrencyVolume(list.getDouble(6));
                return kLine;
            }
        } else {
            return null;
        }
        return null;
    }

    /**
     * 最新成交价
     *
     * @param conf
     * @return
     */
    public Double getLast(UserConf  conf) {
        String lastUrl = LAST + "/" + conf.getInstrumentId() + "/ticker";
        String rs = Client.httpGet(lastUrl, conf.getUser());
        if (rs.length() > 5) {
            JSONObject object = JSON.parseObject(rs);
            return object.getDouble("last");
        } else {
            return null;
        }
    }
}
