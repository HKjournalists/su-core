ALTER TABLE `business_unit`
ADD COLUMN `wda_back_flag`  bit(1) NULL DEFAULT b'0' COMMENT '监管系统退回标志，1:退回，0:没有退回' AFTER `busunit_template_id`,
ADD COLUMN `wda_back_msg`  varchar(300) NULL DEFAULT NULL COMMENT '监管系统退回原因' AFTER `wda_back_flag`;