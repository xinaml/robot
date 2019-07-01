package com.xinaml.robot.ser.okex;

import com.xinaml.robot.entity.order.Order;
import com.xinaml.robot.entity.user.UserConf;
import com.xinaml.robot.vo.user.KLine;
import com.xinaml.robot.vo.user.OrderInfo;

/**
 * @Author: [lgq]
 * @Date: [19-6-26 下午1:53]
 * @Description: 自动交易
 * @Version: [1.0.0]
 * @Copy: [com.xinaml]
 */
public interface AutoTradeSer {
    /**
     * 自动交易
     *
     * @param conf
     */
    default void trade(UserConf conf) {

    }

    /**
     * 下单
     *
     * @param conf
     * @param buy  买入价
     */
    default Order commitOrder(UserConf conf, String buy) {
        return null;
    }

    /**
     * 撤单
     *
     * @param conf
     */
    default void cancelOrder(UserConf conf, String orderId) {

    }

    /**
     * 订单信息
     *
     * @param conf
     */
    default OrderInfo getOrderInfo(UserConf conf, String orderId) {
        return null;
    }

    /**
     * 获取k线数据
     *
     * @param conf
     * @return
     */
    default KLine getLine(UserConf conf) {
        return null;
    }

    /**
     * 最新成交价
     *
     * @param conf
     * @return
     */
    default Double getLast(UserConf conf) {
        return null;
    }
}
