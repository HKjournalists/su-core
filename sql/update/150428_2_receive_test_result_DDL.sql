ALTER TABLE `receive_test_result`
ADD COLUMN `create_time`  datetime NOT NULL COMMENT '创建时间' AFTER `qs_no`;