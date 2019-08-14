package com.xinaml.robot.action.user;

import com.xinaml.robot.base.atction.BaseAct;
import com.xinaml.robot.base.entity.ADD;
import com.xinaml.robot.common.constant.FinalConst;
import com.xinaml.robot.common.constant.PathConst;
import com.xinaml.robot.common.custom.exception.ActException;
import com.xinaml.robot.common.custom.exception.SerException;
import com.xinaml.robot.common.custom.result.ActResult;
import com.xinaml.robot.common.custom.result.Result;
import com.xinaml.robot.common.utils.IpUtil;
import com.xinaml.robot.common.utils.UserUtil;
import com.xinaml.robot.ser.user.UserSer;
import com.xinaml.robot.to.user.LoginTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@RestController
public class LoginAct extends BaseAct {
    @Autowired
    private UserSer userSer;
    /**
     * 转登录页
     *
     * @param model
     * @param request
     * @return
     * @throws ActException
     */
    @GetMapping("login")
    public ModelAndView login(Model model, HttpServletRequest request) throws ActException {
        try {
            Object prevUrl = request.getSession().getAttribute(PathConst.PREV_URL);//获取上次请求页
            if (UserUtil.isLogin(request)) {
                if (null != prevUrl) {
                    return new ModelAndView("redirect:" + prevUrl);
                }
                return new ModelAndView("redirect:index");
            } else {
                model.addAttribute(PathConst.PREV_URL, prevUrl);
                return new ModelAndView("login"); //跳转登录页
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ModelAndView("login");//跳转登录页
        }
    }

    /**
     * 用户登录
     *
     * @param to
     * @param request
     * @param response
     * @return
     * @throws ActException
     */
    @PostMapping("login")
    public Result login(@Validated(ADD.class) LoginTO to, HttpServletRequest request, HttpServletResponse response) throws ActException {
        try {
            to.setIp(IpUtil.getIp(request));
            String token = userSer.login(to);
            Map<String, String> maps = new HashMap<>(1);
            maps.put(FinalConst.TOKEN, token);
            Cookie cookie = new Cookie(FinalConst.TOKEN, token);
            cookie.setMaxAge(7*60 * 60 * 24); //token 有效期为7天
            response.addCookie(cookie);
            request.getSession().invalidate();
            return new ActResult(SUCCESS, maps);
        } catch (SerException e) {
            throw new ActException(e.getMessage());
        }
    }

    /**
     * 退出登录
     *
     * @param request
     * @param response
     * @return
     * @throws ActException
     */
    @GetMapping("logout")
    public ModelAndView logout(HttpServletRequest request, HttpServletResponse response) throws ActException {
        try {
            String token = UserUtil.getToken(request);
            boolean isOut = userSer.logout(token);
            if (isOut) {
                Cookie cookie = new Cookie(FinalConst.TOKEN, token);
                cookie.setMaxAge(0);
                response.addCookie(cookie);
            }
            return new ModelAndView("redirect:login");

        } catch (SerException e) {
            throw new ActException(e.getMessage());
        }
    }

}
