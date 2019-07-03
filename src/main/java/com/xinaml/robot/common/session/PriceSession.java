package com.xinaml.robot.common.session;

import com.google.common.cache.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * @Author: [lgq]
 * @Date: [19-7-3 上午9:02]
 * @Description:上次成交价缓存
 * @Version: [1.0.0]
 * @Copy: [com.xinaml]
 */
public class PriceSession {
    private static Logger logger = LoggerFactory.getLogger(PriceSession.class);

    private PriceSession() {
    }


    private static LoadingCache<String, String> PRICE_SESSION = CacheBuilder.newBuilder()
            .expireAfterWrite(30, TimeUnit.MINUTES)
            .maximumSize(1000)
            .removalListener(new RemovalListener<String, String>() {
                @Override
                public void onRemoval(RemovalNotification<String, String> notification) {
                    logger.info("remove:" + notification.getCause().name());
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
            PRICE_SESSION.put(key, cdn);
        }
    }


    public static void remove(String key) {
        if (StringUtils.isNotBlank(key)) {
            PRICE_SESSION.invalidate(key);
        }

    }


    public static String get(String key) {
        try {
            return PRICE_SESSION.get(key);
        } catch (Exception e) {
            return null;
        }
    }
}
