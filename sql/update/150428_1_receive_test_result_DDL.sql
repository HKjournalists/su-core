ALTER TABLE `receive_test_result`
MODIFY COLUMN `retailer_id`  varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '����Ʒ������Ψһ��ʶ' AFTER `test_date`,
MODIFY COLUMN `retailer_name`  varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '�����Ʒ����������' AFTER `retailer_id`,
MODIFY COLUMN `retailer_licenseno`  varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '�����Ʒ������Ӫҵִ�պ�' AFTER `retailer_name`;