package com.xinaml.robot.ser.okex;

import com.xinaml.robot.common.constant.UrlConst;
import com.xinaml.robot.common.okex.Client;
import com.xinaml.robot.entity.user.UserConf;
import org.springframework.stereotype.Service;

/**
 * @Author: [lgq]
 * @Date: [19-6-26 下午1:53]
 * @Description: 自动交易
 * @Version: [1.0.0]
 * @Copy: [com.xinaml]
 */
@Service
public class AutoTradeSerImpl implements AutoTradeSer {
    @Override
    public void trade(UserConf conf) {
        String url =UrlConst.K_LINE+"/BTC-USD-SWAP/candles?start="+conf.getStartDate()+"&end="+conf.getEndDate()+"&granularity="+conf.getSeconds();
        System.out.println(url);
        String rs = Client.httpGet(url,conf.getUser());
        System.out.println(rs);
    }
}
