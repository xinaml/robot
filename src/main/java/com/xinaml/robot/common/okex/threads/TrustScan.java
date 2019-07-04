package com.xinaml.robot.common.okex.threads;

import com.alibaba.fastjson.JSON;
import com.xinaml.robot.base.rep.RedisRep;
import com.xinaml.robot.entity.order.Order;
import com.xinaml.robot.entity.user.UserConf;
import com.xinaml.robot.ser.okex.AutoTradeSer;
import com.xinaml.robot.ser.order.OrderSer;
import com.xinaml.robot.ser.user.UserConfSer;
import com.xinaml.robot.vo.user.OrderInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @Author: [lgq]
 * @Date: [19-6-14 上午10:38]
 * @Description: 委托订单扫描
 * @Version: [1.0.0]
 * @Copy: [com.xinaml]
 */
@Component
public class TrustScan {
    @Autowired
    private UserConfSer userConfSer;
    @Autowired
    private AutoTradeSer autoTradeSer;
    @Autowired
    private OrderSer orderSer;
    @Autowired
    private RedisRep redisRep;

    @PostConstruct
    public void init() {
        cancelBuyOrder();//撤销买入订单
        cancelSellOrder();//撤销卖出订单
    }

    /**
     * 每分钟查询未完成买入订单以便撤单
     */
    private void cancelBuyOrder() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    List<Order> orders = orderSer.findBuyUnSuccess("");
                    if (null != orders && orders.size() > 0) {
                        for (Order order : orders) {
                            String userId = order.getUid();
                            String s = redisRep.get(userId + "orders");//redis取配置信息
                            UserConf conf = null;
                            if (StringUtils.isNotBlank(s)) {
                                conf = JSON.parseObject(s, UserConf.class);
                            } else {
                                conf = userConfSer.findByUserId(userId);
                            }
                            String rs = autoTradeSer.cancelOrder(conf, order.getOrderId(), "买入");//撤单
                            if (rs.indexOf("撤单失败") != 1) {//撤单失败，说明成交成功了
                                order.setStatus(2);
                                orderSer.update(order);
                            } else {//撤单成功，直接删除数据
                                orderSer.remove(order);
                            }
                            OrderInfo info = autoTradeSer.getOrderInfo(conf, order.getOrderId());
                            if (null == info) {
                                info = autoTradeSer.getOrderInfo(conf, order.getOrderId());
                                if (null == info) {
                                    orderSer.remove(order);
                                }
                            }
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 0, 60000); //每分钟查询一次未完成的订单
    }

    /**
     * 每分钟查询未完成卖出订单以便撤单
     */
    private void cancelSellOrder() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    List<Order> orders = orderSer.findSellUnSuccess("");
                    if (null != orders && orders.size() > 0) {
                        for (Order order : orders) {
                            String userId = order.getUid();
                            String s = redisRep.get(userId + "orders");//redis取配置信息
                            UserConf conf = null;
                            if (StringUtils.isNotBlank(s)) {
                                conf = JSON.parseObject(s, UserConf.class);
                            } else {
                                conf = userConfSer.findByUserId(userId);
                            }
                            String rs = autoTradeSer.cancelOrder(conf, order.getOrderId(), "卖出");//撤单
                            if (rs.indexOf("撤单失败") != 1) {//撤单失败，说明成交成功了
                                order.setStatus(2);
                                orderSer.update(order);
                            } else {//撤单成功，直接删除数据
                                orderSer.remove(order);
                            }
                            OrderInfo info = autoTradeSer.getOrderInfo(conf, order.getOrderId());
                            if (null == info) {
                                info = autoTradeSer.getOrderInfo(conf, order.getOrderId());
                                if (null == info) {
                                    orderSer.remove(order);
                                }
                            }
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 0, 60000); //每分钟查询一次未完成的订单
    }


}
