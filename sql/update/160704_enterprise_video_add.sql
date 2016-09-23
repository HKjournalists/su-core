/*==============================================================*/
/* DBMS name:      MySQL 5.0                                    */
/* Created on:     2016/7/4 ����һ 18:08:21                        */
/*==============================================================*/


/*==============================================================*/
/* Table: enterprise_video                                      */
/*==============================================================*/
create table enterprise_video
(
   id                   varchar(50) not null comment 'id',
   org_id               varchar(50) not null comment '��ҵid',
   video_url            text not null comment '��Ƶ��ַ',
   video_type           varchar(2) not null comment '��Ƶ���1��ʵʱ��Ƶ��2���ļ���Ƶ��',
   video_desc           varchar(200) comment '��Ƶ�������',
   sort                 int not null comment '����',
   is_show              varchar(1) not null comment '�Ƿ�չʾ��0����չʾ��1��չʾ��',
   create_user          varchar(20) comment '������',
   create_time          datetime comment '����ʱ��',
   last_modify_user     varchar(20) comment '��������',
   last_modify_time     datetime comment '������ʱ��',
   primary key (id)
);

alter table enterprise_video comment '��ҵ��Ƶ��Ϣ��';

