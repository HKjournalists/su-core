ALTER TABLE `test_result`
ADD COLUMN `pub_user_name`  varchar(20) NULL COMMENT '发布者名称' AFTER `last_modify_time`;