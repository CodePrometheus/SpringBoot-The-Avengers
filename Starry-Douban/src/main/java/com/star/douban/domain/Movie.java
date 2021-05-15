package com.star.douban.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: zzStar
 * @Date: 05-15-2021 12:30
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Movie {

    private String title;

    private String mUrl;

    private String img;

    private String score;

    private String detail;

}
