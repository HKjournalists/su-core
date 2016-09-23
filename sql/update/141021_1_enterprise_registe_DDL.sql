ALTER TABLE `enterprise_registe`
ADD COLUMN `telephone`  varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '联系电话' AFTER `legalPerson`;