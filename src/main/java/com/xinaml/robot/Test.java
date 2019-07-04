package com.xinaml.robot;

import com.alibaba.fastjson.JSON;
import com.xinaml.robot.common.constant.UrlConst;
import com.xinaml.robot.common.okex.Client;
import com.xinaml.robot.vo.user.OrderInfo;

import static com.xinaml.robot.common.constant.UrlConst.ORDER_INFO;

/**
 * @Author: [lgq]
 * @Date: [19-7-1 上午9:05]
 * @Description:
 * @Version: [1.0.0]
 * @Copy: [com.xinaml]
 */
public class Test {
    public static void main(String[] args) {
        String url = ORDER_INFO + "/" +"BTC-USD-190927" + "/" + "3111121638694912";
        String rs = Client.httpGet(url,null);
        if (null != rs && rs.indexOf("instrument_id") != -1) {//获取订单成功
            OrderInfo info = JSON.parseObject(rs, OrderInfo.class);
            System.out.println(info);;
        }

        System.out.println(rs);
    }
}
