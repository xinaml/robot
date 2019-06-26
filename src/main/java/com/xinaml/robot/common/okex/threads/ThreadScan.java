package com.xinaml.robot.common.okex.threads;

import com.xinaml.robot.entity.user.UserConf;

import java.util.Timer;
import java.util.TimerTask;

/**
 * @Author: [lgq]
 * @Date: [19-6-14 上午10:38]
 * @Description: 线程扫描
 * @Version: [1.0.0]
 * @Copy: [com.changbei]
 */
public class ThreadScan {
    public static void scan(String userId, Boolean stop, UserConf conf) {
        if (stop) {
            stop(userId);
        } else {
            start(userId,conf);
        }
    }


    private static void stop(String userId) {
        Timer timer = TaskSession.get(userId);
        if (null != timer) {
            timer.cancel();
            timer.purge();
            TaskSession.remove(userId);
        }
    }

    private static void start(String userId,UserConf conf) {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                String key = null;
                try {
                    System.out.println("running。。。");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 0, 1000); //每1查询一次
        TaskSession.put(userId, timer);
    }
}
