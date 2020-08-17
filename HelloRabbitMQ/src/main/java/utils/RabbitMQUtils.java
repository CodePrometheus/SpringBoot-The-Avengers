package utils;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class RabbitMQUtils {

    //    避免每拿一次连接就创建一次工厂
    private static ConnectionFactory connectionFactory;

    static {
//        类加载只执行一次
        connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("");
        connectionFactory.setPort(5672);
        connectionFactory.setVirtualHost("/yifa");
        connectionFactory.setUsername("yifa");
        connectionFactory.setPassword("1");
    }


    //    封装
    public static Connection getConnection() {
        try {
            return connectionFactory.newConnection();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
        return null;
    }

    //    关闭通道和关闭连接工具方法
    public static void closeConnectionAndChannel(Connection connection, Channel channel) {
        try {
            if (channel != null) {
                channel.close();
            }
            if (connection != null) {
                connection.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }
}
