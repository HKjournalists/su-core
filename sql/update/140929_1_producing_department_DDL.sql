ALTER TABLE `producing_department`
MODIFY COLUMN `in_commission_number`  varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '投产窖池数' AFTER `telephone`;