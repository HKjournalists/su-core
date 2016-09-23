ALTER TABLE `product_to_businessunit`
ADD COLUMN `bind`  tinyint UNSIGNED NOT NULL DEFAULT 0 COMMENT '0 代表该qs号不是生产企业给产品绑定的qs号  1代表该qs号是生产企业给产品绑定的qs号' AFTER `business_id`;