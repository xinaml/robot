/**
 * 用户持久化接口
 *
 * @author lgq
 * @date 2018/4/15
 **/
package com.xinaml.robot.rep.user;

import com.xinaml.robot.base.rep.JapRep;
import com.xinaml.robot.dto.user.UserDTO;
import com.xinaml.robot.entity.user.User;

public interface UserRep extends JapRep<User, UserDTO> {
    User findByUsername(String username);
}
