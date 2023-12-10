create database user_center;
-- auto-generated definition
create table user
(
    id            bigint auto_increment
        primary key,
    username      varchar(256)                           null comment '用户名',
    user_account  varchar(256)                           null comment '账号',
    avatar_url    varchar(1024)                          null comment '用户头像',
    gender        tinyint                                null comment '性别',
    user_password varchar(512) default '12345678'        not null comment '密码',
    phone         varchar(128)                           null comment '电话',
    email         varchar(512)                           null comment '邮箱',
    user_status   int          default 0                 not null comment '用户状态 0: 正常',
    create_time   timestamp    default CURRENT_TIMESTAMP null comment '创建时间',
    update_time   datetime                               null comment '更新时间',
    is_delete     tinyint      default 0                 null comment '逻辑删除',
    is_valid      tinyint      default 0                 null,
    user_role     int          default 0                 not null comment '角色',
    planet_code   varchar(512)                           null comment '星球编号'
)
    comment '用户';

