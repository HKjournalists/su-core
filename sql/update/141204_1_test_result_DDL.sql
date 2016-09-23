ALTER TABLE `test_result`
ADD COLUMN `end_date`  datetime NULL DEFAULT NULL COMMENT '有效期至' AFTER `sign_flag`;

ALTER TABLE `t_test_temp_report`
ADD COLUMN `END_DATE`  datetime NULL COMMENT '有效期至' AFTER `SAM_PLACE`;