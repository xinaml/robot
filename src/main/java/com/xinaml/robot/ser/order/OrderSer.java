package com.xinaml.robot.ser.order;

import com.xinaml.robot.base.ser.Ser;
import com.xinaml.robot.dto.order.OrderDTO;
import com.xinaml.robot.entity.order.Order;

import java.util.List;

public interface OrderSer extends Ser<Order, OrderDTO> {
    default Order findByOrderId(String orderId){
        return null;
    }

    /**
     * 查询未成功的订单
     * @return
     */
    default List<Order> findUnSuccess(){
        return null;
    }
}
