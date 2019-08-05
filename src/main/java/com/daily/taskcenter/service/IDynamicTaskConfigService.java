package com.daily.taskcenter.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.daily.taskcenter.entity.DynamicTaskConfig;

import java.util.List;

/**
 * @author z
 * @date 2019/7/25 14:20
 **/
public interface IDynamicTaskConfigService extends IService<DynamicTaskConfig> {

    List<DynamicTaskConfig> findAllByStatus(int status);

    DynamicTaskConfig findByBeanName(String beanName);

    DynamicTaskConfig addTask(DynamicTaskConfig resource);

    void updateTask(DynamicTaskConfig resource);

    DynamicTaskConfig removeTask(Long taskId);
}
