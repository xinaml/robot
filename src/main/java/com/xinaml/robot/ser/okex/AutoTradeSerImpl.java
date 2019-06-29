package com.xinaml.robot.ser.okex;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xinaml.robot.common.constant.UrlConst;
import com.xinaml.robot.common.okex.Client;
import com.xinaml.robot.entity.user.UserConf;
import com.xinaml.robot.vo.user.KLine;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
        String kLineUrl = UrlConst.K_LINE + "/"+conf.getType()+"-"+conf.getContract()+"/candles?start=" + conf.getStartDate() + "&end=" + conf.getEndDate() + "&granularity=" + conf.getSeconds();
        System.out.println(kLineUrl);
        String lastUrl = UrlConst.LAST + "/"+conf.getType()+"-"+conf.getContract()+"/ticker";
        String rs = Client.httpGet(kLineUrl, conf.getUser());
        KLine line = getLine(rs);
        rs = Client.httpGet(lastUrl, conf.getUser());
        Double last =getLast(rs); //最新成交价
        if(line!=null && last!=null){
            double buy =conf.getBuyMultiple()*line.getClose();//买入价=买入价倍率*收盘价
            System.out.println("buy:"+buy+"---last:"+last);
        }
    }

    private KLine getLine(String rs) {
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
        }else {
            return null;
        }
        return null;
    }

    /**
     * 最新成交价
     * @param rs
     * @return
     */
    private Double getLast(String rs) {
        if (rs.length() > 5) {
            JSONObject object = JSON.parseObject(rs);
            return object.getDouble("last");
        }else {
            return null;
        }
    }
}
