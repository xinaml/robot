package com.xinaml.robot.common.okex.threads;

import com.xinaml.robot.common.session.TaskSession;
import com.xinaml.robot.entity.user.UserConf;
import org.springframework.stereotype.Component;

import java.util.Timer;
import java.util.TimerTask;

/**
 * @Author: [lgq]
 * @Date: [19-6-14 上午10:38]
 * @Description: 委托订单扫描
 * @Version: [1.0.0]
 * @Copy: [com.xinaml]
 */
@Component
public class TrustScan {


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
                    System.out.println("查询委托订单是否成功");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 0, 1000 * 60); //每1查询一次
    }
}
