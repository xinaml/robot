package com.xinaml.robot.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

public final class RequestUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(RequestUtil.class);

    private RequestUtil() {
    }

    public static HttpServletRequest get() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();//springmvc 自带
    }
}
