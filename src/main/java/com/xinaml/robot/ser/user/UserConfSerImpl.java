package com.xinaml.robot.ser.user;

import com.alibaba.fastjson.JSON;
import com.xinaml.robot.base.dto.RT;
import com.xinaml.robot.base.rep.RedisRep;
import com.xinaml.robot.base.ser.ServiceImpl;
import com.xinaml.robot.common.utils.UserUtil;
import com.xinaml.robot.dto.user.UserConfDTO;
import com.xinaml.robot.entity.user.User;
import com.xinaml.robot.entity.user.UserConf;
import com.xinaml.robot.to.user.UserConfTO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * @Author: [lgq]
 * @Date: [19-6-25 下午5:23]
 * @Description:
 * @Version: [1.0.0]
 * @Copy: [com.xinaml]
 */
@Service
public class UserConfSerImpl extends ServiceImpl<UserConf, UserConfDTO> implements UserConfSer {
    @Autowired
    private RedisRep redisRep;
    @Override
    public void saveConf(UserConfTO to) {
        User user = UserUtil.getUser();
        UserConfDTO dto = new UserConfDTO();
        dto.addRT(RT.eq("user.id", user.getId()));
        UserConf conf = findOne(dto);
        if (null != conf) {
            BeanUtils.copyProperties(to, conf);
            super.update(conf);
        } else {
            conf = new UserConf();
            BeanUtils.copyProperties(to, conf);
            conf.setUser(user);
            conf.setCreateDate(LocalDateTime.now());
            super.save(conf);

        }
        setSeconds(conf);
        redisRep.put(user.getId()+user.getUsername(),JSON.toJSONString(conf));//保存配置信息到redis
    }

    @Override
    public UserConf findByUserId(String userId) {
        UserConfDTO dto = new UserConfDTO();
        dto.addRT(RT.eq("user.id", userId));
        UserConf conf = findOne(dto);
        return conf;
    }

    public void setSeconds(UserConf conf){
        String time =conf.getTime();
        int seconds=1;
        if(time.indexOf("分钟")!=-1){
            time = StringUtils.substringBeforeLast(time,"分钟");
            seconds=Integer.parseInt(time)*60;
        }else if(time.indexOf("小时")!=-1){
            time = StringUtils.substringBeforeLast(time,"小时");
            seconds=Integer.parseInt(time)*60*60;
        }else if(time.indexOf("天")!=-1){
            time = StringUtils.substringBeforeLast(time,"天");
            seconds=Integer.parseInt(time)*60*60*24;
        }else if(time.indexOf("周")!=-1){
            time = StringUtils.substringBeforeLast(time,"周");
            seconds=Integer.parseInt(time)*60*60*24*7;
        }
        conf.setSeconds(seconds);
    }
}
