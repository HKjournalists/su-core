ALTER TABLE `procurement_info`
ADD COLUMN `production_date`  datetime NULL DEFAULT NULL COMMENT '生产日期' AFTER `procurement_date`,
ADD COLUMN `expiration`  int(11) NULL DEFAULT 0 COMMENT '保质期 天' AFTER `production_date`;