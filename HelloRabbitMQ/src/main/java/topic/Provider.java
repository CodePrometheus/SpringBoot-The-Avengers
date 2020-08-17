package topic;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import utils.RabbitMQUtils;

import java.io.IOException;

public class Provider {
    public static void main(String[] args) throws IOException {
        Connection connection = RabbitMQUtils.getConnection();
        Channel channel = connection.createChannel();
        String name = "topics";
        channel.exchangeDeclare(name, "topic");
        String routingKey = "save.user.find";
        channel.basicPublish(name, routingKey, null, (" topic type of " + routingKey + " massage").getBytes());
        RabbitMQUtils.closeConnectionAndChannel(connection, channel);
    }
}
