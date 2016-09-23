ALTER TABLE `business_unit`
ADD COLUMN `other_address`  varchar(300) NULL DEFAULT NULL COMMENT '地址别名' AFTER `address`;