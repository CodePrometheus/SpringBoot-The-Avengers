package helloworld;

import com.rabbitmq.client.*;
import utils.RabbitMQUtils;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Consumer {

    public static void main(String[] args) throws IOException, TimeoutException {
/*
        //        绑定同一个虚拟主机，消息队列
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("");
        connectionFactory.setPort(5672);
        connectionFactory.setVirtualHost("/yifa");
        connectionFactory.setUsername("yifa");
        connectionFactory.setPassword("1");
        Connection connection = connectionFactory.newConnection();
*/

        Connection connection = RabbitMQUtils.getConnection();
        Channel channel = connection.createChannel();
//        需要保持一致
        channel.queueDeclare("yifa", false, false, false, null);

        //队列名 消息自动确认机制 消费时回调接口
        channel.basicConsume("yifa", true, new DefaultConsumer(channel) {

            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println("yifa " + new String(body));
            }
        });

    }


}
