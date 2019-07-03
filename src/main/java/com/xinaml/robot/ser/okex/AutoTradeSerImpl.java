package com.xinaml.robot.ser.okex;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
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
import com.xinaml.robot.vo.user.KLine;
import com.xinaml.robot.vo.user.OrderCommitVO;
import com.xinaml.robot.vo.user.OrderInfo;
import com.xinaml.robot.vo.user.OrderVO;
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
            LOG.debug("buy:" + buy + "---last:" + last);
            String msg = "买入价为:" + buy + ",最新成交价为:" + last;
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
                if(null==time || !(line.getTimestamp()).equals(time)){
                    commitBuyOrder(conf, buy + ""); //提交订单
                    PriceSession.put(userId, line.getTimestamp());
                }

                System.out.println(msg);
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
     * @param last
     */
    private void checkSell(UserConf conf, Double last) {
        List<Order> orders = orderSer.findBuySuccess(conf.getUser().getId());
        for (Order order : orders) {
            double sell = Double.parseDouble(order.getPrice()) * conf.getSelfMultiple();////卖出价=买入价*卖出价倍率
            if (last >= sell) {
                Order sOrder = commitSellOrder(conf);//卖出
                if (sOrder != null && sOrder.getErrorCode().equals("0")) {
                    order.setProfit(StringUtil.formatDouble(last - Double.parseDouble(order.getPrice()) ));//设置盈利
                    order.setSellId(sOrder.getOrderId());//设置卖出id
                    orderSer.update(order);
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
                order.setCreateDate(LocalDateTime.now());
                order.setType(2);//卖出
                OrderInfo info = getOrderInfo(conf,oc.getOrder_id());
                if(null!=info){
                    order.setStatus(Integer.parseInt(info.getState()));//已成交
                }
                orderSer.save(order);
                String email = conf.getUser().getEmail();
                if (StringUtils.isNotBlank(email)) {
                    String msg = DateUtil.dateToString(LocalDateTime.now()) + " 卖出下单成功！" + "单号id为：" + oc.getOrder_id();
                    MailUtil.send(email, "卖出下单成功！", msg);
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
        orderVO.setPrice(buy);
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
                order.setCreateDate(LocalDateTime.now());
                order.setPrice(buy);
                order.setType(1);//买入
                OrderInfo info = getOrderInfo(conf,oc.getOrder_id());
                if(null!=info){
                    order.setStatus(Integer.parseInt(info.getState()));//已成交
                }
                orderSer.save(order);
                String email = conf.getUser().getEmail();
                if (StringUtils.isNotBlank(email)) {
                    String msg = DateUtil.dateToString(LocalDateTime.now()) + " 买入下单成功！" + "单号id为：" + oc.getOrder_id();
                    MailUtil.send(email, "买入下单成功！", msg);
                }
                return order;
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
        if (null!=rs && rs.indexOf("instrument_id") != -1) {//获取订单成功
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
        if (null!=rs && rs.indexOf("\"error_code\":\"0\"") != -1) {//撤单成功
            order.setStatus(-1);
            String email = conf.getUser().getEmail();
            if (StringUtils.isNotBlank(email)) {
                String msg = DateUtil.dateToString(LocalDateTime.now()) + " " + type + "订单撤单成功！" + "单号id为：" + orderId;
                MailUtil.send(email, type + "订单撤单成功！", msg);
            }
            rsMsg = "撤单成功！";
        } else {
            OrderInfo info = getOrderInfo(conf,orderId);
            if(null!=info){
                order.setStatus(Integer.parseInt(info.getState()));//已成交
            }
            LOG.warn("撤单失败！ " + conf.getUser().getUsername() + ":" + rs);
            rsMsg = "撤单失败！ " + rs;
        }
        orderSer.update(order);
        return rsMsg;
    }


    /**
     * 获取k线数据
     *
     * @param conf
     * @return
     */
    public KLine getLine(UserConf conf) {
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
        String rs = Client.httpGet(lastUrl, conf.getUser());
        if (rs != null && rs.length() > 5) {
            JSONObject object = JSON.parseObject(rs);
            return object.getDouble("last");
        } else {
            return null;
        }
    }

}
