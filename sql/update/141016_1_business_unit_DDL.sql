ALTER TABLE `business_unit`
CHANGE COLUMN `orgnization` `organization`  bigint(20) NULL DEFAULT 0 COMMENT '��֯����ID' AFTER `parentOrgnization`;