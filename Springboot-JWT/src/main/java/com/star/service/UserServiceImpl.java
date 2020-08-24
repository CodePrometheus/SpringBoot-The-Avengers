package com.star.service;


import com.star.dao.UserDAO;
import com.star.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDAO userDAO;

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public User login(User user) {
//        根据接收用户密码查询数据库
        User login = userDAO.login(user);
        if (login != null) {
            return login;
        }
        throw new RuntimeException("登录失败");
    }
}
