package com.xinaml.robot;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.xinaml.robot.common.okex.Client;
import com.xinaml.robot.entity.user.UserConf;
import com.xinaml.robot.vo.user.KLine;

import static com.xinaml.robot.common.constant.UrlConst.K_LINE;

/**
 * @Author: [lgq]
 * @Date: [19-7-1 上午9:05]
 * @Description:
 * @Version: [1.0.0]
 * @Copy: [com.xinaml]
 */
public class Test {
    public static void main(String[] args) {
        UserConf conf = new UserConf();
        int s = 3 * 60;
        conf.setSeconds(s);
        String start = conf.getStartDate();
        String end = conf.getEndDate();
        String kLineUrl = K_LINE + "/" + "BTC-USD-190927" + "/candles?" + start + "&end=" + end + "&granularity=" + s;
        String rs = Client.httpGet(kLineUrl, null);
        System.out.println(start);
        System.out.println(end);
        if (null != rs && rs.length() > 5) {
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
                System.out.println(JSON.toJSONString(kLine));
                break;
            }
        }


    }


}
