package com.xinaml.robot.to.user;

import com.xinaml.robot.base.entity.ADD;
import com.xinaml.robot.base.entity.EDIT;
import com.xinaml.robot.base.to.BaseTO;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public class UserSecretTO extends BaseTO {
    @NotBlank(groups = {ADD.class, EDIT.class}, message = "secretKey不能为空！")
    private String secretKey;

    @NotBlank(groups = {ADD.class, EDIT.class}, message = "apiKey不能为空！")
    private String apiKey;

    @NotBlank(groups = {ADD.class, EDIT.class}, message = "passPhrase不能为空！")
    private String passPhrase;

    private String email;

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getPassPhrase() {
        return passPhrase;
    }

    public void setPassPhrase(String passPhrase) {
        this.passPhrase = passPhrase;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
