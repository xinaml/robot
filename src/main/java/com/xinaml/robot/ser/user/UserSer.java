package com.xinaml.robot.ser.user;

import com.xinaml.robot.base.ser.Ser;
import com.xinaml.robot.common.custom.exception.SerException;
import com.xinaml.robot.dto.user.UserDTO;
import com.xinaml.robot.entity.user.User;
import com.xinaml.robot.to.user.LoginTO;
import com.xinaml.robot.to.user.RegisterTO;
import com.xinaml.robot.to.user.UserSecretTO;

public interface UserSer extends Ser<User, UserDTO> {
    /**
     * 登录
     * @param to
     * @return
     * @throws SerException
     */
    String login(LoginTO to) throws SerException;
    /**
     * 注册
     * @param to
     * @return
     * @throws SerException
     */
    String register(RegisterTO to) throws SerException;
    /**
     * 退出
     * @param token
     * @return
     * @throws SerException
     */
    Boolean logout(String token) throws SerException;

    /**
     * 开启自动扫描
     * @return
     * @throws SerException
     */
    Boolean stop() throws SerException;

    void editSecret(UserSecretTO to);
}
