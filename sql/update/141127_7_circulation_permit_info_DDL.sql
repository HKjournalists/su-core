ALTER TABLE `circulation_permit_info`
ADD COLUMN `tolerance_time`  datetime NULL COMMENT '许可时间' AFTER `legal_name`,
ADD COLUMN `manage_type`  varchar(255) NULL COMMENT '经营方式' AFTER `tolerance_time`,
ADD COLUMN `manage_project`  varchar(255) NULL COMMENT '经营项目' AFTER `manage_type`;