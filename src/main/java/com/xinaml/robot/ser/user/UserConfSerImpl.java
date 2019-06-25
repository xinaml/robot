package com.xinaml.robot.ser.user;

import com.xinaml.robot.base.dto.RT;
import com.xinaml.robot.base.ser.ServiceImpl;
import com.xinaml.robot.common.utils.UserUtil;
import com.xinaml.robot.dto.user.UserConfDTO;
import com.xinaml.robot.entity.user.User;
import com.xinaml.robot.entity.user.UserConf;
import com.xinaml.robot.to.user.UserConfTO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * @Author: [lgq]
 * @Date: [19-6-25 下午5:23]
 * @Description:
 * @Version: [1.0.0]
 * @Copy: [com.changbei]
 */
@Service
public class UserConfSerImpl extends ServiceImpl<UserConf, UserConfDTO> implements UserConfSer {
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
    }

    @Override
    public UserConf findByUserId(String userId) {
        UserConfDTO dto = new UserConfDTO();
        dto.addRT(RT.eq("user.id", userId));
        UserConf conf = findOne(dto);
        return conf;
    }
}
