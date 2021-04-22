package com.star.zookeeper.queue;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.queue.DistributedPriorityQueue;
import org.apache.curator.framework.recipes.queue.QueueBuilder;
import org.apache.curator.framework.recipes.queue.QueueConsumer;
import org.apache.curator.framework.recipes.queue.QueueSerializer;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.test.TestingServer;
import org.apache.curator.utils.CloseableUtils;


/**
 * 优先级队列对队列中的元素按照优先级进行排序。Priority越小，元素越靠前，越先被消费掉
 * 注意，不建议使用ZK来做队列使用
 *
 * @Author: zzStar
 * @Date: 04-21-2021 20:13
 */
public class ZookeeperPriorityQueue {

    private static final String PATH = "/queue";

    public static void main(String[] args) throws Exception {
        TestingServer server = new TestingServer();

        CuratorFramework client = null;
        DistributedPriorityQueue<String> queue = null;

        try {
            client = CuratorFrameworkFactory.newClient(
                    server.getConnectString(), new ExponentialBackoffRetry(1000, 3)
            );
            client.getCuratorListenable().addListener(
                    (listener, event) -> System.out.println("CuratorEvent: " + event.getType().name())
            );
            client.start();

            // 消费者
            QueueConsumer<String> consumer = createQueueConsumer();

            // 创建队列
            QueueBuilder<String> producer = QueueBuilder.builder(
                    client, consumer, createQueueSerializer(), PATH
            );

            // 当优先级队列得到元素增删消息时，它会暂停处理当前的元素队列，然后刷新队列。
            // minItemsBeforeRefresh指定刷新前当前活动的队列的最小数量。主要设置你的程序可以容忍的不排序的最小值。
            queue = producer.buildPriorityQueue(0);
            queue.start();

            for (int i = 0; i < 10; i++) {
                int priority = (int) (Math.random() * 100);
                System.out.println("test -> " + i + " priority -> " + priority);
                queue.put("test -> " + i, priority);
                Thread.sleep((long) (50 * Math.random()));
            }

            Thread.sleep(1000 * 10);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            CloseableUtils.closeQuietly(queue);
            CloseableUtils.closeQuietly(client);
            CloseableUtils.closeQuietly(server);
        }
    }

    /**
     * 队列消息序列化和反序列化接口，提供了对队列中的对象的序列化和反序列化
     *
     * @return
     */
    private static QueueSerializer<String> createQueueSerializer() {
        return new QueueSerializer<String>() {
            @Override
            public byte[] serialize(String serial) {
                return serial.getBytes();
            }

            @Override
            public String deserialize(byte[] bytes) {
                return new String(bytes);
            }
        };
    }

    /**
     * 消费者
     *
     * @return
     */
    private static QueueConsumer<String> createQueueConsumer() {
        return new QueueConsumer<String>() {
            @Override
            public void consumeMessage(String message) throws Exception {
                Thread.sleep(1000);
                System.out.println("consume one message: " + message);
            }

            @Override
            public void stateChanged(CuratorFramework client, ConnectionState newState) {
                System.out.println("connection new state: " + newState.name());
            }
        };
    }

}
