package com.star.test;

import com.star.RabbitRunning;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(classes = RabbitRunning.class)
//以便在测试开始的时候自动创建Spring的应用上下文。
// 注解了@RunWith就可以直接使用spring容器，直接使用@Test注解，不用启动spring容器
@RunWith(SpringRunner.class)
public class TestMQ {

    @Autowired
    private RabbitTemplate rabbitTemplate;

//    不会根据生产者去创建，而取决于消费者

    @Test
    public void testTopic() {
        rabbitTemplate.convertSendAndReceive("topics", "user.save", "love you yifa of route");
    }

    @Test
    public void testRouting() {
        rabbitTemplate.convertSendAndReceive("directs", "info", "love you yifa of routing");
    }

    @Test
    public void testFanout() {
        rabbitTemplate.convertSendAndReceive("logs", "", "love you yifa of fanout");
    }

    @Test
    public void testWork() {
        for (int i = 0; i < 10; i++) {
            rabbitTemplate.convertAndSend("work", "love you yifa of working " + i);
        }
    }

    @Test
//    注意 队列是在消费端产生的，消费方产生hello队列，放在默认的交换机上
//    只要routingKey与这个交换机中有同名的队列，就自动路由上
    public void helloWorld() {
        rabbitTemplate.convertAndSend("hello", "love you yifa");
    }
}
