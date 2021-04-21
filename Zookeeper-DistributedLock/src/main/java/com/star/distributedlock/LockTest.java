package com.star.distributedlock;

/**
 * @Author: zzStar
 * @Date: 04-20-2021 20:01
 */
public class LockTest {

    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                DistributedLock distributedLock = new DistributedLock("localhost:2181", 3000, "/lock");
                try {
                    distributedLock.lock();
                    System.out.println(Thread.currentThread().getName() + "开始执行");
                    Thread.sleep(1000);
                    System.out.println((Thread.currentThread().getName() + "执行完成"));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    distributedLock.unlock();
                }
            }).start();
        }
    }

}
