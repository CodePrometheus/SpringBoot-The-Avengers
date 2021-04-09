package com.star.mybatis.rest;

import com.github.pagehelper.PageHelper;
import com.star.mybatis.domain.Order;
import com.star.mybatis.domain.Role;
import com.star.mybatis.domain.User;
import com.star.mybatis.service.OrderService;
import com.star.mybatis.service.RoleService;
import com.star.mybatis.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author: zzStar
 * @Date: 04-09-2021 16:12
 */
@RestController
@RequestMapping
public class ApiController {

    @Resource
    private UserService userService;

    @Resource
    private OrderService orderService;

    @Resource
    private RoleService roleService;

    @GetMapping("findUserAll")
    public List<User> findUserAll() {
        return userService.findAll();
    }

    @GetMapping("findOrderAll")
    public List<Order> findAll() {
        return orderService.findAll();
    }

    @PostMapping("findOrderById/{id}")
    public Order findOrderById(@PathVariable("id") int id) {
        return orderService.findById(id);
    }

    @PostMapping("findUserById/{id}")
    public User findUserById(@PathVariable("id") int id) {
        return userService.findById(id);
    }

    @PostMapping("findRolesByUserId/{userId}")
    public List<Role> findRolesByUserId(@PathVariable("userId") int userId) {
        return roleService.findByUserId(userId);
    }

    @PostMapping("findRolesByUserName/{username}")
    public User findRolesByUserName(@PathVariable("username") String username) {
        return roleService.findByUserName(username);
    }

    @GetMapping("findAllUserPage/{start}/{size}")
    public List<User> findAllUserPage(@PathVariable("start") int start, @PathVariable("size") int size) {
        PageHelper.startPage(start, size);
        return userService.findAllPage();
    }

    /**
     * 一对多查询分页处理
     * 分步查询
     *
     * @return
     */
    @GetMapping("findUsersAndRoles/{start}/{size}")
    public List<User> findUsersAndRoles(@PathVariable("start") int start, @PathVariable("size") int size) {
        PageHelper.startPage(start, size);
        return userService.findUsersAndRoles();
    }

}
