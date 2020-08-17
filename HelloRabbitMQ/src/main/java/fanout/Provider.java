package fanout;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import utils.RabbitMQUtils;

import java.io.IOException;

public class Provider {
    public static void main(String[] args) throws IOException {
        Connection connection = RabbitMQUtils.getConnection();
        Channel channel = connection.createChannel();

//        将通道使用指定的交换机  名称 类型
        channel.exchangeDeclare("logs", "fanout");
//        发送消息
        channel.basicPublish("logs", "", null, " give you this fanout type massage".getBytes());
        RabbitMQUtils.closeConnectionAndChannel(connection, channel);
    }
}
