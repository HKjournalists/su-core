ALTER TABLE `t_meta_purchaseorder_info`
DROP COLUMN `po_qualityreport`,
ADD COLUMN `hasreport`  bit(1) NULL COMMENT '�Ƿ����ʼ챨��' AFTER `po_mtype`;

