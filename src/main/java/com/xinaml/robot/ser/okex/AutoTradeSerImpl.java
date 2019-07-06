package com.xinaml.robot.ser.okex;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xinaml.robot.common.constant.UrlConst;
import com.xinaml.robot.common.okex.Client;
import com.xinaml.robot.common.session.MsgSession;
import com.xinaml.robot.common.session.PriceSession;
import com.xinaml.robot.common.utils.DateUtil;
import com.xinaml.robot.common.utils.MailUtil;
import com.xinaml.robot.common.utils.StringUtil;
import com.xinaml.robot.common.webscoket.WebSocketServer;
import com.xinaml.robot.entity.order.Order;
import com.xinaml.robot.entity.user.UserConf;
import com.xinaml.robot.ser.order.OrderSer;
import com.xinaml.robot.vo.user.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

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
    @Autowired
    private OrderSer orderSer;
    private static Logger LOG = LoggerFactory.getLogger(AutoTradeSer.class);
    @Autowired
    private WebSocketServer webSocketServer;

    @Override
    public void trade(UserConf conf) {
        KLine line = getLine(conf);
        Double last = getLast(conf); //最新成交价
        if (line != null && last != null) {
            double buy = conf.getBuyMultiple() * line.getClose();//买入价=买入价倍率*收盘价
            String msg = "收盘价为:" + line.getClose() + ",最新成交价为:" + last;
            String userId = conf.getUser().getId();
            String old = MsgSession.get(userId);
            if (!msg.equals(old)) {
                if (old == null) {
                    MsgSession.put(userId, msg);
                }
                webSocketServer.sendMessage(userId, msg);
            } else {
                MsgSession.put(userId, msg);
            }
            if (buy >= last) {//买入价>=最新成交价
                String time = PriceSession.get(userId);//上次成交价
                if (null == time || !(line.getTimestamp()).equals(time)) {
                    if (!conf.getOnlySell()) {//如果非只卖出，可以继续下单
                        commitBuyOrder(conf, buy + ""); //提交订单
                    }
                    PriceSession.put(userId, line.getTimestamp());
                }

            }
            if (null != last) {
                checkSell(conf, last);
            }

        }
    }

    /**
     * 检测是否可以卖出
     *
     * @param conf
     * @param last 最新成交价
     */
    private void checkSell(UserConf conf, Double last) {
        List<Order> orders = orderSer.findBuySuccess(conf.getUser().getId());
        for (Order order : orders) {
            double buy = Double.parseDouble(order.getPrice());
            double sell = buy * conf.getSelfMultiple();////卖出价=买入价*卖出价倍率
            if (last >= sell) {
                Order sOrder = commitSellOrder(conf);//卖出
                if (sOrder != null && sOrder.getStatus() == 2) {
                    order.setProfit(StringUtil.formatDouble(last - Double.parseDouble(order.getPrice())));//设置盈利
                    order.setSellId(sOrder.getId());//设置卖出id
                    order.setSell(sell + "");//设置卖出价
                    order.setSellDate(LocalDateTime.now());//设置卖出时间
                    orderSer.update(order);
                }
            }
            OrderInfo orderInfo = getOrderInfo(conf, order.getOrderId());
            if (null != orderInfo && orderInfo.getState() == "2") {//线上已经卖出了
                orderSer.remove(order);
            } else if (null == orderInfo) {//找不到订单
                orderSer.remove(order);
            }

        }
        handleLoss(conf, orders, last);//止损
    }

    /**
     * 止损
     *
     * @param conf
     * @param orders
     * @param last   最新成交价
     */
    private void handleLoss(UserConf conf, List<Order> orders, Double last) {
        HoldInfo info = getHoldInfo(conf);
        if (StringUtils.isNotBlank(info.getLong_pnl_ratio()) && StringUtils.isNotBlank(info.getLeverage())) {
            double profit = Double.parseDouble(info.getLong_pnl_ratio()) * Double.parseDouble(info.getLeverage()); //负数为亏损
            profit = Math.abs(profit);
            double loss = conf.getLoss();
            if (0 > profit && loss > profit) { //如果设定的损值大于实际的损值,profit少于0的时候就是亏损了
                if (StringUtils.isNotBlank(info.getLong_avail_qty())) {//获取平仓数
                    if(Integer.parseInt(info.getLong_avail_qty())>0){
                        conf.setCount(Integer.parseInt(info.getLong_avail_qty()));
                        commitSellOrder(conf);//全部卖出
                        Order[] list = new Order[orders.size()];
                        int index = 0;
                        for (Order order : orders) {
                            list[index++] = order;
                        }
                        orderSer.remove(list);
                        String email = conf.getUser().getEmail();
                        if (StringUtils.isNotBlank(email)) {
                            String msg = DateUtil.dateToString(LocalDateTime.now()) + "亏损率达到设定值， 已全部平仓卖出！" + "卖出张数为：" + conf.getCount();
                            LOG.info(msg);
                            MailUtil.send(email, "亏损率达到设定值"+loss+",平仓卖出！", msg);
                        }
                    }

                }
            }
        }
        //卖出价>最新价
        if (StringUtils.isNotBlank(info.getLong_avg_cost())) {//开仓平均价不为空
            Double sellPrice = Double.parseDouble(info.getLong_avg_cost()) * conf.getSelfMultiple();//卖出价=开仓平均价*卖出倍率
            if (sellPrice > last && 0>1) { //当前价>=最新价
                if(StringUtils.isNotBlank(info.getLong_avail_qty())){
                    Integer count = Integer.parseInt(info.getLong_avail_qty());
                    if(count>0){
                        conf.setCount(count);//获取平仓数
                        commitSellOrder(conf);//全部卖出
                        Order[] list = new Order[orders.size()];
                        int oIndex = 0;
                        for (Order order : orders) {
                            list[oIndex++] = order;
                        }
                        orderSer.remove(list);
                        String email = conf.getUser().getEmail();
                        if (StringUtils.isNotBlank(email)) {
                            String msg = DateUtil.dateToString(LocalDateTime.now()) + "当前价达到卖出价， 已全部平仓卖出！" + "卖出张数为：" + conf.getCount();
                            LOG.info(msg);
                            MailUtil.send(email, "当前价达到卖出价，平仓卖出!", msg);
                        }
                    }

                }

            }
        }

    }

    /**
     * 卖出，下单
     *
     * @param conf
     * @return
     */
    @Override
    public Order commitSellOrder(UserConf conf) {
        String url = COMMIT_ORDER;
        OrderVO orderVO = new OrderVO();
        orderVO.setInstrument_id(conf.getInstrumentId());//合约id
        orderVO.setSize(conf.getCount() + "");//每次开张数
        orderVO.setType("3");
        orderVO.setMatch_price("1");
        orderVO.setLeverage(conf.getLeverage() + "");
        String rs = Client.httpPost(url, JSON.toJSONString(orderVO), conf.getUser());
        if (rs.indexOf("\"error_code\":\"0\"") != -1) {//下单成功
            OrderCommitVO oc = JSON.parseObject(rs, OrderCommitVO.class);
            if (oc.getOrder_id() != "-1") {
                Order order = new Order();
                order.setClientOid(oc.getClient_oid());
                order.setUser(conf.getUser());
                order.setOrderId(oc.getOrder_id());
                order.setErrorCode(oc.getError_code());
                order.setErrorMessage(oc.getError_message());
                order.setInstrumentId(conf.getInstrumentId());
                order.setUid(conf.getUser().getId());
                order.setCreateDate(LocalDateTime.now());
                order.setType(2);//卖出
                OrderInfo info = getOrderInfo(conf, oc.getOrder_id());
                if (null != info) {
                    if (info.getState().equals("2")) {
                        order.setStatus(2);//已成交
                    } else {
                        order.setStatus(1);
                    }
                }
                orderSer.save(order);
                if (info.getState().equals("2")) {//卖出成功,发送邮件
                    String email = conf.getUser().getEmail();
                    if (StringUtils.isNotBlank(email)) {
                        String msg = DateUtil.dateToString(LocalDateTime.now()) + " 卖出下单成功！" + "单号id为：" + oc.getOrder_id();
                        MailUtil.send(email, "卖出下单成功！", msg);
                    }
                }
                return order;
            }

        } else {
            LOG.warn(conf.getUser().getUsername() + "卖出下单失败:" + rs);//下单失败
        }
        return null;
    }

    /**
     * 买入，下单
     *
     * @param conf
     */
    public Order commitBuyOrder(UserConf conf, String buy) {
        String url = COMMIT_ORDER;
        OrderVO orderVO = new OrderVO();
        orderVO.setInstrument_id(conf.getInstrumentId());//合约id
        orderVO.setSize(conf.getCount() + "");//每次开张数
        orderVO.setPrice(buy);//买入价格
        orderVO.setType("1");
        orderVO.setLeverage(conf.getLeverage() + "");
        String rs = Client.httpPost(url, JSON.toJSONString(orderVO), conf.getUser());
        if (rs.indexOf("\"error_code\":\"0\"") != -1) {//下单成功
            OrderCommitVO oc = JSON.parseObject(rs, OrderCommitVO.class);
            if (oc.getOrder_id() != "-1") {
                Order order = new Order();
                order.setClientOid(oc.getClient_oid());
                order.setUser(conf.getUser());
                order.setOrderId(oc.getOrder_id());
                order.setErrorCode(oc.getError_code());
                order.setErrorMessage(oc.getError_message());
                order.setInstrumentId(conf.getInstrumentId());
                order.setUid(conf.getUser().getId());
                order.setCreateDate(LocalDateTime.now());
                order.setPrice(buy);
                order.setType(1);//买入
                OrderInfo info = getOrderInfo(conf, oc.getOrder_id());
                if (null != info) {
                    if (info.getState().equals("2")) {
                        order.setStatus(2);//已成交
                    } else {
                        order.setStatus(1);
                    }
                }
                orderSer.save(order);
            }
        } else {
            LOG.warn(conf.getUser().getUsername() + "买入下单失败:" + rs);//下单失败
        }
        return null;
    }


    /**
     * 订单信息
     *
     * @param conf
     * @param orderId
     */
    @Override
    public OrderInfo getOrderInfo(UserConf conf, String orderId) {
        String url = ORDER_INFO + "/" + conf.getInstrumentId() + "/" + orderId;
        String rs = Client.httpGet(url, conf.getUser());
        if (null != rs && rs.indexOf("instrument_id") != -1) {//获取订单成功
            OrderInfo info = JSON.parseObject(rs, OrderInfo.class);
            return info;
        } else {
            return null;
        }
    }

    /**
     * 撤单
     *
     * @param conf
     * @param orderId
     */
    @Override
    public String cancelOrder(UserConf conf, String orderId, String type) {
        type = type.equals("1") ? "买入" : type;
        type = type.equals("2") ? "卖出" : type;
        String url = CANCEL_ORDER + "/" + conf.getInstrumentId() + "/" + orderId;
        String rs = Client.httpPost(url, JSON.toJSONString(new OrderVO()), conf.getUser());
        Order order = orderSer.findByOrderId(orderId);
        String rsMsg = "";
        if (null != rs && rs.indexOf("\"error_code\":\"0\"") != -1) {//撤单成功
            String email = conf.getUser().getEmail();
            if (StringUtils.isNotBlank(email)) {
                String msg = DateUtil.dateToString(LocalDateTime.now()) + " " + type + "订单撤单成功！" + "单号id为：" + orderId;
                MailUtil.send(email, type + "订单撤单成功！", msg);
            }
            rsMsg = "撤单成功！";
            OrderInfo info = getOrderInfo(conf, orderId);
            if (info.getState().equals("-1")) {
                orderSer.remove(order);//撤单的直接删除本地订单
            }
        } else {
            OrderInfo info = getOrderInfo(conf, orderId);
            if (null != info && info.getState().equals("2")) {
                order.setStatus(2);//已成交
                orderSer.update(order);
            } else if (null == info) {
                orderSer.remove(order);
            }
            LOG.warn("撤单失败！ " + conf.getUser().getUsername() + ":" + rs);
            rsMsg = "撤单失败！ " + rs;
        }
        return rsMsg;
    }


    /**
     * 获取k线数据
     *
     * @param conf
     * @return
     */
    public KLine getLine(UserConf conf) {
        try {
            String kLineUrl = K_LINE + "/" + conf.getInstrumentId() + "/candles?start=" + conf.getStartDate() + "&end=" + conf.getEndDate() + "&granularity=" + conf.getSeconds();
            String rs = Client.httpGet(kLineUrl, conf.getUser());
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
                    return kLine;
                }
            } else {
                return null;
            }
        } catch (Exception e) {
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
    public Double getLast(UserConf conf) {
        String lastUrl = LAST + "/" + conf.getInstrumentId() + "/ticker";
        try {
            String rs = Client.httpGet(lastUrl, conf.getUser());
            if (rs != null && rs.length() > 5) {
                JSONObject object = JSON.parseObject(rs);
                return object.getDouble("last");
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }

    }


    /**
     * 获取持仓信息
     *
     * @param conf
     */
    public HoldInfo getHoldInfo(UserConf conf) {
        try {
            String rs = Client.httpGet(UrlConst.HOLD_INFO + conf.getInstrumentId() + "/position", conf.getUser());
            JSONObject ob = JSON.parseObject(rs);
            JSONArray array = (JSONArray) ob.get("holding");
            List<HoldInfo> list = array.toJavaList(HoldInfo.class);
            return list.get(0);
        } catch (Exception e) {
            return new HoldInfo();
        }
    }


}
