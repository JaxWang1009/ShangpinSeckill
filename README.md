# 电商秒杀系统

基于Spring Boot的极简但完整的电商秒杀系统，具有高并发、防超卖、数据一致性等核心特性。

## 技术栈

- Java 8 / Spring Boot 2.7.18 / MyBatis 3.5.13
- MySQL 8.0.33 / Redis 6.2.13 / Elasticsearch 7.17.14 / RocketMQ 4.9.7
- JWT / Spring Security / RBAC权限控制

## 项目结构

```
ecommerce-system/
├── admin-service/          # 后台管理模块
├── seckill-service/        # 秒杀核心模块
├── common-core/           # 公共组件模块
└── README.md
```

## 快速启动

### 1. 环境准备

使用Docker Compose启动依赖服务：

```yaml
version: '3.8'
services:
  mysql:
    image: mysql:8.0
    environment:
      MYSQL_ROOT_PASSWORD: 123456
      MYSQL_DATABASE: ecommerce_admin
    ports:
      - "3306:3306"

  redis:
    image: redis:6.2
    ports:
      - "6379:6379"

  elasticsearch:
    image: elasticsearch:7.17.14
    environment:
      - discovery.type=single-node
    ports:
      - "9200:9200"

  rocketmq:
    image: apache/rocketmq:4.9.7
    ports:
      - "9876:9876"
```

### 2. 启动应用

```bash
# 编译项目
mvn clean compile

# 启动后台管理服务
cd admin-service && mvn spring-boot:run

# 启动秒杀服务
cd seckill-service && mvn spring-boot:run
```

## 核心接口

### 后台管理接口

- `GET /categories` - 分页查询分类
- `POST /categories` - 创建分类
- `PUT /products/{id}/stock` - 调整库存
- `GET /products?categoryId={}` - 分类商品查询

### 秒杀接口

- `GET /seckill/items?keyword={}` - 搜索秒杀商品
- `POST /seckill/{itemId}` - 执行秒杀
- `DELETE /seckill/orders/{orderNo}` - 取消订单

## 关键技术点

### 1. 防超卖机制
- Redis Lua脚本原子性库存操作
- 分布式锁防止重复下单
- 库存回滚补偿机制

### 2. 数据一致性
- @Transactional注解管理MySQL事务
- MQ消息表保证本地事务与消息发送一致性
- ES异步更新解耦

### 3. 流量控制
- RocketMQ削峰（线程池最大并发=200）
- 延迟消息处理订单超时
- 顺序消费保证状态机有序性

## 架构特点

- **极简设计**：省略非核心功能，专注秒杀核心
- **高扩展性**：模块化分层，支持独立部署
- **安全防护**：RBAC权限控制，操作留痕
- **性能优化**：Redis预压缩，多级缓存策略 