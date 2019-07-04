package com.xinaml.robot.rep;

import com.xinaml.robot.base.rep.JapRep;
import com.xinaml.robot.dto.order.OrderDTO;
import com.xinaml.robot.entity.order.Order;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderRep extends JapRep<Order, OrderDTO> {
    Order findByOrderId(String orderId);

    @Query(value = "select a.* from tb_order a  where  user_id=?1 and type=?2  and status='2' and sell_id is null ",nativeQuery = true)
    List<Order> findSuccess(String userId, Integer type);

    @Query(value = "select a.* from tb_order a  where   type=?1  and status='2' and sell_id is null ",nativeQuery = true)
    List<Order> findSuccess( Integer type);

    @Query(value = "select a.* from tb_order a  where   status in('0','1','3','4') and  type=?1 and sell_id is null",nativeQuery = true)
    List<Order> findUnSuccess(Integer type);

    @Query(value = "select a.* from tb_order a  where  status in('0','1','3','4') and user_id=?1 and type=?2 and sell_id is null",nativeQuery = true)
    List<Order> findUnSuccess(String userId, Integer type);
}
