update test_result set sign_flag='0' where sign_flag is null;
ALTER TABLE `test_result`
MODIFY COLUMN `sign_flag`  char(1) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL DEFAULT '0' COMMENT '0:δǩ' AFTER `mk_db_flag`;
