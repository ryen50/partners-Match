-- auto-generated definition
create table user
(
    id            bigint auto_increment comment 'id'
        primary key,
    user_name     varchar(256)                       null comment '用户昵称',
    user_account  varchar(256)                       null comment '登录账号',
    avatar_url    varchar(1024)                      null comment '头像',
    gender        tinyint                            null comment '性别',
    user_password varchar(256)                       not null comment '密码',
    phone         varchar(256)                       null comment '电话',
    email         varchar(512)                       null comment '邮箱',
    user_status   int      default 0                 null comment '用户状态 0为正常，1为异常',
    create_time   datetime default CURRENT_TIMESTAMP null comment '创建时间',
    update_time   datetime default CURRENT_TIMESTAMP not null comment '更新时间',
    is_delete     tinyint  default 0                 null comment '是否删除',
    role          tinyint  default 0                 null comment '用户权限',
    planet_code   varchar(512)                       null comment '星球编号'
) ;

