package com.xinaml.robot.common.thread;

import com.xinaml.robot.common.utils.DateUtil;
import com.xinaml.robot.common.utils.MailUtil;
import com.xinaml.robot.entity.user.UserConf;
import com.xinaml.robot.ser.okex.AutoTradeSer;
import com.xinaml.robot.vo.user.HoldInfo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;

/**
 * @Author: [lgq]
 * @Date: [19-7-9 上午11:07]
 * @Description:止损卖出线程
 * @Version: [1.0.0]
 * @Copy: [com.xinaml]
 */

public class SellThread extends Thread {
    private static Logger LOG = LoggerFactory.getLogger(SellThread.class);
    private UserConf conf;
    private Double last;
    private AutoTradeSer autoTradeSer;

    public SellThread(UserConf conf, Double last, AutoTradeSer autoTradeSer) {
        this.conf = conf;
        this.last = last;
        this.autoTradeSer = autoTradeSer;
    }

    @Override
    public void run() {
        try {
            handleLoss(conf, last);//止损
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 止损
     *
     * @param conf
     * @param last 最新成交价
     */
    private void handleLoss(UserConf conf, Double last) {
        HoldInfo info = autoTradeSer.getHoldInfo(conf);
        Integer count = StringUtils.isNotBlank(info.getLong_avail_qty()) ? Integer.parseInt(info.getLong_avail_qty()) : 0;//剩余张数
        if (StringUtils.isNotBlank(info.getLong_pnl())) {//多仓收益
            double profit = Double.parseDouble(info.getLong_pnl_ratio()) * 100; //负数为亏损,多仓收益率
            double loss = conf.getLoss();
            if (0 > profit && Math.abs(profit) >= loss) { //如果设定的损值大于实际的损值,profit少于0的时候就是亏损了
                if (count > 0) {
                    autoTradeSer.commitSellOrder(conf, count);//全部卖出
                    String email = conf.getUser().getEmail();
                    if (StringUtils.isNotBlank(email)) {
                        String msg = DateUtil.dateToString(LocalDateTime.now()) + "亏损率达到设定值， 已全部平仓卖出！" + "卖出张数为：" + count;
                        LOG.info(msg);
                        MailUtil.send(email, "亏损率达到设定值" + loss + ",平仓卖出！", msg);
                    }
                }

            }
        }
        //卖出价>最新价
//        if (StringUtils.isNotBlank(info.getLong_avg_cost())) {//开仓平均价不为空
//            Double sellPrice = Double.parseDouble(info.getLong_avg_cost()) * conf.getSelfMultiple();//卖出价=开仓平均价*卖出倍率
//            if (sellPrice > last) { //当前价>=最新价
//                if (count > 0) {
//                    autoTradeSer.commitSellOrder(conf, count);//全部卖出
//                    String email = conf.getUser().getEmail();
//                    if (StringUtils.isNotBlank(email)) {
//                        String msg = DateUtil.dateToString(LocalDateTime.now()) + "当前价:" + sellPrice + "大于等于最新价:" + last + "， 已全部平仓卖出！" + "卖出张数为：" + count;
//                        LOG.info(msg);
//                        MailUtil.send(email, "当前价大于等于最新价，平仓卖出!", msg);
//                    }
//                }
//
//            }
//        }
        //多仓收益,达到设置阀，全部卖出
        if (StringUtils.isNotBlank(info.getLong_pnl())) {//多仓收益
            double profit = Double.parseDouble(info.getLong_pnl_ratio()) * 100; //多仓收益率,负数为亏损
            if (null != conf.getProfit() && profit >= conf.getProfit()) {//多仓收益达到设置阀，全部卖出
                if (count > 0) {
                    autoTradeSer.commitSellOrder(conf, count);//全部卖出
                    String email = conf.getUser().getEmail();
                    if (StringUtils.isNotBlank(email)) {
                        String msg = DateUtil.dateToString(LocalDateTime.now()) + "收益率达到" + profit + "， 已全部平仓卖出！" + "卖出张数为：" + count;
                        LOG.info(msg);
                        MailUtil.send(email, "收益率达到" + profit + "平仓卖出!", msg);
                    }
                }
            }
        }
    }

}
