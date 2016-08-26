ALTER TABLE `business_unit`
CHANGE COLUMN `orgnization` `organization`  bigint(20) NULL DEFAULT 0 COMMENT '组织机构ID' AFTER `parentOrgnization`;