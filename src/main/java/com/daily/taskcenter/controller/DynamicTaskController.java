package com.daily.taskcenter.controller;

import com.daily.taskcenter.entity.DynamicTaskConfig;
import com.daily.taskcenter.exception.ServiceException;
import com.daily.taskcenter.service.DyTaskManager;
import com.daily.taskcenter.service.IDynamicTaskConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * @author z
 * @date 2019/7/25 11:24
 **/
@Api(tags = "动态定时任务")
@RestController
@RequestMapping("/v1/api/dyTasks")
@Slf4j
@CrossOrigin
public class DynamicTaskController {

    @Autowired
    private DyTaskManager dyTaskManager;

    @Autowired
    private IDynamicTaskConfigService dynamicTaskConfigService;

    @ApiOperation("任务配置列表")
    @GetMapping("/list")
    public List<DynamicTaskConfig> listTasks() {
        List<DynamicTaskConfig> list = dynamicTaskConfigService.list();
        return list;
    }

    @ApiOperation("新增任务配置(status=0 将会启动任务)")
    @PostMapping("/add")
    public DynamicTaskConfig addTask(@RequestBody @Validated DynamicTaskConfig resource) {
        resource.setId(null);
        Date date = new Date();
        resource.setCreateTime(date);
        resource.setUpdateTime(date);
        DynamicTaskConfig config = dynamicTaskConfigService.addTask(resource);
        if (config != null && DynamicTaskConfig.Status.ENABLED == config.getStatus()) {
            dyTaskManager.scheduleTask(config.getBeanName(), config.getCron());
        }
        return config;
    }

    @ApiOperation("更新任务配置（将会重新运行任务)")
    @PutMapping("/update")
    public void update(@RequestBody @Validated DynamicTaskConfig resource) {
        if (resource.getId() == null) {
            throw new ServiceException("id无效");
        }

        DynamicTaskConfig config = dynamicTaskConfigService.getById(resource.getId());
        if (config == null) {
            throw new ServiceException("没有找到定时任务记录");
        }

        resource.setCreateTime(config.getCreateTime());
        resource.setUpdateTime(new Date());
        dynamicTaskConfigService.updateTask(resource);

        if (DynamicTaskConfig.Status.ENABLED == resource.getStatus()) {
            try {
                dyTaskManager.cancelTask(config.getBeanName());
                dyTaskManager.scheduleTask(resource.getBeanName(), resource.getCron());
            }
            catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }

    @ApiOperation("删除任务（将会停止正在运行任务)")
    @DeleteMapping("/remove")
    public void removeTask(@RequestParam Long taskId) {
        DynamicTaskConfig config = dynamicTaskConfigService.removeTask(taskId);
        if (config != null) {
            dyTaskManager.cancelTask(config.getBeanName());
        }
    }

    @ApiOperation("开始任务(通过配置taskId)")
    @GetMapping("/scheduleTask")
    public String scheduleTask(@RequestParam String taskName) {
        log.info("请求开始任务:{}", taskName);
        DynamicTaskConfig config = dynamicTaskConfigService.findByBeanName(taskName);
        if (config == null) {
            throw new ServiceException("没有找到配置记录:" + taskName);
        }
        dyTaskManager.scheduleTask(config.getBeanName(), config.getCron());
        return "ok";
    }

    @ApiOperation("取消任务")
    @GetMapping("cancelTask")
    public String cancelTask(@RequestParam String taskName) {
        log.info("请求取消任务:" + taskName);
        dyTaskManager.cancelTask(taskName);
        return "ok";
    }

    @ApiOperation("直接执行任务(异步)")
    @GetMapping("/runTask")
    public String runTask(@RequestParam String taskName) {
        log.info("请求执行任务:{}", taskName);
        dyTaskManager.runTask(taskName);
        return "ok";
    }

}
