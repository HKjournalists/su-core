ALTER TABLE `business_unit`
MODIFY COLUMN `orgnization`  bigint(20) NULL DEFAULT 0 COMMENT '父组织机构ID' AFTER `operating_period`,
ADD COLUMN `parentOrgnization`  bigint(20) NULL AFTER `operating_period`;