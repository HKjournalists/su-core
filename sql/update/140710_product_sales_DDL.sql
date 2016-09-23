ALTER TABLE `product_sales`
MODIFY COLUMN `sales_quantity`  varchar(200) NULL DEFAULT NULL COMMENT '销售数量' AFTER `sales_territory`,
ADD COLUMN `sales_date`  datetime NULL DEFAULT NULL COMMENT '销售时间' AFTER `sales_territory`;