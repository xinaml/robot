package com.xinaml.robot.to.user;

import com.xinaml.robot.base.entity.ADD;
import com.xinaml.robot.base.entity.EDIT;
import com.xinaml.robot.base.to.BaseTO;
import com.xinaml.robot.types.SexType;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class UserTO extends BaseTO {
    @NotBlank(groups = {ADD.class, EDIT.class}, message = "用户名不能为空！")
    private String username;

    @NotBlank(groups = {ADD.class, EDIT.class}, message = "手机不能为空！")
    private String phone;

    @Email
    @NotBlank(groups = {ADD.class, EDIT.class}, message = "邮箱不能为空！")
    private String email;

    private LocalDate birthday;

    private LocalDateTime createTime;

    private Integer age;

    private Double weight;

    private SexType sexType;

    private Boolean expired;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public SexType getSexType() {
        return sexType;
    }

    public void setSexType(SexType sexType) {
        this.sexType = sexType;
    }

    public Boolean getExpired() {
        return expired;
    }

    public void setExpired(Boolean expired) {
        this.expired = expired;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }
}

