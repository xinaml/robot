package com.xinaml.robot;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xinaml.robot.common.constant.UrlConst;
import com.xinaml.robot.common.okex.Client;
import com.xinaml.robot.vo.user.HoldInfo;

import java.util.List;

import static com.xinaml.robot.common.constant.UrlConst.LAST;

/**
 * @Author: [lgq]
 * @Date: [19-7-1 上午9:05]
 * @Description:
 * @Version: [1.0.0]
 * @Copy: [com.xinaml]
 */
public class Test {
    public static void main(String[] args) {
        String lastUrl = LAST + "/" + "BTC-USD-190927" + "/ticker";
        String rs = Client.httpGet(lastUrl, null);
        double last = 0;
        if (rs != null && rs.length() > 5) {
            JSONObject object = JSON.parseObject(rs);
            last = object.getDouble("last");
        }
        rs = Client.httpGet(UrlConst.HOLD_INFO + "BTC-USD-190927" + "/position", null);
        JSONObject ob = JSON.parseObject(rs);
        JSONArray array = (JSONArray) ob.get("holding");
        List<HoldInfo> list = array.toJavaList(HoldInfo.class);
        String avg = list.get(0).getLong_avg_cost();
        System.out.println("last:"+last);
        System.out.println("avg:"+avg);
        System.out.println(last - Double.parseDouble(avg));


    }
}
