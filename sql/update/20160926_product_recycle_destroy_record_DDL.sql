ALTER TABLE `product_recycle_destroy_record`
ADD COLUMN `organization`  bigint(20) NULL DEFAULT NULL COMMENT '所属企业组织机构ID' AFTER `operation_user`;
