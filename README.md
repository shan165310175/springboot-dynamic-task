### 动态定时任务模块

文章链接：https://blog.csdn.net/shan165310175/article/details/98478970

数据库表
```
create table dynamic_task_config
(
    id          int auto_increment
        primary key,
    name        varchar(255)  null comment '名称',
    remark      varchar(255)  null comment '描述',
    bean_name   varchar(255)  not null comment 'bean名称',
    cron        varchar(56)   not null,
    status      int default 0 null comment '0 ok 1 禁用',
    create_time datetime      null,
    update_time datetime      null,
    constraint dynamic_task_config_beanName_uindex
        unique (bean_name)
);
```