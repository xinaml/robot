package com.xinaml.robot.common.thread;

import com.xinaml.robot.common.okex.utils.DateUtils;
import com.xinaml.robot.common.utils.DateUtil;
import com.xinaml.robot.common.utils.MailUtil;
import com.xinaml.robot.common.utils.StringUtil;
import com.xinaml.robot.entity.user.UserConf;
import com.xinaml.robot.ser.okex.AutoTradeSer;
import com.xinaml.robot.vo.user.HoldInfo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author: [lgq]
 * @Date: [19-7-9 上午11:07]
 * @Description:达到收益值卖出线程
 * @Version: [1.0.0]
 * @Copy: [com.xinaml]
 */

public class ProfitSellThread extends Thread {
    private static Logger LOG = LoggerFactory.getLogger(ProfitSellThread.class);
    private UserConf conf;
    private AutoTradeSer autoTradeSer;

    public ProfitSellThread(UserConf conf, AutoTradeSer autoTradeSer) {
        this.conf = conf;
        this.autoTradeSer = autoTradeSer;
    }

    @Override
    public void run() {
        try {
            profitSell(conf);//达到收益值卖出
        } catch (Exception e) {
            LOG.error(e.getMessage());
        }
    }

    /**
     * 达到收益值卖出
     *
     * @param conf
     */
    private void profitSell(UserConf conf) {
        HoldInfo info = autoTradeSer.getHoldInfo(conf);
        Integer count = StringUtils.isNotBlank(info.getLong_avail_qty()) ? Integer.parseInt(info.getLong_avail_qty()) : 0;//剩余张数
        //多仓收益,达到设置阀，全部卖出
        if (StringUtils.isNotBlank(info.getLong_pnl())) {//多仓收益
            double profit = Double.parseDouble(info.getLong_pnl_ratio()) * 100; //多仓收益率,负数为亏损
            if (null != conf.getProfit() && profit >= conf.getProfit()) {//多仓收益达到设置阀，全部卖出
                if (count > 0) {
                    autoTradeSer.commitSellOrder(conf, count);//全部卖出
                    String email = conf.getUser().getEmail();
                    if (StringUtils.isNotBlank(email)) {
                        String msg = DateUtil.now()+ ":收益率为" + StringUtil.formatDouble(profit) + "%，达到设定值" + conf.getProfit() + "%，已全部平仓卖出！" + "卖出张数为：" + count;
                        LOG.info(msg);
                        MailUtil.send(email, "收益率达到" + StringUtil.formatDouble(profit) + "平仓卖出!", msg);
                    }
                }
            }
        }
    }

}
