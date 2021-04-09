package com.star.mybatis.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: zzStar
 * @Date: 04-09-2021 15:23
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    private Integer id;

    private String userId;

    private String price;

    private String createTime;

    private String remark;

    private User user;

}
