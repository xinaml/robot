package com.xinaml.robot.common.interceptor;

import com.alibaba.fastjson.JSON;
import com.xinaml.robot.common.constant.PathConst;
import com.xinaml.robot.common.custom.annotation.Login;
import com.xinaml.robot.common.custom.result.ActResult;
import com.xinaml.robot.common.utils.ResponseUtil;
import com.xinaml.robot.common.utils.UserUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * 登录拦截
 *
 * @author lgq
 * @date 2018/4/15
 **/
@Component
public class LoginIntercept extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!handler.getClass().isAssignableFrom(HandlerMethod.class)) {
            return true; //不拦截未知 action（404页面）
        }
        Method method = ((HandlerMethod) handler).getMethod();
        Class<?> clazz = method.getDeclaringClass();
        //该类或者方法上是否有登录安全认证注解
        String url = StringUtils.substringAfterLast(request.getRequestURI(), "/");
        if("".equals(url)||"index".equals(url)){//首页
            return validateLogin(request, response);
        }
        if (clazz.isAnnotationPresent(Login.class) || method.isAnnotationPresent(Login.class)) {
            Annotation an = clazz.getAnnotation(Login.class);
            if (null != an) { //过滤登录方法
                String excludes = ((Login) an).excludes();
                if (null != excludes) {
                    for (String str : excludes.split(",")) {
                        if (url.equals(str)) {
                            return true;
                        }
                    }
                }
            }
            return validateLogin(request, response);
        }
        return true;

    }


    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object o, ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }

    /**
     * 登录校验
     * @param request
     * @param response
     * @return
     */
    private boolean validateLogin(HttpServletRequest request, HttpServletResponse response) {
        try {
            //前后端分离用header取token更符合规范
            if (UserUtil.isLogin(request)) {
                return true;  // 查询是否已登录
            } else {
                //前后分离返回json数据
                String header = request.getHeader("X-Requested-With");
                if (null != header && header.equals("XMLHttpRequest")) { //ajax请求
                    ActResult result = new ActResult();
                    result.setCode(403);
                    result.setMsg("请先登录！");
                    response.setStatus(200);
                    ResponseUtil.writeData(JSON.toJSONString(result));
                    return false;
                } else { //url请求
                    String url ;//request.getHeader("Referer"); //上次请求页面
                    url = request.getRequestURI();

                    if (null == url || StringUtils.isBlank(StringUtils.substringAfterLast(url,"/"))) { //当前请求页面
                        url = request.getRequestURI();
                    }
                    request.getSession().setAttribute(PathConst.PREV_URL, url); //30分钟过期
                    response.sendRedirect("/login");
                }

                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }


}
