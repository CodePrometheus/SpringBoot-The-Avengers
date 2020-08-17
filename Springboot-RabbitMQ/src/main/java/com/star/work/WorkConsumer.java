package com.star.work;

import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class WorkConsumer {

    //    一个消费者
    @RabbitListener(queuesToDeclare = @Queue(value = "work"))
    public void receive1(String message) {
        System.out.println("No.1 message = " + message);
    }

    //    一个消费者
    @RabbitListener(queuesToDeclare = @Queue(value = "work"))
    public void receive2(String message) {
        System.out.println("No.2 message = " + message);
    }


}
