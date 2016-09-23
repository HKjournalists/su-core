ALTER TABLE `t_meta_out_goods_info`
MODIFY COLUMN `CATEGORY_ID`  bigint(11) NULL DEFAULT NULL COMMENT '商品分类' AFTER `TYPE_ID`;

ALTER TABLE `t_meta_out_goods_info` ADD CONSTRAINT `t_meta_out_goods_info_fk_4` FOREIGN KEY (`CATEGORY_ID`) REFERENCES `product_category` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT;
