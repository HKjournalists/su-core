ALTER TABLE `test_result`
ADD COLUMN `mk_publish_flag`  bit(1) NOT NULL DEFAULT b'0' COMMENT 'easy报告发布标志' AFTER `receiveDate`,
ADD COLUMN `back_flag`  bit(1) NOT NULL DEFAULT b'0' COMMENT '退回标志' AFTER `mk_publish_flag`,
ADD COLUMN `back_result`  varchar(255) NULL COMMENT '退回原因' AFTER `back_flag`,
ADD COLUMN `last_modify_user`  varchar(20) NULL COMMENT '最后更新者' AFTER `back_result`,
ADD COLUMN `last_modify_time`  datetime NULL COMMENT '最后更新时间' AFTER `last_modify_user`,
ADD COLUMN `tips`  varchar(255) NULL COMMENT '提示消息' AFTER `organization`,
ADD COLUMN `back_time`  datetime NULL COMMENT '退回时间' AFTER `back_result`,
ADD COLUMN `test_orgnization`  varchar(100) NULL COMMENT '检验机关' AFTER `tips`,
ADD COLUMN `mk_publish_time`  datetime NULL COMMENT 'easy发布时间' AFTER `mk_publish_flag`,
ADD COLUMN `auto_report_flag`  bit(1) NULL DEFAULT b'0' COMMENT '自动生成报告标志' AFTER `test_orgnization`,
ADD COLUMN `upload_path`  varchar(100) NULL AFTER `auto_report_flag`,
ADD COLUMN `mk_db_flag`  char(1) NULL DEFAULT '3' COMMENT '1:工商，2:lims 2.0 , 3:easy录入数据' AFTER `upload_path`;