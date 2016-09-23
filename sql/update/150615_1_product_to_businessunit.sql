ALTER TABLE `product_to_businessunit`
ADD COLUMN `effect`  tinyint UNSIGNED NOT NULL DEFAULT 1 COMMENT '0：代表当前qs号已经无效，无法继续使用其录报告 1：代表有效，可以继续使用其录报告' AFTER `bind`;

ALTER TABLE `product_to_businessunit` DROP FOREIGN KEY `FK_businessunit_id_to_busid`;

ALTER TABLE `product_to_businessunit` DROP FOREIGN KEY `FK_product_id_to_product_id`;