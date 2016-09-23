ALTER TABLE `business_unit`
ADD COLUMN `lims_busunit_id`  bigint(20) NULL DEFAULT NULL COMMENT 'lims保存企业的扩展信息id' AFTER `wda_back_msg`;