package topic;

import com.rabbitmq.client.*;
import utils.RabbitMQUtils;

import java.io.IOException;

public class ConsumerOfTopic2 {
    public static void main(String[] args) throws IOException {
        Connection connection = RabbitMQUtils.getConnection();
        Channel channel = connection.createChannel();
        String name = "topics";
        channel.exchangeDeclare(name, "topic");
        String queue = channel.queueDeclare().getQueue();

        //        基于动态分配符的方式 绑定队列交换机
        channel.queueBind(queue, name, "#");
        channel.basicConsume(queue, new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println("1 love you chenyifa" + new String(body));
            }
        });


    }
}
