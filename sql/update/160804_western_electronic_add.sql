drop table if exists western_electronic;

/*==============================================================*/
/* Table: western_electronic                                    */
/*==============================================================*/
create table western_electronic
(
   id                   smallint not null auto_increment comment 'id',
   status               varchar(1) not null comment '状态（0：未接入、1：接入完成、2：接入中）',
   percent              varchar(10) comment '接入进度',
   complete_num         int comment '当前接入企业数',
   company_num          int comment '总企业数',
   product_num          int comment '接入产品数',
   report_num           int comment '接入检测报告数',
   trace_num            int comment '接入产品追溯信息数',
   success_num          int comment '接入成功企业数',
   fail_num             int comment '接入失败企业数',
   operation_user       varchar(20) comment '操作者',
   operation_time       datetime comment '操作时间',
   complete_time        datetime comment '完成时间',
   RECORD_INSERT_TIME   TIMESTAMP not null default CURRENT_TIMESTAMP comment '数据插入时间',
   primary key (id)
);

alter table western_electronic comment '西部电子商务数据接入情况表';