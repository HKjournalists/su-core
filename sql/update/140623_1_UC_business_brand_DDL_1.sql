ALTER TABLE `business_brand`
ADD COLUMN `last_modify_time`  datetime NULL COMMENT '最后更新时间' AFTER `organization`;