ALTER TABLE `license_info`
ADD COLUMN `establish_time`  datetime NULL DEFAULT NULL COMMENT '成立时间' AFTER `practical_capital`;