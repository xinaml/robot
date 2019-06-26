package com.xinaml.robot.common.okex.threads;

import com.xinaml.robot.entity.user.UserConf;
import com.xinaml.robot.ser.okex.AutoTradeSer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @Author: [lgq]
 * @Date: [19-6-14 上午10:38]
 * @Description: 线程扫描
 * @Version: [1.0.0]
 * @Copy: [com.xinaml]
 */
@Component
public class ThreadScan {
    @Autowired
    private AutoTradeSer autoTradeSer;

    @PostConstruct
    public void init() {
        this.tradeSer = autoTradeSer;
    }

    private static AutoTradeSer tradeSer;

    public static void scan(String userId, Boolean stop, UserConf conf) {
        if (stop) {
            stop(userId);
        } else {
            start(userId, conf);
        }
    }

    /**
     * 移除线程
     *
     * @param userId
     */
    private static void stop(String userId) {
        Timer timer = TaskSession.get(userId);
        if (null != timer) {
            timer.cancel();
            timer.purge();
            TaskSession.remove(userId);
        }
    }

    /**
     * 开始线程
     *
     * @param userId
     * @param conf
     */
    private static void start(String userId, UserConf conf) {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    if (null != tradeSer) {
                        tradeSer.trade(conf);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 0, 10000); //每1查询一次
        TaskSession.put(userId, timer);
    }
}
