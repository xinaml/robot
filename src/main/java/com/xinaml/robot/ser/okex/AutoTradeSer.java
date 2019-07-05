package com.xinaml.robot.ser.okex;

import com.xinaml.robot.entity.order.Order;
import com.xinaml.robot.entity.user.User;
import com.xinaml.robot.entity.user.UserConf;
import com.xinaml.robot.vo.user.HoldInfo;
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
     * 卖出，下单
     *
     * @param conf
     */
    default Order commitSellOrder(UserConf conf) {
        return null;
    }

    /**
     * 买入，下单
     *
     * @param conf 配置
     * @param buy  买入价
     */
    default Order commitBuyOrder(UserConf conf, String buy) {
        return null;
    }

    /**
     * 撤单
     *
     * @param conf
     * @param orderId 订单id
     * @param type    买入，卖出
     */
    default String cancelOrder(UserConf conf, String orderId, String type) {
        return null;
    }

    /**
     * 订单信息
     *
     * @param conf    配置
     * @param orderId 订单id
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

    /**
     * 获取持仓信息
     *
     * @param user
     */
    default HoldInfo getHoldInfo(User user) {
        return null;
    }
}
