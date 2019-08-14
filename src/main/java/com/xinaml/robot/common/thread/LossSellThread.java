package com.xinaml.robot.common.thread;

import com.xinaml.robot.base.rep.RedisRep;
import com.xinaml.robot.common.utils.DateUtil;
import com.xinaml.robot.common.utils.MailUtil;
import com.xinaml.robot.common.utils.StringUtil;
import com.xinaml.robot.entity.user.UserConf;
import com.xinaml.robot.ser.okex.AutoTradeSer;
import com.xinaml.robot.vo.user.HoldInfo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * @Author: [lgq]
 * @Date: [19-7-9 上午11:07]
 * @Description:止损卖出线程
 * @Version: [1.0.0]
 * @Copy: [com.xinaml]
 */

public class LossSellThread extends Thread {
    private static Logger LOG = LoggerFactory.getLogger(LossSellThread.class);
    private UserConf conf;
    private AutoTradeSer autoTradeSer;
    private RedisRep redisRep;

    public LossSellThread(UserConf conf, AutoTradeSer autoTradeSer,RedisRep redisRep) {
        this.conf = conf;
        this.autoTradeSer = autoTradeSer;
        this.redisRep = redisRep;
    }

    @Override
    public void run() {
        try {
            lossSell(conf);//止损
        } catch (Exception e) {
            LOG.error(e.getMessage());
        }
    }


    /**
     * 止损卖出
     *
     * @param conf
     */
    private void lossSell(UserConf conf) {
        HoldInfo info = autoTradeSer.getHoldInfo(conf);
        String type = "0";
        if (conf.getUp() == true) {
            type = "3";//平多
            String countStr = info.getLong_avail_qty();
            String profitStr = info.getLong_pnl_ratio();
            Integer count = StringUtils.isNotBlank(countStr) ? Integer.parseInt(countStr) : 0;//剩余张数
            sell(profitStr, count, type);
        }
        if (conf.getDown() == true) {
            type = "4";//平空
            String countStr = info.getShort_avail_qty();
            String profitStr = info.getShort_pnl_ratio();
            Integer count = StringUtils.isNotBlank(countStr) ? Integer.parseInt(countStr) : 0;//剩余张数
            sell(profitStr, count, type);
        }

    }

    private void sell(String profitStr, Integer count, String type) {
        if (StringUtils.isNotBlank(profitStr)) {//多仓收益
            double profit = Double.parseDouble(profitStr) * 100; //负数为亏损,多仓收益率
            //profit-50
            double loss = type.equals("3") ? conf.getLoss() : conf.getDownLoss();//50
            if (0 > profit && Math.abs(profit) >= loss) { //如果设定的损值大于实际的损值,profit少于0的时候就是亏损了
                if (count > 0 && type != null) {
                    autoTradeSer.commitSellOrder(conf, count, type);//全部卖出
                    String email = conf.getUser().getEmail();
                    if (StringUtils.isNotBlank(email)) {
                        String t = type.equals("3") ? "平多" : "平空";
                        String msg = DateUtil.now() + ":亏损率为" + StringUtil.formatDouble(profit) + "%，达到设定值" + StringUtil.formatDouble(loss) + "%， 已全部" + t + "卖出！" + "卖出张数为：" + count;
                        LOG.info(msg);
                        MailUtil.send(email, "亏损率达到设定值" + StringUtil.formatDouble(loss) + "," + t + "卖出！", msg);
                    }
                    //止损卖出后不再买入
                    String key = conf.getUser().getId()+"orders";
                    redisRep .put(key,"1",conf.getStopTime(), TimeUnit.MINUTES);
                }
            }
        }
    }

}
