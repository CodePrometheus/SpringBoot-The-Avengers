package com.star.mybatis.mapper;

import com.star.mybatis.domain.Role;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Author: zzStar
 * @Date: 04-09-2021 18:04
 */
@Mapper
public interface RoleMapper {

    /**
     * user.id -> roles
     *
     * @param userId
     * @return
     */
    List<Role> findByUserId(Integer userId);

}
