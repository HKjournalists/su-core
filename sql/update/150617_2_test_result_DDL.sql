ALTER TABLE `test_result`
ADD COLUMN `del`  tinyint UNSIGNED NOT NULL DEFAULT 0 COMMENT '0代表未删除  1代表已经删除' AFTER `end_date`;