package com.xinaml.robot.action.user;

import com.alibaba.fastjson.JSON;
import com.xinaml.robot.base.atction.BaseAct;
import com.xinaml.robot.base.rep.RedisRep;
import com.xinaml.robot.common.custom.result.ActResult;
import com.xinaml.robot.common.custom.result.Result;
import com.xinaml.robot.common.utils.DateUtil;
import com.xinaml.robot.common.utils.UserUtil;
import com.xinaml.robot.entity.user.User;
import com.xinaml.robot.entity.user.UserConf;
import com.xinaml.robot.ser.okex.AutoTradeSer;
import com.xinaml.robot.vo.user.HoldInfo;
import com.xinaml.robot.vo.user.KLine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: [lgq]
 * @Date: [19-7-5 上午9:42]
 * @Description:持仓信息
 * @Version: [1.0.0]
 * @Copy: [com.xinaml]
 */
@RestController
public class HoldAct extends BaseAct {
    @Autowired
    private AutoTradeSer autoTradeSer;
    @Autowired
    private RedisRep redisRep;

    @GetMapping("hold/info")
    public Result getInfo() {
        User user = UserUtil.getUser();
        if (null != user) {
            String rs = redisRep.get(user.getId() + "conf");
            if (null != rs) {
                return new ActResult(autoTradeSer.getHoldInfo(JSON.parseObject(rs, UserConf.class)));
            }
        }
        return new ActResult(new HoldInfo());
    }

    @GetMapping("kline")
    public Result kline() {
        User user = UserUtil.getUser();
        if (null != user) {
            String rs = redisRep.get(user.getId() + "conf");
            if (null != rs) {
                KLine kLine = autoTradeSer.getLine(JSON.parseObject(rs, UserConf.class));
                String time = kLine.getTimestamp();
                time = time.substring(0, 19).replace("T", " ");
                time = DateUtil.parseDateTime(time).plusHours(8).toString().replace("T", " ");
                kLine.setTimestamp(time);
                return new ActResult(kLine);

            }
        }
        return new ActResult(new KLine());
    }

    public static void main(String[] args) {
        String time = "2019-07-05T10:00:00.000Z";
        time = time.substring(0, 19).replace("T", " ");
        System.out.println();

    }
}
