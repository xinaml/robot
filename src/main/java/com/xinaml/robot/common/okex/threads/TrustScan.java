package com.xinaml.robot.common.okex.threads;

import com.xinaml.robot.entity.order.Order;
import com.xinaml.robot.entity.user.UserConf;
import com.xinaml.robot.ser.okex.AutoTradeSer;
import com.xinaml.robot.ser.order.OrderSer;
import com.xinaml.robot.ser.user.UserConfSer;
import com.xinaml.robot.vo.user.OrderInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
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
                            UserConf conf = userConfSer.findByUserId(order.getUser().getId());
                            OrderInfo info = autoTradeSer.getOrderInfo(conf, order.getOrderId());
                            if (null != info) {
                                //开始判断订单状态
                                if (!info.getState().equals("0")) {
                                    order.setStatus(Integer.parseInt(info.getState()));//
                                    orderSer.update(order);
                                } else {//如果状态是0，代表没有撤销
                                    int minutes = conf.getOrderTime() != null ? conf.getOrderTime() : 0;
                                    if (order.getCreateDate().plusMinutes(minutes).isAfter(LocalDateTime.now())) {//订单超时，撤销
                                        autoTradeSer.cancelOrder(conf, order.getOrderId(), "买入");
                                        info = autoTradeSer.getOrderInfo(conf, order.getOrderId());//撤单后重查状态
                                        order.setStatus(Integer.parseInt(info.getState()));//
                                        orderSer.update(order);//更新订单状态
                                    }
                                }

                            } else {//找不到订单，删除本地的
                                orderSer.remove(order);
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
                            UserConf conf = userConfSer.findByUserId(order.getUser().getId());
                            OrderInfo info = autoTradeSer.getOrderInfo(conf, order.getOrderId());
                            if (null != info) {
                                //开始判断订单状态
                                if (!info.getState().equals("0")) {
                                    order.setStatus(Integer.parseInt(info.getState()));//
                                    orderSer.update(order);
                                } else {//如果状态是0，代表没有撤销
                                    int minutes = conf.getSellTime() != null ? conf.getSellTime() : 0;
                                    if (order.getCreateDate().plusMinutes(minutes).isAfter(LocalDateTime.now())) {//订单超时，撤销
                                        autoTradeSer.cancelOrder(conf, order.getOrderId(), "卖出");
                                        info = autoTradeSer.getOrderInfo(conf, order.getOrderId());//撤单后重查状态
                                        order.setStatus(Integer.parseInt(info.getState()));//
                                        orderSer.update(order);//更新订单状态
                                    }
                                }

                            } else {//找不到订单，删除本地的
                                orderSer.remove(order);
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
