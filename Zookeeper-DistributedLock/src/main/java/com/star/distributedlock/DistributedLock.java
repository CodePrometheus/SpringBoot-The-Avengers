package com.star.distributedlock;

import org.apache.log4j.spi.LoggerFactory;
import org.apache.zookeeper.*;
import org.slf4j.Logger;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * @Author: zzStar
 * @Date: 04-20-2021 19:28
 */
public class DistributedLock implements Lock, Watcher {

    private ZooKeeper zooKeeper;

    private String parentPath;

    private CountDownLatch latch = new CountDownLatch(1);

    private static ThreadLocal<String> currentNodePath = new ThreadLocal<>();

    public DistributedLock(String url, int sessionTimeout, String path) {

        this.parentPath = path;
        try {
            zooKeeper = new ZooKeeper(url, sessionTimeout, this);
            latch.await();
        } catch (Exception e) {
            System.out.println((e.getMessage()));
        }
    }

    @Override
    public void lock() {
        if (tryLock()) {
            System.out.println((Thread.currentThread().getName() + " 成功获取锁"));
        } else {
            String path = currentNodePath.get();
            synchronized (path) {
                try {
                    path.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println((Thread.currentThread().getName() + " 等待锁完成"));
            lock();
        }
    }

    @Override
    public void lockInterruptibly() {
    }

    @Override
    public boolean tryLock() {
        String path = currentNodePath.get();
        try {
            if (path == null) {
                path = zooKeeper.create(parentPath + "/", "lock".getBytes(),
                        ZooDefs.Ids.OPEN_ACL_UNSAFE,
                        CreateMode.EPHEMERAL_SEQUENTIAL);
                currentNodePath.set(path);
                System.out.println((Thread.currentThread().getName() + " 已经创建" + path));
            }

            final String currentPath = path;
            List<String> allNodes = zooKeeper.getChildren(parentPath, false);
            Collections.sort(allNodes);

            String nodeName = currentPath.substring((parentPath + "/").length());
            if (allNodes.get(0).equals(nodeName)) {
                System.out.println(Thread.currentThread().getName() + " tryLock 成功");
                return true;
            } else {
                // 监听上一个节点，防止羊群效应
                String targetNodeName = "";
                for (String node : allNodes) {
                    if (nodeName.equals(node)) {
                        break;
                    } else {
                        targetNodeName = node;
                    }
                }
                targetNodeName = parentPath + "/" + targetNodeName;

                System.out.println((Thread.currentThread().getName() + " 需要等待删除节点" + targetNodeName));

                zooKeeper.exists(targetNodeName, event -> {
                    System.out.println(("收到事件: " + event));
                    if (event.getType() == Event.EventType.NodeDeleted) {
                        synchronized (currentPath) {
                            currentPath.notify();
                        }
                        System.out.println((Thread.currentThread().getName() + " 获取到NodeDeleted通知，重新尝试获取锁"));
                    }
                });
            }
        } catch (Exception e) {
            System.out.println((e.getMessage()));
        }
        return false;
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) {
        return false;
    }

    @Override
    public void unlock() {
        String path = currentNodePath.get();
        if (path != null) {
            System.out.println((Thread.currentThread().getName() + " 释放锁"));
            currentNodePath.remove();
            try {
                zooKeeper.delete(path, -1);
            } catch (Exception e) {
                System.out.println((e.getMessage()));
            }
        }
    }

    @Override
    public Condition newCondition() {
        return null;
    }

    @Override
    public void process(WatchedEvent event) {
        System.out.println(("收到事件：" + event));
        if (event.getType() == Event.EventType.None
                && event.getState() == Event.KeeperState.SyncConnected) {
            latch.countDown();
            System.out.println((Thread.currentThread().getName() + " 已经连接成功"));
        }
    }
}
