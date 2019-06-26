package com.xinaml.robot.common.okex.threads;

import com.google.common.cache.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Timer;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * 如果数据有改动，放置该缓存
 */
public class TaskSession {
    private static Logger logger = LoggerFactory.getLogger(TaskSession.class);

    private TaskSession() {
    }


    private static LoadingCache<String, Timer> TASK_SESSION = CacheBuilder.newBuilder()
            .expireAfterWrite(30, TimeUnit.DAYS)
            .maximumSize(1000)
            .removalListener(new RemovalListener<String, Timer>() {
                @Override
                public void onRemoval(RemovalNotification<String, Timer> notification) {
                    logger.info("remove:" + notification.getCause().name());
                }
            })
            .build(new CacheLoader<String, Timer>() {
                @Override
                public Timer load(String key) throws Exception {
                    return null;
                }
            });


    public static void put(String key, Timer cdn) {
        if (StringUtils.isNotBlank(key)) {
            TASK_SESSION.put(key, cdn);
        }
    }


    public static void remove(String key) {
        if (StringUtils.isNotBlank(key)) {
            TASK_SESSION.invalidate(key);
        }
        System.out.println("定时器剩余：" + TASK_SESSION.size());

    }


    public static Timer get(String key) {
        try {
            return TASK_SESSION.get(key);
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }
}
