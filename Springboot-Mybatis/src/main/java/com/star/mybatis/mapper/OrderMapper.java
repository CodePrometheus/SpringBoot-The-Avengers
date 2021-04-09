package com.star.mybatis.mapper;

import com.star.mybatis.domain.Order;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Author: zzStar
 * @Date: 04-09-2021 16:00
 */
@Mapper
public interface OrderMapper {

    /**
     * findAll
     *
     * @return
     */
    List<Order> findAll();


    /**
     * 订单和对应的用户信息
     *
     * @param id
     * @return
     */
    Order findById(Integer id);

}
