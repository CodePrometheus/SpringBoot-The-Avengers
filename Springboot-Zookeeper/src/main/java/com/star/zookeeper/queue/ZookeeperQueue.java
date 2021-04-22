package com.star.zookeeper.queue;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.queue.DistributedQueue;
import org.apache.curator.framework.recipes.queue.QueueBuilder;
import org.apache.curator.framework.recipes.queue.QueueConsumer;
import org.apache.curator.framework.recipes.queue.QueueSerializer;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.test.TestingServer;
import org.apache.curator.utils.CloseableUtils;

/**
 * Curator也提供ZK Recipe的分布式队列实现。利用ZK的 PERSISTENTSEQUENTIAL节点，可以保证放入到队列中的项目是按照顺序排队的。如果单一的消费者从队列中取数据，那么它是先入先出的，这也是队列的特点。如果你严格要求顺序，你就得使用单一的消费者，可以使用leader选举只让leader作为唯一的消费者。
 * * 但是，根据Netflix的Curator作者所说，ZooKeeper真心不适合做Queue，或者说ZK没有实现一个好的Queue，详细内容可以看 Tech Note 4，原因有五：
 * * ZK有1MB 的传输限制。实践中ZNode必须相对较小，而队列包含成千上万的消息，非常的大
 * * 如果有很多节点，ZK启动时相当的慢。而使用queue会导致好多ZNode。你需要显著增大 initLimit 和 syncLimit
 * * ZNode很大的时候很难清理。Netflix不得不创建了一个专门的程序做这事
 * * 当很大量的包含成千上万的子节点的ZNode时，ZK的性能变得不好
 * * ZK的数据库完全放在内存中。大量的Queue意味着会占用很多的内存空间
 * * 尽管如此，Curator还是创建了各种Queue的实现。如果Queue的数据量不太多，数据量不太大的情况下，酌情考虑，还是可以使用的。
 * *
 *
 * @Author: zzStar
 * @Date: 04-22-2021 23:37
 */
public class ZookeeperQueue {

    private static final String PATH = "/queue";

    /**
     * 定义了两个分布式队列和两个消费者，因为PATH是相同的，会存在消费者抢占消费消息的情况
     */
    public static void main(String[] args) throws Exception {
        TestingServer server = new TestingServer();

        CuratorFramework clientA = CuratorFrameworkFactory.newClient(
                server.getConnectString(), new ExponentialBackoffRetry(1000, 3));
        clientA.start();

        CuratorFramework clientB = CuratorFrameworkFactory.newClient(
                server.getConnectString(), new ExponentialBackoffRetry(1000, 3));
        clientB.start();

        DistributedQueue<String> queueA;
        QueueBuilder<String> builderA = QueueBuilder.builder(
                clientA, createQueueConsumer("A"), createQueueSerializer(), PATH);
        queueA = builderA.buildQueue();
        queueA.start();

        DistributedQueue<String> queueB;
        QueueBuilder<String> builderB = QueueBuilder.builder(
                clientB, createQueueConsumer("B"), createQueueSerializer(), PATH);
        queueB = builderB.buildQueue();
        queueB.start();

        for (int i = 0; i < 20; i++) {
            queueA.put("test-A-" + i);
            Thread.sleep(100);
            queueB.put("test-B-" + i);
        }
        // 等待消息消费完成
        Thread.sleep(1000 * 10);

        queueB.close();
        queueA.close();
        clientB.close();
        clientA.close();
        System.out.println("OK!");

        CloseableUtils.closeQuietly(server);
    }

    /**
     * 队列消息序列化实现类
     */
    private static QueueSerializer<String> createQueueSerializer() {
        return new QueueSerializer<String>() {
            @Override
            public byte[] serialize(String item) {
                return item.getBytes();
            }

            @Override
            public String deserialize(byte[] bytes) {
                return new String(bytes);
            }
        };
    }

    /**
     * 定义队列消费者
     */
    private static QueueConsumer<String> createQueueConsumer(final String name) {
        return new QueueConsumer<String>() {
            @Override
            public void stateChanged(CuratorFramework client, ConnectionState newState) {
                System.out.println("连接状态改变: " + newState.name());
            }

            @Override
            public void consumeMessage(String message) throws Exception {
                System.out.println("消费消息(" + name + "): " + message);
            }
        };
    }
}
