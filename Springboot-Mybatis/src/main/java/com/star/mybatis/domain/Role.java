package com.star.mybatis.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: zzStar
 * @Date: 04-09-2021 15:57
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Role {

    private Integer id;

    private String name;

    private String desc;

}
