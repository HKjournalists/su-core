ALTER TABLE `test_result`
MODIFY COLUMN `publish_flag`  char(1) NULL DEFAULT NULL COMMENT '发布到portal标志' AFTER `pdf_report`;