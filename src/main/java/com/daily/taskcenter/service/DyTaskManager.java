package com.daily.taskcenter.service;

/**
 * @author z
 * @date 2019/7/25 11:48
 **/
public interface DyTaskManager {

    /**
     * 直接执行定时任务
     */
    boolean runTask(String beanName);

    /**
     * 开始定时任务
     *
     * @param beanName
     * @param cron
     *
     * @return
     */
    boolean scheduleTask(String beanName, String cron);

    /**
     * 取消定时任务
     *
     * @param beanName
     *
     * @return
     */
    boolean cancelTask(String beanName);

}
