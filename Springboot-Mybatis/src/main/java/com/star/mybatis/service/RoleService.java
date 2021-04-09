package com.star.mybatis.service;

import com.star.mybatis.domain.Role;
import com.star.mybatis.domain.User;
import com.star.mybatis.mapper.RoleMapper;
import com.star.mybatis.mapper.UserMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author: zzStar
 * @Date: 04-09-2021 18:09
 */
@Service
public class RoleService {

    @Resource
    private RoleMapper roleMapper;

    @Resource
    private UserMapper userMapper;

    public List<Role> findByUserId(int userId) {
        return roleMapper.findByUserId(userId);
    }

    public User findByUserName(String name) {
        return userMapper.findByName(name);
    }

}
