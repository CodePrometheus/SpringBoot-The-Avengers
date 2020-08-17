package fanout;

import com.rabbitmq.client.*;
import utils.RabbitMQUtils;

import java.io.IOException;

public class ConsumerOfFanout3 {

    public static void main(String[] args) throws IOException {
        Connection connection = RabbitMQUtils.getConnection();
        Channel channel = connection.createChannel();

//        通道绑定交换机,对应于provider
        channel.exchangeDeclare("logs", "fanout");
//        临时队列
        String queue = channel.queueDeclare().getQueue();
//        绑定 交换机 队列 路由key
        channel.queueBind(queue, "logs", "");
//        消费消息
        channel.basicConsume(queue, true, new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println("3 love you chenyifa" + new String(body));
            }
        });
    }
}
