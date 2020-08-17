package direct;

import com.rabbitmq.client.*;
import utils.RabbitMQUtils;

import java.io.IOException;

public class ConsumerOfDirect1 {
    public static void main(String[] args) throws IOException {
        Connection connection = RabbitMQUtils.getConnection();
        Channel channel = connection.createChannel();

        String name = "logs-direct";

//        通道声明交换机以及交换的类型
        channel.exchangeDeclare(name, "direct");
//        同样获取一个临时队列
        String queue = channel.queueDeclare().getQueue();
//        基于rounteKey绑定队列和交换机
        channel.queueBind(queue, name, "error");
//        获取消费
        channel.basicConsume(queue, new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println("1 love you chenyifa" + new String(body));
            }
        });
    }
}
