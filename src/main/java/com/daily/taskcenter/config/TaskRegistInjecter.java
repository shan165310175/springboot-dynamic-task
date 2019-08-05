package com.daily.taskcenter.config;

import com.daily.taskcenter.entity.DynamicTaskConfig;
import com.daily.taskcenter.anno.RegisterTask;
import com.daily.taskcenter.service.IDynamicTaskConfigService;
import com.daily.taskcenter.tasks.DyTask;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Method;
import java.util.Date;

/**
 * @author z
 * @date 2019/7/25 17:17
 **/
@Configuration
@Slf4j
public class TaskRegistInjecter implements BeanPostProcessor {

    @Autowired
    private IDynamicTaskConfigService dynamicTaskConfigService;

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        RegisterTask anno = this.getAnnotation(bean);
        if (anno == null) {
            return bean;
        }

        if (!anno.enable()) {
            return bean;
        }

        DynamicTaskConfig config = dynamicTaskConfigService.findByBeanName(beanName);
        if (config != null) {
            // 不覆盖
            if (!anno.override()) {
                return bean;
            }

            config.setStatus(anno.status());
            config.setBeanName(beanName);
            config.setCron(anno.cron());
            config.setName(anno.name());
            config.setRemark(anno.remark());
            config.setUpdateTime(new Date());

            dynamicTaskConfigService.updateById(config);
            log.info("更新定时任务配置: {},{},{}", beanName, anno.name(), anno.cron());
            return bean;
        }

        // 新增加
        config = new DynamicTaskConfig();
        config.setStatus(anno.status());
        config.setBeanName(beanName);
        config.setCron(anno.cron());
        config.setName(anno.name());
        config.setRemark(anno.remark());
        config.setCreateTime(new Date());
        config.setUpdateTime(new Date());

        dynamicTaskConfigService.save(config);

        log.info("新增定时任务配置: {},{},{}", beanName, anno.name(), anno.cron());

        return bean;
    }

    RegisterTask getAnnotation(Object bean) {
        if (!( bean instanceof DyTask )) {
            return null;
        }
        try {
            Method method = bean.getClass().getMethod("run");
            return method.getAnnotation(RegisterTask.class);
        }
        catch (NoSuchMethodException e) {
            return null;
        }
    }

}
