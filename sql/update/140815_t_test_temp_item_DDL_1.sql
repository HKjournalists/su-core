ALTER TABLE `t_test_temp_item`
MODIFY COLUMN `ITEM_UNIT`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL AFTER `ITEM_NAME`,
MODIFY COLUMN `SPECIFICATION`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL AFTER `ITEM_UNIT`,
MODIFY COLUMN `TEST_RESULT`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL AFTER `SPECIFICATION`,
MODIFY COLUMN `ITEM_CONCLUSION`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL AFTER `TEST_RESULT`,
MODIFY COLUMN `STANDARD`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL AFTER `ITEM_CONCLUSION`;