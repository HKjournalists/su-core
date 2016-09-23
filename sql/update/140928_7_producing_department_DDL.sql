ALTER TABLE `producing_department`
ADD COLUMN `business_id`  bigint(20) NULL DEFAULT NULL COMMENT '企业id ' AFTER `id`;