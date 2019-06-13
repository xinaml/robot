package com.xinaml.robot.to.user;

import com.xinaml.robot.base.entity.ADD;

import javax.validation.constraints.NotBlank;

public class RegisterTO {
    @NotBlank(groups = {ADD.class}, message = "用户名不能为空！")
    private String username;
    @NotBlank(groups = {ADD.class}, message = "密码不能为空！")
    private String password;
    @NotBlank(groups = {ADD.class}, message = "重复密码不能为空！")
    private String rePassword;

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

    public String getRePassword() {
        return rePassword;
    }

    public void setRePassword(String rePassword) {
        this.rePassword = rePassword;
    }
}
