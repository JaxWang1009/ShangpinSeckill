package com.ecommerce.seckill.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * 线程池配置
 * 
 * @author ecommerce-team
 * @since 1.0.0
 */
@Configuration
public class ThreadPoolConfig {
    
    @Value("${seckill.thread-pool.core-size:10}")
    private Integer coreSize;
    
    @Value("${seckill.thread-pool.max-size:50}")
    private Integer maxSize;
    
    @Value("${seckill.thread-pool.queue-capacity:100}")
    private Integer queueCapacity;
    
    @Value("${seckill.thread-pool.keep-alive-seconds:60}")
    private Integer keepAliveSeconds;
    
    @Bean("seckillThreadPool")
    public ThreadPoolExecutor seckillThreadPool() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(coreSize);
        executor.setMaxPoolSize(maxSize);
        executor.setQueueCapacity(queueCapacity);
        executor.setKeepAliveSeconds(keepAliveSeconds);
        executor.setThreadNamePrefix("seckill-");
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        return executor.getThreadPoolExecutor();
    }
} 