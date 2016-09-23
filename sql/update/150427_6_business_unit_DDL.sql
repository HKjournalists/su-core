ALTER TABLE `business_unit`
ADD COLUMN `guid`  varchar(50) NULL DEFAULT '' COMMENT '企业唯一标识' AFTER `website`;