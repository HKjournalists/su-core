
ALTER TABLE `product` DROP FOREIGN KEY `product_fk4`;

ALTER TABLE `product`
ADD COLUMN `category_id`  bigint(20) NULL COMMENT '产品分类' AFTER `ICB_category`;

ALTER TABLE `product` ADD FOREIGN KEY (`category_id`) REFERENCES `product_category_info` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE `product`
ADD COLUMN `regularity_id`  bigint(20) NULL COMMENT '执行标准' AFTER `format_pdf`;
ALTER TABLE `product` ADD FOREIGN KEY (`regularity_id`) REFERENCES `product_category_info` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT;
