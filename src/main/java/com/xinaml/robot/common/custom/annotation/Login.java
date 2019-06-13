package com.xinaml.robot.common.custom.annotation;


import java.lang.annotation.*;

/**
 * 自定义注解
 * 登录拦截标识
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Login {
    String excludes() default ""; //不扫描某个方法，多个逗号隔开
}
