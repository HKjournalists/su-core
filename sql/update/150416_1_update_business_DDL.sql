ALTER TABLE `business_unit`
MODIFY COLUMN `about`  varchar(2000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '企业简介' AFTER `sign_flag`;