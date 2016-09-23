ALTER TABLE `receive_test_result`
MODIFY COLUMN `attachments`  varchar(300) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '检测样品照片文件名' AFTER `edition`,
ADD COLUMN `ftp_attachments`  varchar(300) NOT NULL COMMENT '检测样品照片文件名 ftp url' AFTER `attachments`;