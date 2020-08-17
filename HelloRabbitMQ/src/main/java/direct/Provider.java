package direct;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import utils.RabbitMQUtils;

import java.io.IOException;

public class Provider {
    public static void main(String[] args) throws IOException {
        Connection connection = RabbitMQUtils.getConnection();
        Channel channel = connection.createChannel();

        String name = "logs-direct";

//        交换机名称 路由模式
        channel.exchangeDeclare(name, "direct");
//        发送消息
        String routingKey = "info";
        channel.basicPublish(name, routingKey, null, (" direct type of " + routingKey + " massage").getBytes());
        RabbitMQUtils.closeConnectionAndChannel(connection, channel);
    }
}
