package com.xinaml.robot.ser.okex;

import com.xinaml.robot.entity.user.UserConf;

/**
 * @Author: [lgq]
 * @Date: [19-6-26 下午1:53]
 * @Description: 自动交易
 * @Version: [1.0.0]
 * @Copy: [com.xinaml]
 */
public interface AutoTradeSer {
    /**
     * 自动交易
     * @param conf
     */
    default void trade(UserConf conf) {

    }
}
