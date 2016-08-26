ALTER TABLE `test_result`
ADD COLUMN `check_org_name`  varchar(30) NOT NULL DEFAULT '' COMMENT '审核人' AFTER `rpt_id`;