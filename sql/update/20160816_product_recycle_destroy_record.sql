/*==============================================================*/
/* Table: product_recycle_destroy_record                        */
/*==============================================================*/
create table product_recycle_destroy_record
(
   id                   bigint not null auto_increment comment 'id',
   product_name         varchar(200) not null comment '产品名称',
   product_code         varchar(50) not null comment '产品条码',
   batch                varchar(50) not null comment '批次',
   number               varchar(50) not null comment '数量',
   problem_describe     varchar(100) not null comment '问题描述',
   process_time         datetime not null comment '处理时间',
   process_mode         varchar(1) not null comment '处理方式（1：退货、2：销毁）',
   record_id            varchar(50) comment '处理记录id（可关联记录详情，比如销毁图片）',
   handle_name          varchar(100) not null comment '处理企业名称',
   recieve_name         varchar(100) comment '接收企业名（只有退货时有）',
   operation_user       varchar(20) not null comment '操作者',
   RECORD_INSERT_TIME   TIMESTAMP not null default CURRENT_TIMESTAMP comment '数据插入时间',
   primary key (id)
);

alter table product_recycle_destroy_record comment '产品回收/销毁记录表';