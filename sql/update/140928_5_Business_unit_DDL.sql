ALTER TABLE `business_unit`
ADD COLUMN `tax_register_id`  bigint(20) NULL DEFAULT NULL COMMENT '税务登记表id' AFTER `hot_flag`,
ADD COLUMN `liquor_sales_id`  bigint(20) NULL DEFAULT NULL COMMENT '酒类销售许可证表id' AFTER `tax_register_id`;