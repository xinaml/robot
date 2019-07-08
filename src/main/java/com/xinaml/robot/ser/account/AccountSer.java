package com.xinaml.robot.ser.account;

import com.xinaml.robot.entity.user.UserConf;
import com.xinaml.robot.vo.user.AccountInfo;

/**
 * @Author: [lgq]
 * @Date: [19-7-8 上午9:48]
 * @Description:
 * @Version: [1.0.0]
 * @Copy: [com.xinaml]
 */
public interface AccountSer {
    default AccountInfo info(UserConf conf){
        return null;
    }
}
