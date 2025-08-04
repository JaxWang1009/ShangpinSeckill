-- 电商秒杀系统数据库初始化脚本

-- 创建数据库
CREATE DATABASE IF NOT EXISTS ecommerce_admin DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS ecommerce_seckill DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 使用后台管理数据库
USE ecommerce_admin;

-- 商品分类表
CREATE TABLE IF NOT EXISTS category (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '分类ID',
    name VARCHAR(100) NOT NULL COMMENT '分类名称',
    parent_id BIGINT DEFAULT 0 COMMENT '父分类ID',
    level INT DEFAULT 1 COMMENT '分类层级',
    sort INT DEFAULT 0 COMMENT '排序',
    status TINYINT DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    create_by VARCHAR(50) COMMENT '创建人',
    update_by VARCHAR(50) COMMENT '更新人',
    INDEX idx_parent_id (parent_id),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品分类表';

-- 商品表
CREATE TABLE IF NOT EXISTS product (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '商品ID',
    name VARCHAR(200) NOT NULL COMMENT '商品名称',
    description TEXT COMMENT '商品描述',
    price DECIMAL(10,2) NOT NULL COMMENT '商品价格',
    stock INT DEFAULT 0 COMMENT '商品库存',
    category_id BIGINT COMMENT '分类ID',
    image VARCHAR(500) COMMENT '商品图片',
    status TINYINT DEFAULT 1 COMMENT '状态：0-下架，1-上架',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    create_by VARCHAR(50) COMMENT '创建人',
    update_by VARCHAR(50) COMMENT '更新人',
    INDEX idx_category_id (category_id),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品表';

-- 订单表
CREATE TABLE IF NOT EXISTS `order` (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '订单ID',
    order_no VARCHAR(50) UNIQUE NOT NULL COMMENT '订单号',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    product_id BIGINT NOT NULL COMMENT '商品ID',
    quantity INT DEFAULT 1 COMMENT '商品数量',
    amount DECIMAL(10,2) NOT NULL COMMENT '订单金额',
    status TINYINT DEFAULT 0 COMMENT '订单状态：0-未支付，1-已支付，-1-已取消',
    pay_time DATETIME COMMENT '支付时间',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    create_by VARCHAR(50) COMMENT '创建人',
    update_by VARCHAR(50) COMMENT '更新人',
    INDEX idx_user_id (user_id),
    INDEX idx_product_id (product_id),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单表';

-- 使用秒杀数据库
USE ecommerce_seckill;

-- 秒杀商品表
CREATE TABLE IF NOT EXISTS seckill_item (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '秒杀商品ID',
    title VARCHAR(200) NOT NULL COMMENT '商品标题',
    description TEXT COMMENT '商品描述',
    price DECIMAL(10,2) NOT NULL COMMENT '商品原价',
    seckill_price DECIMAL(10,2) NOT NULL COMMENT '秒杀价格',
    stock INT DEFAULT 0 COMMENT '商品库存',
    is_active TINYINT DEFAULT 0 COMMENT '是否激活：0-未激活，1-已激活',
    start_time DATETIME COMMENT '秒杀开始时间',
    end_time DATETIME COMMENT '秒杀结束时间',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_is_active (is_active),
    INDEX idx_start_time (start_time),
    INDEX idx_end_time (end_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='秒杀商品表';

-- 秒杀订单表
CREATE TABLE IF NOT EXISTS seckill_order (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '订单ID',
    order_no VARCHAR(50) UNIQUE NOT NULL COMMENT '订单号',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    item_id BIGINT NOT NULL COMMENT '秒杀商品ID',
    quantity INT DEFAULT 1 COMMENT '商品数量',
    amount DECIMAL(10,2) NOT NULL COMMENT '订单金额',
    status TINYINT DEFAULT 0 COMMENT '订单状态：0-未支付，1-已支付，-1-已取消',
    pay_time DATETIME COMMENT '支付时间',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_user_id (user_id),
    INDEX idx_item_id (item_id),
    INDEX idx_status (status),
    INDEX idx_order_no (order_no)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='秒杀订单表';

-- 插入测试数据
USE ecommerce_admin;

-- 插入分类数据
INSERT INTO category (name, parent_id, level, sort, status, create_by) VALUES 
('电子产品', 0, 1, 1, 1, 'admin'),
('服装鞋帽', 0, 1, 2, 1, 'admin'),
('手机数码', 1, 2, 1, 1, 'admin'),
('电脑办公', 1, 2, 2, 1, 'admin');

-- 插入商品数据
INSERT INTO product (name, description, price, stock, category_id, status, create_by) VALUES 
('iPhone 15', '最新款iPhone手机', 5999.00, 100, 3, 1, 'admin'),
('MacBook Pro', '专业级笔记本电脑', 12999.00, 50, 4, 1, 'admin'),
('Nike运动鞋', '舒适透气运动鞋', 299.00, 200, 2, 1, 'admin');

USE ecommerce_seckill;

-- 插入秒杀商品数据
INSERT INTO seckill_item (title, description, price, seckill_price, stock, is_active, start_time, end_time) VALUES 
('iPhone 15 秒杀', 'iPhone 15 限时秒杀', 5999.00, 4999.00, 10, 1, '2024-01-01 10:00:00', '2024-12-31 23:59:59'),
('MacBook Pro 秒杀', 'MacBook Pro 限时秒杀', 12999.00, 9999.00, 5, 1, '2024-01-01 10:00:00', '2024-12-31 23:59:59'),
('Nike运动鞋 秒杀', 'Nike运动鞋 限时秒杀', 299.00, 199.00, 20, 1, '2024-01-01 10:00:00', '2024-12-31 23:59:59'); 