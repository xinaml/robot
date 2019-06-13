package com.xinaml.robot.common.config;

import com.xinaml.robot.common.interceptor.LoginIntercept;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 拦截器注册
 */
@Component
public class WebConf implements WebMvcConfigurer {

    @Value("${login.exclude}")
    private String loginExclude;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registryLogin(registry); //添加登录拦截
    }

    private void registryLogin(InterceptorRegistry registry) {
        String[] loginExcludes = loginExclude.split(",");
        for (int i = 0; i < loginExcludes.length; i++) { //过滤设置无需登录url
            loginExcludes[i] = "/" + loginExcludes[i].trim() + "/**";
        }
        registry.addInterceptor(new LoginIntercept()).addPathPatterns("/**").
                excludePathPatterns(loginExcludes);
    }


}
