ALTER TABLE `test_result`
MODIFY COLUMN `sample_quantity`  varchar(100) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '������' AFTER `sample_id`,
MODIFY COLUMN `sampling_location`  varchar(200) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '�����ص�' AFTER `sample_quantity`;