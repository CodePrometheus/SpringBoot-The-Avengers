package workquene;

import com.rabbitmq.client.*;
import utils.RabbitMQUtils;

import java.io.IOException;

public class ConsumerOfWork2 {
    public static void main(String[] args) throws IOException {
        Connection connection = RabbitMQUtils.getConnection();
        Channel channel = connection.createChannel();
        channel.basicQos(1);
        channel.queueDeclare("work", true, false, false, null);
//        获取消息
        channel.basicConsume("work", false, new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println("2love you yifa" + new String(body));
//                手动确认  参数1手动确认消息标识    参数2 false每次确认一个
                channel.basicAck(envelope.getDeliveryTag(), false);
            }
        });
    }
}
