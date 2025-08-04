package com.ecommerce.admin;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 后台管理服务启动类
 * 
 * @author ecommerce-team
 * @since 1.0.0
 */
@SpringBootApplication
@MapperScan("com.ecommerce.admin.dao")
public class AdminServiceApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(AdminServiceApplication.class, args);
    }
} 