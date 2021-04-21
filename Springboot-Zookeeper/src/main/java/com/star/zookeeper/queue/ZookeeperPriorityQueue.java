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
 * Curator也提供ZK Recipe的分布式队列实现。利用ZK的 PERSISTENTSEQUENTIAL节点，可以保证放入到队列中的项目是按照顺序排队的。如果单一的消费者从队列中取数据，那么它是先入先出的，这也是队列的特点。如果你严格要求顺序，你就得使用单一的消费者，可以使用leader选举只让leader作为唯一的消费者。但是，根据Netflix的Curator作者所说，ZooKeeper真心不适合做Queue，或者说ZK没有实现一个好的Queue，详细内容可以看 Tech Note 4，原因有五：
 * ZK有1MB 的传输限制。实践中ZNode必须相对较小，而队列包含成千上万的消息，非常的大
 * 如果有很多节点，ZK启动时相当的慢。而使用queue会导致好多ZNode。你需要显著增大 initLimit 和 syncLimit
 * ZNode很大的时候很难清理。Netflix不得不创建了一个专门的程序做这事
 * 当很大量的包含成千上万的子节点的ZNode时，ZK的性能变得不好
 * ZK的数据库完全放在内存中。大量的Queue意味着会占用很多的内存空间
 * 尽管如此，Curator还是创建了各种Queue的实现。如果Queue的数据量不太多，数据量不太大的情况下，酌情考虑，还是可以使用的。
 * <p>
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
