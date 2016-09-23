DELETE from test_result WHERE publish_flag='';
ALTER TABLE `test_result`
DROP COLUMN `mk_publish_flag`,
DROP COLUMN `mk_publish_time`,
DROP COLUMN `back_flag`;