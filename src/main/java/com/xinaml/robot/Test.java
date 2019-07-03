package com.xinaml.robot;

import com.xinaml.robot.common.constant.UrlConst;
import com.xinaml.robot.common.okex.Client;

/**
 * @Author: [lgq]
 * @Date: [19-7-1 上午9:05]
 * @Description:
 * @Version: [1.0.0]
 * @Copy: [com.xinaml]
 */
public class Test {
    public static void main(String[] args) {
        String url="/api/futures/v3/position";
        String rs = Client.httpGet(url, null);
        System.out.println(rs);
    }
}
