package com.star.contenthash;

/**
 * 节点的IP
 *
 * @Author: zzStar
 * @Date: 04-06-2021 13:34
 */
public class Node {

    private String name;

    private String ip;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Node(String name, String ip) {
        this.name = name;
        this.ip = ip;
    }

    @Override
    public String toString() {
        if (name != null && !"".equals(name)) {
            return ip + "-" + name;
        }
        return ip;
    }

    public Node(String ip) {
        this.ip = ip;
    }

    /**
     * 重写equals是实现业务场景下，实例之间在业务逻辑上是否相等。
     * 重写hashcode是为了让集合快速判重
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        Node node = (Node) obj;
        if (node.getIp() == null && ip == null && node.getName() == null && name == null) {
            return true;
        }
        if (name == null && node.getName() != null) {
            return false;
        }
        if (ip == null && node.getIp() != null) {
            return false;
        }
        assert ip != null;
        assert name != null;
        return name.equals(node.getName()) && ip.equals(node.getIp());
    }

    @Override
    public int hashCode() {
        int res = name != null ? name.hashCode() : 0;
        res = 31 * res + (ip != null ? ip.hashCode() : 0);
        return res;
    }
}
