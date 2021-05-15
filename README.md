SpringBoot-The-Avengers
======
Springboot与时下最新流行技术的整合学习记录
<br/>
以及Java相关技术点的实现

#### I can do this all day.

#### I am Iron Man

![image](https://pic4.zhimg.com/80/v2-93f36044d20d3b3c8803474d96c8e1ad_1440w.jpg?source=1940ef5c)

![image](https://pic2.zhimg.com/80/v2-d04ec9628faf75b94ffa7c349d0847dc_1440w.jpg?source=1940ef5c)

没错，接受SpringBoot熊熊燃烧的大火吧

---

## 简介

该仓库为SpringBoot2.4.3与其他相关技术的整合


## 内容

#### Springboot + RabbitMQ
- RabbitMQ常用几大模型的使用，包括Hello World,Work queues,Routing,Topics
- Springboot整合MQ的使用

#### Springboot + JWT
- JWT前后端分离系统的认证方案，单点登录系统的认证
- JWT整合Springboot完成认证以及认证优化
- 基于客户端的存储认证标记的解决方案

#### Springboot + Swagger
- swagger3的使用，加入Token授权相关
- http://localhost:8080/swagger-ui/index.html

#### Springboot + Redis
- 基于注解与Token方案实现接口幂等性
- 一次和多次请求某一个资源对于资源本身应该具有同样的结果（网络超时等问题除外）。也就是说，其任意多次执行对资源本身所产生的影响均与一次执行的影响相同

#### 一致性哈希的实现
- 虚拟节点处理哈希偏斜问题

#### 线程池异步记录日志
- 切面实现

#### Springboot + Mybatis
- 单表查询
- 多表查询 (一对一，一对多，分步查询)
- 分页查询

#### Springboot + zookeeper
- 2n+1台server，只要有n+1台就可以使用，若server少于一半集群整体失效

#### Zookeeper实现分布式锁
- 分布式锁是控制分布式系统之间同步访问共享资源的一种方式。如果不同的系统或是同一个系统的不同主机之间共享了一个或一组资源，
  那么访问这些资源的时候，往往需要通过一些互斥手段来防止彼此之间的干扰，以保证一致性，在这种情况下，就需要使用分布式锁了。

**排他锁**

- 排他锁的核心是如何保证当前有且仅有一个事务获得锁，并且锁被释放后，所有正在等待获取锁的事务都能够被通知到。
- 在通常的 Java 开发编程中，有两种常见的方式可以用来定义锁，分别是 synchronized 机制和JDK5提供的 ReentrantLock。然而，在 ZooKeeper 中，没有类似于这样的 API 可以直接使用，而是通过 ZooKeeper 上的数据节点来表示一个锁，例如 /exclusive_ lock/lock 节点就可以被定义为一个锁。
- 在需要获取排他锁时，所有的客户端都会试图通过调用 create() 接口， 在
  /exclusive_ lock 节点下创建临时子节点 /exclusive_ lock/lock。ZooKeeper 会保证在所有的客户端中，最终只有一个客户端能够创建成功，那么就可以认为该客户端获取了锁。同时，所有没有获取到锁的客户端就需要到 /exclusive_ lock 节点上注册一个子节点变更的 Watcher 监听，以便实时监听到 lock 节点的变更情况。
- /exclusive_ lock/lock 是一个临时节点，因此在以下两种情况下，都有可能释放锁。
  - 当前获取锁的客户端机器发生宕机，那么 ZooKeeper 上的这个临时节点就会被移除。
  - 正常执行完业务逻辑后，客户端就会主动将自己创建的临时节点删除。
- 无论在什么情况下移除了 lock 节点，ZooKeeper 都会通知所有在 /exclusive_ lock 节点上注册了子节点变更 Watcher 监听的客户端。这些客户端在接收到通知后，再次重新发起分布式锁获取。

**共享锁**

- 共享锁和排他锁最根本的区别在于，加上排他锁后，数据对象只对一个事务可见，而加上共享锁后，数据对所有事务都可见。
- 和排他锁-样，同样是通过 ZooKeeper 上的数据节点来表示一个锁，是一个类似于
  /shared_ lock/[Hostname]-请求类型-序号 的临时顺序节点，例如 /shared_ lock/
  192.168.0.1-R-0000000001，那么，这个节点就代表了一个共享锁
- 根据共享锁的定义，不同的事务都可以同时对同一个数据对象进行读取操作，而更新操作必须在当前没有任何事务进行读写操作的情况下进行。基于这个原则，我们来看看如何通过 ZooKeeper 的节点来确定分布式读写顺序，大致可以分为如下4个步骤。
  - 创建完节点后，获取 /shared_lock 节点下的所有子节点，并对该节点注册子节点变更的 Watcher 监听。
  - 确定自己的节点序号在所有子节点中的顺序。
  - 对于读请求：如果没有比自己序号小的子节点，或是所有比自己序号小的子节点都是读请求，那么表明自己已经成功获取到了共享锁，同时开始执行读取逻辑。如果比自己序号小的子节点中有写请求，那么就需要进入等待。对于写请求：如果自己不是序号最小的子节点，那么就需要进入等待。
  - 接收到 Watcher 通知后，重复步骤1。

**羊群效应**

-  一个特定的znode 改变的时候ZooKeeper 触发了所有watches 的事件。
-  举个例子，如果有1000个客户端watch 一个znode的exists调用，当这个节点被创建的时候，将会有1000个通知被发送。这种由于一个被watch的znode变化，导致大量的通知需要被发送，将会导致在这个通知期间的其他操作提交的延迟。因此，只要可能，我们都强烈建议不要这么使用watch。仅仅有很少的客户端同时去watch一个znode比较好，理想的情况是只有1个。


#### Zookeeper实现分布式队列

- 分布式队列，简单地讲分为两大类，一种是常规的先入先出队列，另一种则是要等到队列元素集聚之后才统一安排执行的Barrier模型。

**FIFO**

- 使用 ZooKeeper 实现 FIFO 队列，和共享锁的实现非常类似。FIFO 队列就类似于一个全写的共享锁模型，大体的设计思路其实非常简单：所有客户端都会到 /queue_fifo 这个节点下面创建一个临时顺序节点，例如 /queue_fifo/192.168.0.1-0000000001。
- 创建完节点之后，根据如下4个步骤来确定执行顺序。
  - 通过调用 getChildren() 接口来获取 /queue_fifo 节点下的所有子节点，即获取队列中所有的元素。
  - 确定自己的节点序号在所有子节点中的顺序。
  - 如果自己不是序号最小的子节点，那么就需要进入等待，同时向比自己序号小的最后一个节点注册 Watcher 监听。
  - 接收到 Watcher 通知后，重复步骤1。

**Barrier**

- Barrier 原意是指障碍物、屏障，而在分布式系统中，特指系统之间的一个协调条件，规定了一个队列的元素必须都集聚后才能统一进行安排，否则一直等待。这往往出现在那些大规模分布式并行计算的应用场景上：最终的合并计算需要基于很多并行计算的子结果来进行。这些队列其实是在 FIFO 队列的基础上进行了增强
- 大致的设计思想如下：开始时，/queue_barrier 节点是一个已经存在的默认节点，并且将其节点的数据内容赋值为一个数字n来代表 Barrier 值，例如n=10表示只有当 /queue_barrier 节点下的子节点个数达到10后，才会打开 Barrier。之后，所有的客户端都会到 /queue_barrier 节点下创建一个临时节点，例如 /queue_barrier/l92.168.0.1。

#### RabbitMQ微服务外卖

#### Starry-Douban

- 仿豆瓣电影搜索引擎

- 前端: Vue + Bootstrap
- 后端: Spring Boot + ElasticSearch + Jsoup

<img src="./images/image-20210515143659573.png" alt="image-20210515143659573" style="zoom:50%;" />
