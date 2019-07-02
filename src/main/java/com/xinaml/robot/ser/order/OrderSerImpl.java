package com.xinaml.robot.ser.order;

import com.xinaml.robot.base.ser.ServiceImpl;
import com.xinaml.robot.common.custom.exception.SerException;
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

    /**
     * 查询买入未成功的订单
     * @return
     */
    @Override
    public List<Order> findBuyUnSuccess() {
        return orderRep.findByStatusAndType(1,1);
    }

    /**
     * 查询卖出未成功的订单
     * @return
     */
    @Override
    public List<Order> findSellUnSuccess() {
        return orderRep.findByStatusAndType(1,2);
    }

    @Override
    public void remove(Order... entities) throws SerException {
        for(Order order:entities){
            order.setStatus(-6);//订单不存在
        }
        super.update(entities);
    }
}
