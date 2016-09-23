ALTER TABLE `license_info`
ADD COLUMN `other_address`  varchar(300) NULL DEFAULT NULL COMMENT '地址别名' AFTER `business_address`;