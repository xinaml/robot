package com.xinaml.robot.ser.account;

import com.alibaba.fastjson.JSON;
import com.xinaml.robot.common.constant.UrlConst;
import com.xinaml.robot.common.okex.Client;
import com.xinaml.robot.entity.user.UserConf;
import com.xinaml.robot.vo.user.AccountInfo;
import org.springframework.stereotype.Service;

/**
 * @Author: [lgq]
 * @Date: [19-7-8 上午9:49]
 * @Description:
 * @Version: [1.0.0]
 * @Copy: [com.xinaml]
 */
@Service
public class AccountSerImpl implements AccountSer {
    @Override
    public AccountInfo info(UserConf conf) {
        String rs = Client.httpGet(UrlConst.ACCOUNT_INFO, conf.getUser());
        if (null!=rs && rs.indexOf("unrealized_pnl") != -1) {
            AccountInfo info = JSON.parseObject(rs, AccountInfo.class);
            return info;
        }
        return new AccountInfo();
    }
}
