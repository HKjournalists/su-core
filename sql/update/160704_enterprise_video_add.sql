/*==============================================================*/
/* DBMS name:      MySQL 5.0                                    */
/* Created on:     2016/7/4 星期一 18:08:21                        */
/*==============================================================*/


/*==============================================================*/
/* Table: enterprise_video                                      */
/*==============================================================*/
create table enterprise_video
(
   id                   varchar(50) not null comment 'id',
   org_id               varchar(50) not null comment '企业id',
   video_url            text not null comment '视频地址',
   video_type           varchar(2) not null comment '视频类别（1：实时视频、2：文件视频）',
   video_desc           varchar(200) comment '视频简短描述',
   sort                 int not null comment '排序',
   is_show              varchar(1) not null comment '是否展示（0：不展示、1：展示）',
   create_user          varchar(20) comment '创建者',
   create_time          datetime comment '创建时间',
   last_modify_user     varchar(20) comment '最后更新者',
   last_modify_time     datetime comment '最后更新时间',
   primary key (id)
);

alter table enterprise_video comment '企业视频信息表';

