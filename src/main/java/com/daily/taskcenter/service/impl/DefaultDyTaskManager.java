package com.daily.taskcenter.service.impl;

import com.daily.taskcenter.config.DyTaskInfo;
import com.daily.taskcenter.entity.DynamicTaskConfig;
import com.daily.taskcenter.exception.ServiceException;
import com.daily.taskcenter.service.DyTaskManager;
import com.daily.taskcenter.service.IDynamicTaskConfigService;
import com.daily.taskcenter.tasks.DyTask;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.CronTask;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.scheduling.support.ScheduledMethodRunnable;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

/**
 * @author z
 * @date 2019/7/25 11:53
 **/
@Service
@Slf4j
public class DefaultDyTaskManager implements DyTaskManager, CommandLineRunner, ApplicationContextAware {

    private ApplicationContext context;

    Map<String, DyTaskInfo> taskMap = new ConcurrentHashMap<>();

    @Autowired
    private ThreadPoolTaskScheduler taskScheduler;

    @Autowired
    private IDynamicTaskConfigService dynamicTaskConfigService;

    @Override
    public boolean cancelTask(String beanName) {
        if (StringUtils.isBlank(beanName)) {
            return false;
        }
        DyTaskInfo task = taskMap.remove(beanName);
        if (task != null && task.getFuture() != null) {
            task.getFuture().cancel(true);
            return true;
        }
        return false;
    }

    @Override
    public boolean runTask(String beanName) {
        DyTask target = null;
        try {
            target = (DyTask) context.getBean(beanName);
        }
        catch (Throwable e) {
            throw new ServiceException("无法获取bean信息, 无法执行任务:" + e.getMessage());
        }

        // 执行
        target.run();

        return true;
    }

    @Override
    public boolean scheduleTask(String beanName, String cron) {
        DyTask target = null;
        try {
            target = (DyTask) context.getBean(beanName);
        }
        catch (Throwable e) {
            throw new ServiceException("无法获取bean信息, 无法执行任务:" + e.getMessage());
        }

        Method method = null;
        try {
            method = target.getClass().getMethod("run");
        }
        catch (NoSuchMethodException e) {
            throw new ServiceException("无法获取run方法, 无法执行任务:" + e.getMessage());
        }

        try {
            CronTask cronTask = new CronTask(createRunnable(target, method),
                    new CronTrigger(cron, TimeZone.getTimeZone(ZoneId.systemDefault())));
            DyTaskInfo taskInfo = new DyTaskInfo(cronTask);
            ScheduledFuture<?> future = taskScheduler.schedule(cronTask.getRunnable(), cronTask.getTrigger());
            taskInfo.setFuture(future);

            cancelTask(beanName);

            taskMap.put(beanName, taskInfo);

            return true;
        }
        catch (Throwable e) {
            log.info("配置异常:{}", e.getMessage());
        }

        return true;
    }

    private Runnable createRunnable(Object target, Method method) {
        Method invocableMethod = AopUtils.selectInvocableMethod(method, target.getClass());
        return new ScheduledMethodRunnable(target, invocableMethod);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }

    @Override
    public void run(String... args) throws Exception {
        List<DynamicTaskConfig> taskConfigs = dynamicTaskConfigService.findAllByStatus(0);
        taskConfigs.forEach(config -> {
            if (StringUtils.isNotBlank(config.getBeanName()) && StringUtils.isNotBlank(config.getCron())) {
                log.info(">>> 初始化定时任务: {},{},{}", config.getName(), config.getBeanName(), config.getCron());
                try {
                    scheduleTask(config.getBeanName(), config.getCron());
                }
                catch (Throwable e) {
                    log.error("初始化失败: {}", e.getMessage());
                }
            }
        });
    }
}
