ALTER TABLE `receive_test_result`
MODIFY COLUMN `retailer_id`  varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '测样品零售商唯一标识' AFTER `test_date`,
MODIFY COLUMN `retailer_name`  varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '检测样品零售商名称' AFTER `retailer_id`,
MODIFY COLUMN `retailer_licenseno`  varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '检测样品零售商营业执照号' AFTER `retailer_name`;