ALTER TABLE `business_unit`
ADD COLUMN `postal_code`  varchar(20) BINARY CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '邮政编码' AFTER `note`;