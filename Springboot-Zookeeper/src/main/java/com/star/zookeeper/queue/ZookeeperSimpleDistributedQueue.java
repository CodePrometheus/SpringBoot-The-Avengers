package com.star.zookeeper.queue;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.queue.SimpleDistributedQueue;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.test.TestingServer;
import org.apache.curator.utils.CloseableUtils;

/**
 * 分布式场景下的阻塞队列
 * SimpleDistributedQueue提供了和JDK基本一致的接口(但是没有实现Queue接口)
 *
 * @Author: zzStar
 * @Date: 04-22-2021 23:40
 */
public class ZookeeperSimpleDistributedQueue {

    private static final String PATH = "/queue";

    public static void main(String[] args) throws Exception {
        TestingServer server = new TestingServer();
        CuratorFramework client = null;
        SimpleDistributedQueue queue;

        try {
            client = CuratorFrameworkFactory.newClient(
                    server.getConnectString(), new ExponentialBackoffRetry(1000, 3));
            client.getCuratorListenable().addListener(
                    (client1, event) -> System.out.println("CuratorEvent: " + event.getType().name()));
            client.start();

            queue = new SimpleDistributedQueue(client, PATH);
            Producer producer = new Producer(queue);
            Consumer consumer = new Consumer(queue);

            new Thread(producer, "producer").start();
            new Thread(consumer, "consumer").start();
            Thread.sleep(10000);
        } catch (Exception ex) {

        } finally {
            CloseableUtils.closeQuietly(client);
            CloseableUtils.closeQuietly(server);
        }
    }

    public static class Producer implements Runnable {

        private SimpleDistributedQueue queue;

        public Producer(SimpleDistributedQueue queue) {
            this.queue = queue;
        }

        @Override
        public void run() {
            for (int i = 0; i < 100; i++) {
                try {
                    boolean flag = queue.offer(("zjc-" + i).getBytes());
                    if (flag) {
                        System.out.println("发送一条消息成功：" + "zjc-" + i);
                    } else {
                        System.out.println("发送一条消息失败：" + "zjc-" + i);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static class Consumer implements Runnable {

        private SimpleDistributedQueue queue;

        public Consumer(SimpleDistributedQueue queue) {
            this.queue = queue;
        }

        @Override
        public void run() {
            for (int i = 0; i < 100; i++) {
                try {
                    byte[] data = queue.take();
                    System.out.println("消费一条消息成功：" + new String(data, "UTF-8"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
