package com.xinaml.robot.base.rep;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.concurrent.TimeUnit;


@Component
public class RedisRep {
    private static Logger LOGGER = LoggerFactory.getLogger(RedisRep.class);
    private static String FAIL_MSG = "redis 连接失败！";
    @Autowired
    private StringRedisTemplate template;

    /**
     * 设置值
     *
     * @param key
     * @param value
     */
    public void put(String key, String value) {

        try {
            ValueOperations<String, String> ops = template.opsForValue();
            ops.set(key, value);
        } catch (RedisConnectionFailureException e) {
            LOGGER.error(FAIL_MSG);
            throw new RuntimeException(e.getCause());
        }


    }

    /**
     * 设置值
     *
     * @param key
     * @param value
     * @param timeOut
     * @param timeUnit
     */
    public void put(String key, String value, long timeOut, TimeUnit timeUnit) {
        try {
            ValueOperations<String, String> ops = template.opsForValue();
            ops.set(key, value, timeOut, timeUnit);
        } catch (RedisConnectionFailureException e) {
            LOGGER.error(FAIL_MSG);
            throw new RuntimeException(e.getCause());
        }
    }

    /**
     * 获取值
     *
     * @param key
     * @return
     */
    public String get(String key) {
        try {
            ValueOperations<String, String> ops = this.template.opsForValue();
            return ops.get(key);
        } catch (RedisConnectionFailureException e) {
            LOGGER.error(FAIL_MSG);
            throw new RuntimeException(e.getCause());
        }
    }

    /**
     * 设置过期
     *
     * @param key
     * @param timeOut
     * @param timeUnit
     */
    public void expire(String key, long timeOut, TimeUnit timeUnit) {
        try {
            this.template.expire(key, timeOut, timeUnit);
        } catch (RedisConnectionFailureException e) {
            LOGGER.error(FAIL_MSG);
            throw new RuntimeException(e.getCause());
        }
    }

    /**
     * 获取过期时间
     *
     * @param key
     * @param timeUnit
     */
    public Long getExpire(String key, TimeUnit timeUnit) {
        try {
            return this.template.getExpire(key, timeUnit);
        } catch (RedisConnectionFailureException e) {
            LOGGER.error(FAIL_MSG);
            throw new RuntimeException(e.getCause());
        }
    }

    /**
     * 删除建
     *
     * @param key
     */
    public void del(String key) {
        try {
            this.template.delete(key);
        } catch (RedisConnectionFailureException e) {
            LOGGER.error(FAIL_MSG);
            throw new RuntimeException(e.getCause());
        }
    }

    /**
     * 是否存在
     *
     * @param key
     */
    public boolean exists(String key) {
        try {
            return this.template.hasKey(key);
        } catch (RedisConnectionFailureException e) {
            LOGGER.warn(FAIL_MSG);
            throw new RuntimeException(e.getCause());
        }
    }

    /**
     * 保存集合
     *
     * @param key
     * @param value
     */
    public void setHashSet(String key, String... value) {
        try {
            template.opsForSet().add(key, value);
        } catch (RedisConnectionFailureException e) {
            LOGGER.error(FAIL_MSG);
            throw new RuntimeException(e.getCause());
        }
    }

    /**
     * 根据key获取集合
     *
     * @param key
     * @return
     */
    public Set<String> getHashSet(String key) {
        try {
            return template.opsForSet().members(key);
        } catch (RedisConnectionFailureException e) {
            LOGGER.error(FAIL_MSG);
            throw new RuntimeException(e.getCause());
        }
    }

    /**
     * 根据key查看集合中是否存在指定数据
     *
     * @param key
     * @param value
     * @return
     */
    public boolean existsHashSet(String key, String value) {
        try {
            return template.opsForSet().isMember(key, value);
        } catch (RedisConnectionFailureException e) {
            LOGGER.error(FAIL_MSG);
            throw new RuntimeException(e.getCause());
        }
    }

    public Long removeHashSetVal(String key, String value) {
        try {
            return template.opsForSet().remove(key, value);
        } catch (RedisConnectionFailureException e) {
            LOGGER.error(FAIL_MSG);
            throw new RuntimeException(e.getCause());
        }
    }

}
