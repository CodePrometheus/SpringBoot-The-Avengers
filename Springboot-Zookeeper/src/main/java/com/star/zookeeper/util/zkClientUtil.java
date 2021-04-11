package com.star.zookeeper.util;

import lombok.extern.slf4j.Slf4j;
import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.serialize.SerializableSerializer;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

/**
 * 获取客户端对象
 *
 * @Author: zzStar
 * @Date: 04-11-2021 14:25
 */
@Slf4j
public class zkClientUtil {

    private ZkClient client;

    @Before
    public void before() {
        client = new ZkClient("192.168.43.205:2183,192.168.43.205:2182,192.168.43.205:2181", 10000 * 30, 10000, new SerializableSerializer());
    }

    @After
    public void after() throws InterruptedException {
        Thread.sleep(5000);
        client.close();
        log.info("关闭连接");
    }


    @Test
    public void testCreateNode() {
        try {
            // 持久节点
            client.create("/node1", "PERSISTENT", CreateMode.PERSISTENT);
            // 持久顺序节点
            client.create("/node1/node2", "PERSISTENT_SEQUENTIAL", CreateMode.PERSISTENT_SEQUENTIAL);
            // 临时节点
            client.create("/node1/list", "EPHEMERAL", CreateMode.EPHEMERAL);
            // 临时顺序节点
            client.create("/node1/map", "EPHEMERAL_SEQUENTIAL", CreateMode.EPHEMERAL_SEQUENTIAL);
        } catch (RuntimeException e) {
            log.debug(String.valueOf(e));
        }
    }

    @Test
    public void testRmrNode() {
        boolean delete = client.deleteRecursive("/node1");
        System.out.println(delete);
    }

    @Test
    public void testFindNodes() {
        List<String> childrenList = client.getChildren("/node1");
        for (String child : childrenList) {
            System.out.println(child);
        }
    }

    @Test
    public void testFindNodeData() {
        String data = client.readData("/node1");
        System.out.println(data);
    }

    @Test
    public void testFindNodeStat() {
        Stat stat = new Stat();
        String data = client.readData("/node1", stat);
        System.out.println(stat.getVersion());
        System.out.println(stat.getNumChildren());
        System.out.println(stat.getCtime());
        System.out.println(stat.getMtime());
        System.out.println(data);
    }

    @Test
    public void testUpdateNodeData() {
        client.writeData("/node1", "XUANYA");
    }


    /**
     * 永久监听
     *
     * @throws IOException
     */
    @Test
    public void testWatchDataChange() throws IOException {
        client.subscribeDataChanges("/node1", new IZkDataListener() {
            // 当前节点数据变化时触发
            @Override
            public void handleDataChange(String dataPath, Object data) throws Exception {
                System.out.println("当前节点路径" + dataPath);
                System.out.println("当前节点变化后的数据" + data);
            }

            // 当前节点删除触发
            @Override
            public void handleDataDeleted(String dataPath) throws Exception {
                System.out.println("当前节点路径" + dataPath);
            }
        });
        // 阻塞当前监听
        System.in.read();
    }


    @Test
    public void testOnNodesChange() throws IOException {
        client.subscribeChildChanges("/node1", new IZkChildListener() {
            @Override
            public void handleChildChange(String nodeName, List<String> list) throws Exception {
                System.out.println(("父节点名称" + nodeName));
                System.out.println(("发生变更后节点孩子的名称"));
                for (String name : list) {
                    System.out.println(name);
                }
            }
        });
        System.in.read();
    }


}
