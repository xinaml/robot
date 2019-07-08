package com.xinaml.robot;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xinaml.robot.common.constant.UrlConst;
import com.xinaml.robot.common.okex.Client;
import com.xinaml.robot.common.okex.utils.DateUtils;
import com.xinaml.robot.entity.user.UserConf;
import com.xinaml.robot.vo.user.HoldInfo;
import com.xinaml.robot.vo.user.KLine;

import java.util.List;

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
        String rs = Client.httpGet(UrlConst.HOLD_INFO + "BTC-USD-190927" + "/position",null);
        JSONObject ob = JSON.parseObject(rs);
        JSONArray array = (JSONArray) ob.get("holding");
        List<HoldInfo> list = array.toJavaList(HoldInfo.class);
        System.out.println(JSON.toJSONString(list));
    }



}
