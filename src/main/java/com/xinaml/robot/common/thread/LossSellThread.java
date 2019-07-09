package com.xinaml.robot.common.thread;

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
 * @Description:止损卖出线程
 * @Version: [1.0.0]
 * @Copy: [com.xinaml]
 */

public class LossSellThread extends Thread {
    private static Logger LOG = LoggerFactory.getLogger(LossSellThread.class);
    private UserConf conf;
    private AutoTradeSer autoTradeSer;

    public LossSellThread(UserConf conf, AutoTradeSer autoTradeSer) {
        this.conf = conf;
        this.autoTradeSer = autoTradeSer;
    }

    @Override
    public void run() {
        try {
            lossSell(conf);//止损
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 止损卖出
     *
     * @param conf
     */
    private void lossSell(UserConf conf) {
        HoldInfo info = autoTradeSer.getHoldInfo(conf);
        Integer count = StringUtils.isNotBlank(info.getLong_avail_qty()) ? Integer.parseInt(info.getLong_avail_qty()) : 0;//剩余张数
        if (StringUtils.isNotBlank(info.getLong_pnl())) {//多仓收益
            double profit = Double.parseDouble(info.getLong_pnl_ratio()) * 100; //负数为亏损,多仓收益率
            //profit-50
            double loss = conf.getLoss();//50
            if (0 > profit && Math.abs(profit) >= loss) { //如果设定的损值大于实际的损值,profit少于0的时候就是亏损了
                if (count > 0) {
                    autoTradeSer.commitSellOrder(conf, count);//全部卖出
                    String email = conf.getUser().getEmail();
                    if (StringUtils.isNotBlank(email)) {
                        String msg = "亏损率为" + StringUtil.formatDouble(profit) + "%，达到设定值" + StringUtil.formatDouble(loss) + "%， 已全部平仓卖出！" + "卖出张数为：" + count;
                        LOG.info(msg);
                        MailUtil.send(email, "亏损率达到设定值" + StringUtil.formatDouble(loss) + ",平仓卖出！", msg);
                    }
                }

            }
        }

    }

}
