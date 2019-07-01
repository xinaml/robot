package com.xinaml.robot.ser.okex;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xinaml.robot.common.okex.Client;
import com.xinaml.robot.common.utils.DateUtil;
import com.xinaml.robot.common.utils.MailUtil;
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

    @Override
    public void trade(UserConf conf) {
        KLine line = getLine(conf);
        Double last = getLast(conf); //最新成交价
        if (line != null && last != null) {
            double buy = conf.getBuyMultiple() * line.getClose();//买入价=买入价倍率*收盘价
            if (buy >= last) {//买入价>=最新成交价
                LOG.info("buy:" + buy + "---last:" + last);
                commitOrder(conf, buy + ""); //提交订单
                String orderId = commitOrder(conf, buy + ""); //提交订单
            }

        }
        cancelOrder(conf, "RB318517");
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
        if (rs.indexOf("instrument_id") != -1) {//获取订单成功
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
    public void cancelOrder(UserConf conf, String orderId) {
        String url = CANCEL_ORDER + "/" + conf.getInstrumentId() + "/" + orderId;
        String rs = Client.httpPost(url, JSON.toJSONString(new OrderVO()), conf.getUser());
        if (rs.indexOf("\"error_code\":\"0\"") != -1) {//撤单成功
            Order order = orderSer.findByOrderId(orderId);
            orderSer.remove(order);
            String email = conf.getUser().getEmail();
            if (StringUtils.isNotBlank(email)) {
                String msg = DateUtil.dateToString(LocalDateTime.now()) + " 撤单成功！" + "单号id为：" + orderId;
                MailUtil.send(email, "撤单成功！", msg);
            }
        } else {
            LOG.warn(conf.getUser().getUsername() + ":" + rs);
        }
    }

    /**
     * 下单
     *
     * @param conf
     */
    public String commitOrder(UserConf conf, String buy) {
        String url = COMMIT_ORDER;
        OrderVO orderVO = new OrderVO();
        orderVO.setInstrument_id(conf.getInstrumentId());//合约id
        orderVO.setSize(conf.getCount() + "");//每次开张数
        orderVO.setPrice(buy);
        orderVO.setLeverage(conf.getLeverage() + "");
        String rs = Client.httpPost(url, JSON.toJSONString(orderVO), conf.getUser());
        if (rs.indexOf("\"error_code\":\"0\"") != -1) {//下单成功
            OrderCommitVO oc = JSON.parseObject(rs, OrderCommitVO.class);
            Order order = new Order();
            order.setClientOid(oc.getClient_oid());
            order.setUser(conf.getUser());
            order.setOrderId(oc.getOrder_id());
            order.setErrorCode(oc.getError_code());
            order.setErrorMessage(oc.getError_message());
            order.setInstrumentId(conf.getInstrumentId());
            order.setCreateDate(LocalDateTime.now());
            orderSer.save(order);
            String email = conf.getUser().getEmail();
            if (StringUtils.isNotBlank(email)) {
                String msg = DateUtil.dateToString(LocalDateTime.now()) + " 下单成功！" + "单号id为：" + oc.getOrder_id();
                MailUtil.send(email, "下单成功！", msg);
            }
            return order.getOrderId();
        } else {
            LOG.warn(conf.getUser().getUsername() + "下单失败:" + rs);//下单失败
        }
        return null;
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
        if (rs.length() > 5) {
            JSONObject object = JSON.parseObject(rs);
            return object.getDouble("last");
        } else {
            return null;
        }
    }

}
