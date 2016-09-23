ALTER TABLE `test_result`
ADD COLUMN `fullPdfPath`  varchar(1024) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT '' COMMENT '政府抽检报告完整的报告存放路径' AFTER `pdf_report`;