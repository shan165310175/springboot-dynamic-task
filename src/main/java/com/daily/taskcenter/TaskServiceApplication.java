package com.daily.taskcenter;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author z
 * @date 2019/7/25 11:04
 **/
@SpringBootApplication
@MapperScan(basePackages = "com.daily.**.mapper")
public class TaskServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(TaskServiceApplication.class, args);
    }
}
