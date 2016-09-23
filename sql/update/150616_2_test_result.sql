ALTER TABLE `test_result`
ADD COLUMN `create_user`  varchar(50) NOT NULL DEFAULT '' AFTER `back_time`,
ADD COLUMN `create_time`  datetime NULL AFTER `create_user`;