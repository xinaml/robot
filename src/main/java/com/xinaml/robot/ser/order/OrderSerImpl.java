package com.xinaml.robot.ser.order;

import com.xinaml.robot.base.ser.ServiceImpl;
import com.xinaml.robot.common.custom.exception.SerException;
import com.xinaml.robot.common.utils.StringUtil;
import com.xinaml.robot.dto.order.OrderDTO;
import com.xinaml.robot.entity.order.Order;
import com.xinaml.robot.rep.OrderRep;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderSerImpl extends ServiceImpl<Order, OrderDTO> implements OrderSer {

    @Autowired
    private OrderRep orderRep;

    public Order findByOrderId(String orderId) {
        return orderRep.findByOrderId(orderId);
    }

    /**
     * 查询买入未成功的订单
     *
     * @return
     */
    @Cacheable(value = "orders", key = "#root.methodName.concat(#userId)")
    @Override
    public List<Order> findBuyUnSuccess(String userId) {
        if (StringUtils.isBlank(userId)) {
            return orderRep.findUnSuccess(1);
        } else {
            return orderRep.findUnSuccess(userId, 1);
        }
    }

    /**
     * 查询卖出未成功的订单
     *
     * @return
     */
    @Cacheable(value = "orders", key = "#root.methodName.concat(#userId)")
    @Override
    public List<Order> findSellUnSuccess(String userId) {
        if (StringUtils.isBlank(userId)) {
            return orderRep.findUnSuccess(2);
        } else {
            return orderRep.findUnSuccess(userId, 2);
        }
    }

    @Cacheable(value = "orders", key = "#root.methodName.concat(#userId)")
    @Override
    public List<Order> findBuySuccess(String userId) {
        if (StringUtils.isBlank(userId)) {
            return orderRep.findSuccess(1);
        } else {
            return orderRep.findSuccess(userId, 1);

        }
    }

    @Cacheable(value = "orders", key = "#root.methodName.concat(#userId)")
    @Override
    public List<Order> findSellSuccess(String userId) {
        if (null == userId) {
            return orderRep.findSuccess(2);
        } else {
            return orderRep.findSuccess(userId, 2);

        }
    }

    @CacheEvict(value = "orders", beforeInvocation = true, allEntries = true)
    @Override
    public void remove(Order... entities) throws SerException {
        for (Order order : entities) {
            order.setStatus(-6);//订单不存在
        }
        super.update(entities);
    }

    @CacheEvict(value = "orders", beforeInvocation = true, allEntries = true)
    @Override
    public void save(Order... entities) throws SerException {
        super.save(entities);
    }

    @CacheEvict(value = "orders", beforeInvocation = true, allEntries = true)
    @Override
    public void update(List<Order> entities) throws SerException {
        super.update(entities);
    }

    @CacheEvict(value = "orders", beforeInvocation = true, allEntries = true)
    @Override
    public void update(Order... entities) throws SerException {
        super.update(entities);
    }
}


