package com.xinaml.robot.action;

import com.xinaml.robot.common.custom.annotation.Login;
import com.xinaml.robot.common.custom.result.ActResult;
import com.xinaml.robot.common.utils.UserUtil;
import com.xinaml.robot.common.webscoket.WebSocketServer;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * @Author: [lgq]
 * @Date: [19-7-2 上午9:09]
 * @Description:
 * @Version: [1.0.0]
 * @Copy: [com.xinaml]
 */
@Login
@Controller
public class SocketAct {

    //页面请求
    @GetMapping("socket/page")
    public ModelAndView socket() {
        ModelAndView mav = new ModelAndView("user/info");
        mav.addObject("userId", UserUtil.getUser().getId());
        return mav;
    }

    //推送数据接口
    @PostMapping("/socket/push/{userId}")
    public ActResult pushToWeb(@PathVariable String userId, String message) {
        WebSocketServer.sendInfo(message, userId);
        return new ActResult(userId);
    }
}
