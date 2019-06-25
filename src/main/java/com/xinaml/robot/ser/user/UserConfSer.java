package com.xinaml.robot.ser.user;

import com.xinaml.robot.base.ser.Ser;
import com.xinaml.robot.dto.user.UserConfDTO;
import com.xinaml.robot.entity.user.UserConf;
import com.xinaml.robot.to.user.UserConfTO;

/**
 * @Author: [lgq]
 * @Date: [19-6-25 下午5:23]
 * @Description:
 * @Version: [1.0.0]
 * @Copy: [com.changbei]
 */
public interface UserConfSer extends Ser<UserConf, UserConfDTO> {
    default void saveConf(UserConfTO to) {

    }

    default UserConf findByUserId(String userId) {
        return null;
    }
}
