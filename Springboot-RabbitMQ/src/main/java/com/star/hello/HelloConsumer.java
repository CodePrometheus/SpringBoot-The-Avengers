package com.star.hello;

import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
//代表是一个消费者,并可设置队列一系列参数
@RabbitListener(queuesToDeclare = @Queue(value = "hello", durable = "false", autoDelete = "true"))
public class HelloConsumer {

    @RabbitHandler//代表对队列取出消息时所执行的方法
    public void receive(String message) {
        System.out.println("message = " + message);
    }

}
