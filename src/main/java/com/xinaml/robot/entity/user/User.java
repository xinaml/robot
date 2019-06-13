/**
 * 用户实体
 *
 * @author lgq
 * @date 2018/4/15
 **/
package com.xinaml.robot.entity.user;

import com.xinaml.robot.base.entity.BaseEntity;
import com.xinaml.robot.types.SexType;
import org.hibernate.validator.constraints.Range;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name = "tb_user")
public class User extends BaseEntity {
    @Column(unique = true, nullable = false, columnDefinition = "VARCHAR(25) COMMENT '用户名'")
    private String username;

    @Column(nullable = false, columnDefinition = "VARCHAR(56) COMMENT '密码'")
    private String password;

    @Column(unique = true, length = 20, columnDefinition = "VARCHAR(25) COMMENT '邮件'")
    private String email;

    //常用类型 ：String
    @Column(unique = true, length = 20, columnDefinition = "VARCHAR(25) COMMENT '手机号'")
    private String phone;

    //常用类型 ：Integer
    @Range(min = 0, max = 100) //取范围
    @Column(columnDefinition = "TINYINT COMMENT '年龄'")
    private Integer age;

    // 常用类型 ：Double
    @Column(columnDefinition = " DECIMAL(5,2) COMMENT '重量'")
    private Double weight;

    //常用类型 ：Enum
    @Column(columnDefinition = "TINYINT(2) COMMENT '性别'")
    private SexType sexType;

    //常用类型 ：Boolean
    @Column(name = "is_expired", columnDefinition = "TINYINT(1) DEFAULT 0 COMMENT '是否过期'", nullable = false, insertable = false)
    private Boolean expired;

    //常用类型 ：Date
    @Column(columnDefinition = "DATE  COMMENT '日期'")
    private LocalDate birthday;


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
