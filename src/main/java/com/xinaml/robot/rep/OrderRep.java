package com.xinaml.robot.rep;

import com.xinaml.robot.base.rep.JapRep;
import com.xinaml.robot.dto.order.OrderDTO;
import com.xinaml.robot.entity.order.Order;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderRep extends JapRep<Order, OrderDTO> {
    Order findByOrderId(String orderId);

    @Query("from Order where user.id=?1 and type=?2  and status='2' and sellId is null ")
    List<Order> findSuccess(String userId, Integer type);

    @Query("from Order where  type=?1 and sellId is null")
    List<Order> findSuccess(Integer type);

    @Query("from Order where   status <>'2' and type=?1 and sellId is null")
    List<Order> findUnSuccess(Integer type);

    @Query("from Order where   status<>'2' and user.id=?1 and type=?2 and sellId is null")
    List<Order> findUnSuccess(String userId, Integer type);
}
