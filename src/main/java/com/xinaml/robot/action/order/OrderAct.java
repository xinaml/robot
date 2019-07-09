package com.xinaml.robot.action.order;

import com.xinaml.robot.base.dto.RT;
import com.xinaml.robot.common.custom.annotation.Login;
import com.xinaml.robot.common.custom.exception.ActException;
import com.xinaml.robot.common.custom.exception.SerException;
import com.xinaml.robot.common.custom.result.ActResult;
import com.xinaml.robot.common.custom.result.Result;
import com.xinaml.robot.common.utils.UserUtil;
import com.xinaml.robot.dto.order.OrderDTO;
import com.xinaml.robot.entity.user.User;
import com.xinaml.robot.entity.user.UserConf;
import com.xinaml.robot.ser.okex.AutoTradeSer;
import com.xinaml.robot.ser.order.OrderSer;
import com.xinaml.robot.ser.user.UserConfSer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

/**
 * @Author: [lgq]
 * @Date: [19-7-2 下午2:01]
 * @Description:
 * @Version: [1.0.0]
 * @Copy: [com.xinaml]
 */
@Login
@RestController
@RequestMapping("order")
public class OrderAct {
    @Autowired
    private OrderSer orderSer;

    @GetMapping("page")
    public ModelAndView conf() throws ActException {
        return new ModelAndView("order/order");
    }

    @GetMapping("maps")
    public Map<String, Object> maps(OrderDTO dto) throws ActException {
        try {
            if(dto.getStatus()!=null && dto.getStatus().equals("已卖出")){
                dto.addRT(RT.isNotNull("sellId"));
            }else {
                dto.addRT(RT.isNull("sellId"));
            }
            dto.addRT(RT.eq("user.id", UserUtil.getUser().getId()));
            dto.addRT(RT.eq("type", "1"));
            dto.addRT(RT.eq("status", "2"));
            dto.addSort("createDate");
            Map<String, Object> maps = orderSer.findByPage(dto);
            return maps;
        } catch (SerException e) {
            throw new ActException(e.getMessage());
        }
    }

    // DeleteMapping 这种方式接收不到数组参数，见下一方法，必须用过url来传参
    @PostMapping("del")
    public Result del(String[] ids) throws ActException {
        try {
            orderSer.remove(ids);
            return new ActResult("删除订单成功！");
        } catch (Exception e) {
            throw new ActException(e.getMessage());
        }
    }



}
