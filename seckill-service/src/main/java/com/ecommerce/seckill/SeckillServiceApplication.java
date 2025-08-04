package com.ecommerce.seckill;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 秒杀服务启动类
 * 
 * @author ecommerce-team
 * @since 1.0.0
 */
@SpringBootApplication
@MapperScan("com.ecommerce.seckill.dao")
public class SeckillServiceApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(SeckillServiceApplication.class, args);
    }
} 