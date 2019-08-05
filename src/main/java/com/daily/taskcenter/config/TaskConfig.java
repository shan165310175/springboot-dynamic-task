package com.daily.taskcenter.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

/**
 * @author z
 * @date 2019/7/25 11:03
 **/
@Configuration
@EnableAsync
@EnableScheduling
@Slf4j
public class TaskConfig {

    @Bean
    @Primary
    public ThreadPoolTaskExecutor threadPoolTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(30);
        executor.setMaxPoolSize(50);
        executor.setAllowCoreThreadTimeOut(true);
        executor.setKeepAliveSeconds(600);
        executor.setThreadNamePrefix("async-pool-");
        executor.initialize();
        return executor;
    }

    @Bean("taskExecutor")
    public ThreadPoolTaskScheduler threadPoolTaskScheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setThreadNamePrefix("sync-pool-");
        scheduler.setPoolSize(30);
        scheduler.initialize();
        return scheduler;
    }

}
