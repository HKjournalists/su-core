ALTER TABLE `t_meta_out_goods_info` DROP FOREIGN KEY `t_meta_out_goods_info_fk_4`;

ALTER TABLE `t_meta_out_goods_info`
ADD COLUMN `category`  bigint(20) NULL COMMENT '产品分类' AFTER `TYPE_ID`;

ALTER TABLE `t_meta_out_goods_info` ADD FOREIGN KEY (`category`) REFERENCES `product_category_info` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT;

