-- 用户表
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
    planet_code   varchar(512)                           null comment '星球编号',
    tags          varchar(1024)                          null comment '标签 json 列表'
)
    comment '用户';

-- 队伍表
create table team
(
    id           bigint auto_increment comment 'id' primary key,
    name   varchar(256)                   not null comment '队伍名称',
    description varchar(1024)                      null comment '描述',
    max_num    int      default 1                 not null comment '最大人数',
    expire_time    datetime  null comment '过期时间',·
    user_id            bigint comment '用户id',
    status    int      default 0                 not null comment '0 - 公开，1 - 私有，2 - 加密',
    password varchar(512)                       null comment '密码',
    create_time   datetime default CURRENT_TIMESTAMP null comment '创建时间',
    update_time   datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP,
    is_delete     tinyint  default 0                 not null comment '是否删除'
)
    comment '队伍';

-- 用户队伍关系表
create table user_team
(
    id           bigint auto_increment comment 'id'
        primary key,
    user_id            bigint comment '用户id',
    team_id            bigint comment '队伍id',
    join_time datetime  null comment '加入时间',
    create_time   datetime default CURRENT_TIMESTAMP null comment '创建时间',
    update_time   datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP,
    is_delete     tinyint  default 0                 not null comment '是否删除'
)
    comment '用户队伍关系';