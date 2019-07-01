package com.xinaml.robot.ser.order;

import com.xinaml.robot.base.ser.ServiceImpl;
import com.xinaml.robot.dto.order.OrderDTO;
import com.xinaml.robot.entity.order.Order;
import com.xinaml.robot.rep.OrderRep;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderSerImpl extends ServiceImpl<Order, OrderDTO> implements OrderSer {
    @Autowired
    private OrderRep orderRep;
    public Order findByOrderId(String orderId){
        return orderRep.findByOrderId(orderId);
    }

    @Override
    public List<Order> findUnSuccess() {
        return orderRep.findByStatus(1);
    }
}
