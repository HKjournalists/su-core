ALTER TABLE `test_result`
MODIFY COLUMN `publish_flag`  char(1) NULL DEFAULT NULL COMMENT '������portal��־' AFTER `pdf_report`;