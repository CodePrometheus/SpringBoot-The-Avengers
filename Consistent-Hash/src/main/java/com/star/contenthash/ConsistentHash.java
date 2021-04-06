package com.star.contenthash;

import java.util.Set;
import java.util.TreeMap;

/**
 * 一致性hash实现
 *
 * @Author: zzStar
 * @Date: 04-06-2021 13:46
 */
public class ConsistentHash {

    /**
     * 平均虚拟节点数
     */
    private int virtualNum = 5;

    /**
     * 采用的HASH算法
     */
    private HashAlgorithm alg = HashAlgorithm.KETAMA_HASH;

    /**
     * 节点列表
     */
    private Set<Node> nodeSet;

    /**
     * 节点hash (key,value)
     */
    private final TreeMap<Long, Node> nodeMap = new TreeMap<>();

    public ConsistentHash() {
    }

    private static class SingletonHolder {
        private static ConsistentHash instance = new ConsistentHash();
    }

    public static ConsistentHash getInstance() {
        return SingletonHolder.instance;
    }

    /**
     * 设置每个节点的虚拟节点个数，该参数默认是100
     */
    public void setVirtualNum(int virtualNum) {
        this.virtualNum = virtualNum;
    }

    /**
     * 设置一致性HASH的算法，默认采用 KETAMA_HASH
     * 对于一致性HASH而言选择的HASH算法首先要考虑发散度其次再考虑性能
     */
    public void setAlg(HashAlgorithm alg) {
        this.alg = alg;
    }

    /**
     * 配置实际的节点，允许同一个IP上多个节点，但是应该用name区分开
     */
    public void setNodeList(Set<Node> nodeList) {
        this.nodeSet = nodeList;
    }

    /**
     * 获取环形Hash
     */
    public TreeMap<Long, Node> getNodeMap() {
        return nodeMap;
    }

    /**
     * Hash环
     */
    public void buildHashCircle() {
        if (nodeSet == null) {
            return;
        }
        for (Node node : nodeSet) {
            for (int i = 0; i < virtualNum; i++) {
                long hashKey = this.alg.hash(node.toString() + "-" + i);
                nodeMap.put(hashKey, node);
            }
        }
    }

    /**
     * 沿环的顺时针找到虚拟节点
     */
    public Node findNodeByKey(String key) {
        // 计算hash
        final long hash = this.alg.hash(key);
        Long target = hash;

        // 如果环上没有对应
        if (!nodeMap.containsKey(hash)) {
            // 返回最小键大于或等于返回到给定的键
            target = nodeMap.ceilingKey(hash);
            if (target == null && !nodeMap.isEmpty()) {
                target = nodeMap.firstKey();
            }
        }
        return nodeMap.get(target);
    }
}
