package com.xinaml.robot.action.user;

import com.xinaml.robot.base.entity.ADD;
import com.xinaml.robot.common.custom.annotation.Login;
import com.xinaml.robot.common.custom.exception.ActException;
import com.xinaml.robot.common.custom.result.ActResult;
import com.xinaml.robot.common.custom.result.Result;
import com.xinaml.robot.common.utils.UserUtil;
import com.xinaml.robot.entity.user.User;
import com.xinaml.robot.entity.user.UserConf;
import com.xinaml.robot.ser.user.UserConfSer;
import com.xinaml.robot.to.user.UserConfTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

/**
 * @Author: [lgq]
 * @Date: [19-6-25 下午5:04]
 * @Description:
 * @Version: [1.0.0]
 * @Copy: [com.xinaml]
 */
@RequestMapping("user")
@RestController
@Login
public class UserConfAct {
    @Autowired
    private UserConfSer userConfSer;

    @GetMapping("conf")
    public ModelAndView conf() throws ActException {
        return new ModelAndView("user/conf");
    }

    @PutMapping("conf/edit")
    public Result edit(@Validated(ADD.class) UserConfTO to, BindingResult rs) throws ActException {
        userConfSer.saveConf(to);
        return new ActResult("保存配置成功！");
    }


    @GetMapping("conf/get")
    public Result get() throws ActException {
        User user = UserUtil.getUser();
        UserConf conf = userConfSer.findByUserId(user.getId());
        return new ActResult(conf);
    }


}
