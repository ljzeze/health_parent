package com.itheima.health.service;

import com.itheima.health.exception.MyException;
import com.itheima.health.pojo.Order;

import java.util.Map;

/**
 * <p>
 *
 * </p>
 *
 * @author: Eric
 * @since: 2021/1/12
 */
public interface OrderService {
    /**
     * 预约提交
     * @param orderInfo
     * @return
     */
    Order submitOrder(Map<String, String> orderInfo) throws MyException;

    /**
     * 查询预约成功订单信息
     * @param id
     * @return
     */
    Map<String, String> findDetailById(int id);
}
