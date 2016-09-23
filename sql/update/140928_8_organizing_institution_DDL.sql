ALTER TABLE `organizing_institution`
ADD COLUMN `register_no`  varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '登记号' AFTER `other_address`;