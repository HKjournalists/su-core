ALTER TABLE `test_result`
MODIFY COLUMN `back_count`  smallint UNSIGNED NOT NULL DEFAULT 0 COMMENT '结构化人员退回次数' AFTER `RECORD_INSERT_TIME`,
ADD COLUMN `suppliers_back_count`  smallint UNSIGNED NOT NULL COMMENT '供应商退回次数' AFTER `back_count`;

