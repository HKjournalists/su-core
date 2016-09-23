ALTER TABLE `test_result`
ADD COLUMN `sign_flag`  char(1) NULL COMMENT '0:Î´Ç©Ãû  1:ÒÑÇ©Ãû' AFTER `mk_db_flag`;