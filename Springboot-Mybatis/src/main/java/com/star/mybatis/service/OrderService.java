package com.star.mybatis.service;

import com.star.mybatis.domain.Order;
import com.star.mybatis.mapper.OrderMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author: zzStar
 * @Date: 04-09-2021 16:28
 */
@Service
public class OrderService {

    @Resource
    private OrderMapper orderMapper;

    public List<Order> findAll() {
        return orderMapper.findAll();
    }

    public Order findById(int id) {
        return orderMapper.findById(id);
    }

}
