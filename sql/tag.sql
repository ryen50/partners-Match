-- auto-generated definition
create table tag
(
    id          bigint auto_increment comment '标签id'
        primary key,
    tag_name    varchar(256)                        not null comment '标签名称',
    user_id     bigint                              not null comment '上传用户id',
    is_Parent   tinyint   default 1                 not null comment '是否为父标签,1为是，0为不是',
    parent_id   bigint                              null comment '父标签id',
    create_time timestamp default CURRENT_TIMESTAMP null comment '创建时间',
    update_time timestamp default CURRENT_TIMESTAMP null comment '更新时间',
    isDelete    tinyint   default 0                 not null comment '逻辑删除，1为删除，0为未删除',
    constraint tag_tag_name_uindex
        unique (tag_name) comment '根据标签创建的索引',
    constraint tag_user_id_uindex
        unique (user_id) comment '根据用户id索引'
)
    comment '标签表';

