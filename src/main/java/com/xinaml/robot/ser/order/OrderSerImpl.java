package com.xinaml.robot.ser.order;

import com.xinaml.robot.base.ser.ServiceImpl;
import com.xinaml.robot.dto.order.OrderDTO;
import com.xinaml.robot.entity.order.Order;
import org.springframework.stereotype.Service;

@Service
public class OrderSerImpl extends ServiceImpl<Order, OrderDTO> implements OrderSer {
}
