package com.xinaml.robot.common.okex.threads;

import com.alibaba.fastjson.JSON;
import com.xinaml.robot.base.rep.RedisRep;
import com.xinaml.robot.entity.order.Order;
import com.xinaml.robot.entity.user.User;
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
 * @Date: [19-6-26 下午2:24]
 * @Description:启动时开始用户交易扫描
 * @Version: [1.0.0]
 * @Copy: [com.xinaml]
 */
@Component
public class StartInit {
    @Autowired
    private UserConfSer userConfSer;
    @Autowired
    private RedisRep redisRep;
    @Autowired
    private AutoTradeSer autoTradeSer;
    @Autowired
    private OrderSer orderSer;

    @PostConstruct
    public void start() {
        List<UserConf> confs = userConfSer.findAll();
        for (UserConf conf : confs) {
            User user = conf.getUser();
            if (!user.getStop()) {
                userConfSer.setSeconds(conf);
                redisRep.put(user.getId() + user.getUsername(), JSON.toJSONString(conf));//保存配置信息到redis
                ThreadScan.scan(user.getId(), false, conf);
            }
        }
        cancelOrder();
    }

    /**
     * 查询未完成订单以便撤单
     */
    private void cancelOrder() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    List<Order> orders = orderSer.findUnSuccess();
                    if (null != orders && orders.size() > 0) {
                        for (Order order : orders) {
                            UserConf conf = userConfSer.findByUserId(order.getUser().getId());
                            OrderInfo info = autoTradeSer.getOrderInfo(conf,order.getOrderId());
                            if(null!=info){
                                 //开始判断订单状态
                                if(!info.getState().equals("0")){
                                    order.setStatus(Integer.parseInt(info.getState()));//
                                    orderSer.update(order);
                                }else {//如果状态是0，代表没有撤销
                                    int minutes = conf.getOrderTime()!=null?conf.getOrderTime():0;
                                    if(order.getCreateDate().plusMinutes(minutes).isAfter(LocalDateTime.now())){//订单超时，撤销
                                        autoTradeSer.cancelOrder(conf,order.getOrderId());
                                        info = autoTradeSer.getOrderInfo(conf,order.getOrderId());//撤单后重查状态
                                        order.setStatus(Integer.parseInt(info.getState()));//
                                        orderSer.update(order);//更新订单状态
                                    }
                                }

                            }else {//找不到订单，删除本地的
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
