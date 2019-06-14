package com.xinaml.robot.action.okex;

import com.xinaml.robot.base.atction.BaseAct;
import com.xinaml.robot.common.custom.result.ActResult;
import com.xinaml.robot.common.custom.result.Result;
import com.xinaml.robot.common.okex.Client;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * @Author: [lgq]
 * @Date: [19-6-13 下午5:51]
 * @Description:
 * @Version: [1.0.0]
 * @Copy: [com.changbei]
 */
@RestController
public class AccountAct extends BaseAct {
    @GetMapping("/info")
    public Result info() throws IOException {
        String rs = Client.httpGet("/api/account/v3/deposit/history");
        return new ActResult(rs);
    }
}
