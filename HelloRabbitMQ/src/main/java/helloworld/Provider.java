package helloworld;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.MessageProperties;
import org.junit.jupiter.api.Test;
import utils.RabbitMQUtils;


public class Provider {

    @Test
    public void testSendMessage() throws Exception {

/*
//        创建连接mq的连接工厂对象
        ConnectionFactory connectionFactory = new ConnectionFactory();
//        设置连接rabbitmq主机
        connectionFactory.setHost("");
//        设置端口号
        connectionFactory.setPort(5672);
//        设置所要连接虚拟主机
        connectionFactory.setVirtualHost("/yifa");
//        设置访问虚拟主机用户名和密码
        connectionFactory.setUsername("yifa");
        connectionFactory.setPassword("1");

        //        获取连接对象
        Connection connection = connectionFactory.newConnection();
*/


//        通过工具类获取连接对象
        Connection connection = RabbitMQUtils.getConnection();


//        连接通过通道获取信息
        Channel channel = connection.createChannel();

//        通道要绑定消息队列
//        参数1 队列的名称在不存在的情况下自动创建
//        参数2 durable队列 是否要持久化
//        参数3 exclusive是否独占队列
//        参数4 autoDelete 是否在消费完成后自动删除队列
        channel.queueDeclare("yifa", true, false, false, null);

//        发布消息  到一个具体的队列
//        交换及名称  队列名称  传递消息额外设置(消息持久化)  消息的具体内容
        channel.basicPublish("", "yifa", MessageProperties.PERSISTENT_TEXT_PLAIN, "love you".getBytes());

/*
        channel.close();
        connection.close();
*/
        RabbitMQUtils.closeConnectionAndChannel(connection, channel);
    }

}
