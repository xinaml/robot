package com.xinaml.robot;

import com.alibaba.fastjson.JSON;
import com.xinaml.robot.common.okex.Client;
import com.xinaml.robot.entity.user.UserConf;
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
        System.out.println(JSON.toJSONString(getOrderInfo("3112214567990272")));
        System.out.println(JSON.toJSONString(getOrderInfo("3112222504949760")));
        System.out.println(JSON.toJSONString(getOrderInfo("3112584599609344")));
        System.out.println(JSON.toJSONString(getOrderInfo("3112592466189312")));
        System.out.println(JSON.toJSONString(getOrderInfo("3112616502309888")));
        System.out.println(JSON.toJSONString(getOrderInfo("3112620332821504")));
    }

    public static OrderInfo getOrderInfo(String orderId) {
        String url = ORDER_INFO + "/" + "BTC-USD-190927" + "/" + orderId;
        String rs = Client.httpGet(url, null);
        if (null != rs && rs.indexOf("instrument_id") != -1) {//获取订单成功
            OrderInfo info = JSON.parseObject(rs, OrderInfo.class);
            return info;
        } else {
            return null;
        }
    }
}
