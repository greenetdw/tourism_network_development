# 旅游网开发
### 项目描述：

- 系统分为三大模块：日志收集模块、数据分析模块、数据展示模块

- 日志收集模块：主要由 sdk + nginx + flume 组成。 通过 js/java 将 PC 端和程序后台的数据发

送到 nginx 服务器，然后 flume 监控 nginx 日志存储到 HDFS 中。

- 数据分析模块：主要包括用户基本信息分析、浏览器信息分析、 地域信息分析、用户浏览深度分

析、订单信息分析、事件分析等， 首先解析 HDFS 中的日志数据并存储到 HBase 分布式数据库中，
然后通过 MapReduce/Hive 解析 HBase 中的数据并存储到 MySQL 中。 同时使用 Oozie 来管理多个
MR 或 Hive 任务的调度，使多个任务有条不紊进行。 并且为了提高系统的运行效率，适当对系统
进行调优(主要是 reducer 调优和服务器调优)。

- 数据展示模块： 用 Spring + Hibernate + SpringMVC 实现了系统搭建， 并且把 MySQL 数据库中

获取的数据(为了提高查询速度，增加了缓存机制)转换成 json 格式，并结合 HighChart + jQuery

- ajax 对各个模块数据进行图标展示。

### 技术环境：

- [ ] Hadoop、 HDFS、 Hive、 HBase、 ZooKeeper、 Flume、 MR、 Oozie

- [ ] Spring、 Hibernate、 SpringMVC、 Maven
- [ ] jQuery、 HighChart、 Ajax