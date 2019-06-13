package com.xinaml.robot.common.config;

import com.xinaml.robot.base.rep.RedisRep;
import com.xinaml.robot.common.utils.UserUtil;
import com.xinaml.robot.ser.user.UserSer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class BeanConf {
    @Autowired
    public UserSer userSer;

    @Autowired
    private RedisRep redisRep;

    @PostConstruct
    public void init() {
        UserUtil.userSer = userSer;
        UserUtil.redisRep = redisRep;
    }
}
