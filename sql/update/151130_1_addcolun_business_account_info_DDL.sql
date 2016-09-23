ALTER TABLE `tz_business_account_info`
ADD COLUMN `report_id`  bigint(20) NULL COMMENT '对应绑定的报告id' AFTER `RECORD_INSERT_TIME`;
