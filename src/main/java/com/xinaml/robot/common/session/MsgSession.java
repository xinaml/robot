package com.xinaml.robot.common.session;

import com.google.common.cache.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Timer;
import java.util.concurrent.TimeUnit;

/**
 * 如果数据有改动，放置该缓存
 */
public class MsgSession {
    private static Logger logger = LoggerFactory.getLogger(MsgSession.class);

    private MsgSession() {
    }


    private static LoadingCache<String, String> MSG_SESSION = CacheBuilder.newBuilder()
            .expireAfterWrite(1, TimeUnit.HOURS)
            .maximumSize(1000)
            .removalListener(new RemovalListener<String, String>() {
                @Override
                public void onRemoval(RemovalNotification<String, String> notification) {
                    logger.debug("remove:" + notification.getCause().name());
                }
            })
            .build(new CacheLoader<String, String>() {
                @Override
                public String load(String key) throws Exception {
                    return null;
                }
            });


    public static void put(String key, String cdn) {
        if (StringUtils.isNotBlank(key)) {
            MSG_SESSION.put(key, cdn);
        }
    }


    public static void remove(String key) {
        if (StringUtils.isNotBlank(key)) {
            MSG_SESSION.invalidate(key);
        }

    }


    public static String get(String key) {
        try {
            return MSG_SESSION.get(key);
        } catch (Exception e) {
            return null;
        }
    }
}
