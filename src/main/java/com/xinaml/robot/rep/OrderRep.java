package com.xinaml.robot.rep;

import com.xinaml.robot.base.rep.JapRep;
import com.xinaml.robot.dto.order.OrderDTO;
import com.xinaml.robot.entity.order.Order;

import java.util.List;

public interface OrderRep extends JapRep<Order, OrderDTO> {
    Order findByOrderId(String orderId);
    List<Order> findByStatusAndType(Integer status,Integer type);
}
