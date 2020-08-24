package com.star.dao;

import com.star.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserDAO {

    User login(User user);

}
