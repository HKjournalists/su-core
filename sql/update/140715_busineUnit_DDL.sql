ALTER TABLE `business_unit`
MODIFY COLUMN `type`  varchar(20) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '类型（生产，流通，餐饮）' AFTER `address`;