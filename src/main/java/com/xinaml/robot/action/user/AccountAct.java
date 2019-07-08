package com.xinaml.robot.action.user;

import com.xinaml.robot.common.custom.result.ActResult;
import com.xinaml.robot.common.custom.result.Result;
import com.xinaml.robot.common.utils.UserUtil;
import com.xinaml.robot.entity.user.User;
import com.xinaml.robot.entity.user.UserConf;
import com.xinaml.robot.ser.account.AccountSer;
import com.xinaml.robot.ser.user.UserConfSer;
import com.xinaml.robot.vo.user.AccountInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: [lgq]
 * @Date: [19-7-8 上午9:46]
 * @Description:
 * @Version: [1.0.0]
 * @Copy: [com.xinaml]
 */
@RequestMapping("account")
@RestController
public class AccountAct {
    @Autowired
    private AccountSer accountSer;
    @Autowired
    private UserConfSer userConfSer;

    @GetMapping("info")
    public Result info() {
        User user = UserUtil.getUser();
        if (null != user) {
            UserConf conf = userConfSer.findByUserId(user.getId());
            if (null != conf) {
                AccountInfo info = accountSer.info(conf);
                return new ActResult(info);

            }
        }
        return new ActResult(new AccountInfo());
    }
}
