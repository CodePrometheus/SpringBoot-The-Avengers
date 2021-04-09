package com.star.mybatis.service;

import com.star.mybatis.domain.User;
import com.star.mybatis.mapper.UserMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author: zzStar
 * @Date: 04-09-2021 16:10
 */
@Service
public class UserService {

    @Resource
    private UserMapper userMapper;


    public List<User> findAll() {
        List<User> list = userMapper.findAll();
        return list;
    }

    public User findById(int id) {
        return userMapper.findById(id);
    }

    public List<User> findAllPage() {
        return userMapper.findAll();
    }

    public List<User> findUsersAndRoles() {
        return userMapper.findUsersAndRoles();
    }
}
