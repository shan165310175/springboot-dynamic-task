package com.daily.taskcenter.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 在run方法上添加该注解，启动后自动将信息注入到数据库中
 *
 * @author z
 * @date 2019/7/25 17:19
 **/
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RegisterTask {

    /**
     * 是否启用定时任务信息注入到数据中
     *
     * @return
     */
    boolean enable() default true;

    /**
     * 每次重启，覆盖掉数据库中的TASK记录
     */
    boolean override() default false;

    /**
     * 任务名称
     *
     * @return
     */
    String name();

    /**
     * 备注
     *
     * @return
     */
    String remark() default "";

    /**
     * cron表达式
     *
     * @return
     */
    String cron();

    /**
     * 状态: 0 启用 1 禁用
     *
     * @return
     */
    int status();

}
