package com.xinaml.robot.to.user;

import com.xinaml.robot.base.entity.ADD;
import com.xinaml.robot.base.entity.EDIT;

import javax.validation.constraints.NotBlank;

public class LoginTO {
    @NotBlank(groups = {ADD.class}, message = "用户名不能为空！")
    private String username;

    @NotBlank(groups = {ADD.class}, message = "密码不能为空！")
    private String password;

    private String ip;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
}
