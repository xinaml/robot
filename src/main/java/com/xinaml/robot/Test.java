package com.xinaml.robot;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.xinaml.robot.common.okex.Client;
import com.xinaml.robot.common.okex.utils.DateUtils;
import com.xinaml.robot.entity.user.UserConf;
import com.xinaml.robot.vo.user.KLine;

import static com.xinaml.robot.common.constant.UrlConst.K_LINE;
import static com.xinaml.robot.common.constant.UrlConst.ORDER_INFO;

/**
 * @Author: [lgq]
 * @Date: [19-7-1 上午9:05]
 * @Description:
 * @Version: [1.0.0]
 * @Copy: [com.xinaml]
 */
public class Test {
    public static void main(String[] args) {
        String url = ORDER_INFO + "/" + "BTC-USD-190927" + "/" + "3122772559674369";
        String rs = Client.httpGet(url, null);
        System.out.println(rs);
    }



}
