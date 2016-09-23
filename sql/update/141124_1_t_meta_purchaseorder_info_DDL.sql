ALTER TABLE `t_meta_purchaseorder_info`
DROP COLUMN `po_qualityreport`,
ADD COLUMN `hasreport`  bit(1) NULL COMMENT '是否有质检报告' AFTER `po_mtype`;

