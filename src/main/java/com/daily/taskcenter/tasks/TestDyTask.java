package com.daily.taskcenter.tasks;

import com.daily.taskcenter.anno.RegisterTask;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * @author z
 * @date 2019/7/25 12:38
 **/
@Component
@Slf4j
public class TestDyTask implements DyTask {

    /**
     * 启动后自动将信息注入到数据库
     */
    @RegisterTask(enable = true, override = false, name = "动态任务1 - 测试", remark = "这是备注", cron = "0 */5 * * * ?", status = 0)
    @Override
    public void run() {
        log.info("TestDyTask 测试任务。。。" + LocalDateTime.now());
    }
}
