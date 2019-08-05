package com.daily.taskcenter.config;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.lang.Nullable;
import org.springframework.scheduling.config.Task;

import java.util.concurrent.ScheduledFuture;

/**
 * @author z
 * @date 2019/7/25 11:47
 **/
@Data
public class DyTaskInfo {
    private final Task task;
    @Nullable
    volatile ScheduledFuture<?> future;

    public DyTaskInfo(Task task) {
        this.task = task;
    }
}
