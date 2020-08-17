package com.star.route;

import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class RouteConsumer {

    @RabbitListener(bindings = {
            @QueueBinding(
                    value = @Queue,
                    exchange = @Exchange(name = "directs", type = "direct"),//名称与类型
                    key = {"info", "error", "warn"}
            )
    })
    public void receive1(String message) {
        System.out.println("No.1 message = " + message);
    }

    @RabbitListener(bindings = {
            @QueueBinding(
                    value = @Queue,
                    exchange = @Exchange(name = "directs", type = "direct"),//名称与类型
                    key = {"error"}
            )
    })
    public void receive2(String message) {
        System.out.println("No.2 message = " + message);
    }

}
