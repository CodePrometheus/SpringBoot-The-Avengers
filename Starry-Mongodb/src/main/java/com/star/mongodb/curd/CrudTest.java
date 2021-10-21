package com.star.mongodb.curd;

import com.mongodb.client.result.UpdateResult;
import com.star.mongodb.domain.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author: zzStar
 * @Date: 10-21-2021 17:14
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class CrudTest {

    /**
     * 插入： save()
     * 查询： findAll()、find()、findOne()
     * 更新：update()
     * 删除: remove()
     */
    @Resource
    private MongoTemplate mongoTemplate;

    @Test
    public void add() {
        User user = User.builder().id(System.currentTimeMillis())
                .name("starry").addr("localhost").build();
        mongoTemplate.save(user);
    }

    @Test
    public void find() {
        Query query = new Query();
        query.addCriteria(Criteria.where("name").is("starry"));
        List<User> users = mongoTemplate.find(query, User.class);
        System.out.println("users = " + users);
    }

    @Test
    public void update() {
        Query query = new Query().addCriteria(Criteria.where("name").is("starry"));
        Update update = new Update().set("name", "star");
        UpdateResult result = mongoTemplate.updateFirst(query, update, User.class);
        System.out.println("result = " + result);
    }
}
