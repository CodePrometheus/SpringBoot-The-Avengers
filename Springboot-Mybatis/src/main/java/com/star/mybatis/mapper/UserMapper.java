package com.star.mybatis.mapper;

import com.star.mybatis.domain.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Author: zzStar
 * @Date: 04-09-2021 16:01
 */
@Mapper
public interface UserMapper {

    /**
     * findAll
     *
     * @return
     */
    List<User> findAll();

    /**
     * 用户信息以及对应的角色信息
     * 一对多
     *
     * @param id
     * @return
     */
    User findById(Integer id);

    /**
     * name->User&User.roles
     *
     * @param name
     * @return
     */
    User findByName(String name);

    /**
     * 用户以及对应的角色信息全部
     *
     * @return
     */
    List<User> findUsersAndRoles();
}
