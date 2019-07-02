package com.xinaml.robot.common.okex.threads;

import com.xinaml.robot.entity.user.User;
import com.xinaml.robot.entity.user.UserConf;
import com.xinaml.robot.ser.user.UserConfSer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * @Author: [lgq]
 * @Date: [19-6-26 下午2:24]
 * @Description:启动时开始用户交易扫描
 * @Version: [1.0.0]
 * @Copy: [com.xinaml]
 */
@Component
public class StartInit {
    @Autowired
    private UserConfSer userConfSer;

    /**
     * 项目启动时，检测用户配置开启自动交易
     */
    @PostConstruct
    public void start() {
        List<UserConf> confs = userConfSer.findAll();
        for (UserConf conf : confs) {
            User user = conf.getUser();
            if (!user.getStop()) {
                userConfSer.setSeconds(conf);
                ThreadScan.scan(user.getId(), false, conf);
            }
        }

    }


}
