package com.xinaml.robot.common.okex.threads;

import com.alibaba.fastjson.JSON;
import com.xinaml.robot.base.dto.RT;
import com.xinaml.robot.base.rep.RedisRep;
import com.xinaml.robot.common.utils.UserUtil;
import com.xinaml.robot.dto.user.UserConfDTO;
import com.xinaml.robot.dto.user.UserDTO;
import com.xinaml.robot.entity.user.User;
import com.xinaml.robot.entity.user.UserConf;
import com.xinaml.robot.ser.user.UserConfSer;
import com.xinaml.robot.ser.user.UserSer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * @Author: [lgq]
 * @Date: [19-6-26 下午2:24]
 * @Description:启动时开始用户交易扫描
 * @Version: [1.0.0]
 * @Copy: [com.xinaml]
 */
@Component
public class StartInit {
    @Autowired
    private UserConfSer userConfSer;
    @Autowired
    private RedisRep redisRep;
    @PostConstruct
    public void start(){
        List<UserConf> confs = userConfSer.findAll();
        for(UserConf conf:confs){
            User user =conf.getUser();
            if(!user.getStop()){
                userConfSer.setSeconds(conf);
                redisRep.put(user.getId()+user.getUsername(),JSON.toJSONString(conf));//保存配置信息到redis
                ThreadScan.scan(user.getId(),false,conf);
            }
        }

    }
}
