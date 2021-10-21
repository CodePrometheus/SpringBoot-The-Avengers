package com.star.mongodb.domain;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.MongoId;

/**
 * @Author: zzStar
 * @Date: 10-21-2021 17:16
 */
@Data
@Builder
@Document("user")
public class User {

    @MongoId
    private Long id;

    @Field
    private String name;

    @Field
    private String addr;

}
