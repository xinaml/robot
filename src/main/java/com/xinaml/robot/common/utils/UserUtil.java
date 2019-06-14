package com.xinaml.robot.common.utils;

import com.alibaba.fastjson.JSON;
import com.xinaml.robot.base.rep.RedisRep;
import com.xinaml.robot.common.constant.FinalConst;
import com.xinaml.robot.entity.user.User;
import com.xinaml.robot.ser.user.UserSer;
import com.xinaml.robot.common.constant.FinalConst;
import com.xinaml.robot.entity.user.User;
import com.xinaml.robot.ser.user.UserSer;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * 用户工具，（定时器请勿从此处获取）
 */
public final class UserUtil {
    public static UserSer userSer;
    public static RedisRep redisRep;

    /**
     * 是否已登录
     *
     * @param token
     * @return
     */
    public static boolean isLogin(String token) {
        return StringUtils.isNotBlank(token);
    }

    /**
     * 是否已登录
     *
     * @param request
     * @return
     */
    public static boolean isLogin(HttpServletRequest request) {
        String token = getToken(request);
        if (StringUtils.isNotBlank(token) && null != (redisRep.get(token))) {
            return true;
        }
        return false;
    }

    /**
     * 获取当前用户token
     *
     * @param request
     * @return
     */
    public static String getToken(HttpServletRequest request) {
        String token = request.getHeader(FinalConst.TOKEN); //取header的token
        if (StringUtils.isBlank(token)) { //取cookie的token
            if (null != request.getCookies()) {
                for (Cookie cookie : request.getCookies()) {
                    if (cookie.getName().equals(FinalConst.TOKEN)) {
                        token = cookie.getValue();
                        break;
                    }
                }
            }
        }
        return token;
    }

    /**
     * 获取当前用户token
     * @return
     */
    public static String getToken( ) {
        HttpServletRequest request = RequestUtil.get();
        String token = request.getHeader(FinalConst.TOKEN); //取header的token
        if (StringUtils.isBlank(token)) { //取cookie的token
            if (null != request.getCookies()) {
                for (Cookie cookie : request.getCookies()) {
                    if (cookie.getName().equals(FinalConst.TOKEN)) {
                        token = cookie.getValue();
                        break;
                    }
                }
            }
        }
        return token;
    }

    /**
     * 获取当前用户
     *
     * @return
     */
    public static User getUser() {
        HttpServletRequest request = RequestUtil.get();
        String token = getToken(request);
        if (StringUtils.isNotBlank(token)) {
            String userJson = redisRep.get(token);
            if (StringUtils.isNoneBlank(userJson)) {
                return JSON.parseObject(userJson, User.class);
            }
        }
        return null;
    }

    /**
     * 获取当前用户
     *
     * @param cache 是否从缓存拿
     * @return
     */
    public static User getUser(boolean cache) {
        if (cache) {
            return getUser();
        } else {
            User u = getUser();
            try {
                return userSer.findById(u.getId());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    /**
     * 通过token获取当前用户
     *
     * @param token
     * @return
     */
    public static User getUser(String token) {
        if (StringUtils.isNotBlank(token)) {
            String userJson = redisRep.get(token);
            if (StringUtils.isNoneBlank(userJson)) {
                return JSON.parseObject(userJson, User.class);
            }
        }
        return null;
    }


}
