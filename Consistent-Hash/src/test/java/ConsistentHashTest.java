import com.star.contenthash.ConsistentHash;
import com.star.contenthash.Node;
import org.junit.Test;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: zzStar
 * @Date: 04-06-2021 13:59
 */
public class ConsistentHashTest {

    final ConcurrentHashMap<String, Long> stat = new ConcurrentHashMap<>();

    @Test
    public void testHash() throws InterruptedException {
        ConsistentHashTest hashTest = new ConsistentHashTest();
        Set<Node> ips = new HashSet<>();
        ips.add(new Node("192.168.10.1"));
        ips.add(new Node("192.168.10.2"));
        ips.add(new Node("192.168.10.3"));
        ips.add(new Node("192.168.10.4"));
        ips.add(new Node("192.168.10.5"));
        ips.add(new Node("192.168.10.6"));
        ips.add(new Node("192.168.10.7"));
        ips.add(new Node("192.168.10.8"));
        ips.add(new Node("192.168.10.9"));
        ips.add(new Node("192.168.1.10"));

        ConsistentHash hash = ConsistentHash.getInstance();
        hash.setNodeList(ips);
        hash.buildHashCircle();

        long start = System.currentTimeMillis();

        for (int i = 0; i < 10; i++) {
            String name = "thread" + i;
            Thread thread = new Thread(() -> {
                for (int j = 0; j < 2; j++) {
                    Node node = hash.findNodeByKey(name + j);
                    hashTest.send(node);
                }
                hashTest.print();
            }, name);
            thread.start();
        }
        System.out.println(System.currentTimeMillis() - start);
        Thread.sleep(1000 * 20);
        hashTest.print();
        System.exit(0);
    }

    private void print() {
        long all = 0;
        for (Map.Entry<String, Long> entry : stat.entrySet()) {
            long num = entry.getValue();
            all += num;
            System.out.println("mac:" + entry.getKey() + " hits:" + num);
        }
        System.out.println("all：" + all);
    }

    private synchronized void send(Node node) {
        Long count = stat.get(node.getIp());
        if (count == null) {
            stat.put(node.getIp(), 1L);
        } else {
            stat.put(node.getIp(), count + 1);
        }
    }

}
