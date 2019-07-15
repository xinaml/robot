package com.xinaml.robot.ser.okex;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xinaml.robot.common.constant.UrlConst;
import com.xinaml.robot.common.okex.Client;
import com.xinaml.robot.common.session.MsgSession;
import com.xinaml.robot.common.session.PriceSession;
import com.xinaml.robot.common.thread.LossSellThread;
import com.xinaml.robot.common.thread.ProfitSellThread;
import com.xinaml.robot.common.utils.DateUtil;
import com.xinaml.robot.common.utils.MailUtil;
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
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

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
        if (null != last) {
            new Thread(new ProfitSellThread(conf, this))
                    .start();//收益卖出线程
            new Thread(new LossSellThread(conf, this))
                    .start();//止损卖出线程
        }
        if (line != null && last != null) { //买入，单张卖出
            double buy = conf.getBuyMultiple() * line.getClose();//买入价=买入价倍率*收盘价
            String userId = conf.getUser().getId();
            if (buy >= last && !conf.getOnlySell()) {//买入价>=最新成交价 且 如果非只卖出，可以继续买入下单 买入
                String time = PriceSession.get(userId);//上次成交价
                if (null == time || !(line.getTimestamp()).equals(time)) {
                    if (conf.getBuyVal() == null) { //没有设置买入阀值，直接买入
                        commitBuyOrder(conf, buy + "", conf.getCount()); //提交订单
                    } else {//设置了阀值
                        HoldInfo info = getHoldInfo(conf);
                        String avg = info.getLong_avg_cost();//开仓均价
                        //
                        int size = StringUtils.isNotBlank(info.getLong_qty()) ? Integer.parseInt(info.getLong_qty()) : 0;
                        //张数>0 并且 最新价-开仓均价 >= 买入阀值
                        if (size > 0 && StringUtils.isNotBlank(avg) && last - Double.parseDouble(avg) >= conf.getBuyVal()) {
                            commitBuyOrder(conf, buy + "", conf.getCount()); //提交订单
                        }
                        //如果持仓没有张数，即使设置了阀值，也买入
                        if (size == 0) {
                            commitBuyOrder(conf, buy + "", conf.getCount()); //提交订单
                        }
                    }
                    PriceSession.put(userId, line.getTimestamp());
                }

            }
            checkSell(conf, last);//检测卖出
            String msg = "收盘价为:" + line.getClose() + ",最新成交价为:" + last;
            String old = MsgSession.get(userId);
            if (!msg.equals(old)) {
                if (old == null) {
                    MsgSession.put(userId, msg);
                }
                webSocketServer.sendMessage(userId, msg);
            } else {
                MsgSession.put(userId, msg);
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
        HoldInfo info = getHoldInfo(conf);
        Integer count = StringUtils.isNotBlank(info.getLong_avail_qty()) ? Integer.parseInt(info.getLong_avail_qty()) : 0;//剩余张数
        List<Order> orders = orderSer.findBuySuccess(conf.getUser().getId());
        for (Order order : orders) {
            double buy = Double.parseDouble(order.getPrice());
            double sell = buy * conf.getSelfMultiple();////卖出价=买入价*卖出价倍率
            if (last >= sell && count == 1) { //如果张数为1时才卖出，张树大于等于2时，看整体收益率卖出
                Order sOrder = commitSellOrder(conf, conf.getCount());//卖出
                if (sOrder != null && sOrder.getStatus() == 2) {//卖出成功
                    orderSer.remove(order);//删除买入订单
                    String email = conf.getUser().getEmail();
                    if (StringUtils.isNotBlank(email)) {//发送邮件
                        String msg = DateUtil.now() + ": 卖出下单成功！" + "单号id为：" + sOrder.getOrderId() + ",卖出张数为：" + count;
                        MailUtil.send(email, "卖出下单成功！", msg);
                    }
                } else {//找不到订单
                    orderSer.remove(order);
                }
            }


        }
        if (count == 0) {//平台上没有张数了。删除本地未完成订单
            Order[] list = new Order[orders.size()];
            int oIndex = 0;
            for (Order order : orders) {
                list[oIndex++] = order;
            }
            orderSer.remove(list);
        }
    }


    /**
     * 卖出，下单
     *
     * @param conf
     * @return
     */
    @Transactional
    @Override
    public Order commitSellOrder(UserConf conf, Integer size) {
        String url = COMMIT_ORDER;
        OrderVO orderVO = new OrderVO();
        orderVO.setInstrument_id(conf.getInstrumentId());//合约id
        orderVO.setSize(size + "");//每次开张数
        orderVO.setType(conf.getUp()==true?"3":"4");
        orderVO.setMatch_price("1");
        orderVO.setLeverage(conf.getLeverage() + "");
        String rs = Client.httpPost(url, JSON.toJSONString(orderVO), conf.getUser());
        if (rs.indexOf("\"error_code\":\"0\"") != -1) {//下单成功
            OrderCommitVO oc = JSON.parseObject(rs, OrderCommitVO.class);
            if (oc.getOrder_id() != "-1") {//保存成功订单
                Order order = new Order();
                order.setClientOid(oc.getClient_oid());
                order.setUser(conf.getUser());
                order.setOrderId(oc.getOrder_id());
                order.setErrorCode(oc.getError_code());
                order.setErrorMessage(oc.getError_message());
                order.setInstrumentId(conf.getInstrumentId());
                order.setSellId(UUID.randomUUID().toString());
                order.setSize(size + "");
                order.setUid(conf.getUser().getId());
                order.setCreateDate(LocalDateTime.now());
                order.setType(2);//卖出
                order.setSellDate(LocalDateTime.now());
                OrderInfo info = getOrderInfo(conf, oc.getOrder_id());
                if (null != info) {
                    if (info.getState().equals("2")) {
                        order.setStatus(2);//已成交
                    } else {
                        order.setStatus(1);
                    }
                }
                orderSer.save(order);
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
    @Transactional
    @Override
    public Order commitBuyOrder(UserConf conf, String buy, Integer size) {
        String url = COMMIT_ORDER;
        OrderVO orderVO = new OrderVO();
        orderVO.setInstrument_id(conf.getInstrumentId());//合约id
        orderVO.setSize(size + "");//每次开张数
        orderVO.setPrice(buy);//买入价格
        orderVO.setType(conf.getUp()==true?"1":"2");
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
                order.setSize(size + "");
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
                String msg = DateUtil.now() + ":" + type + "订单撤单成功！" + "单号id为：" + orderId;
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
