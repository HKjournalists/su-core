ALTER TABLE `t_meta_purchaseorder_info`
MODIFY COLUMN `po_product_info_id`  varchar(20) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL COMMENT '商品编号' AFTER `po_isgift`;

ALTER TABLE `t_meta_purchaseorder_info`
MODIFY COLUMN `po_product_info_id`  varchar(100) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL COMMENT '商品编号' AFTER `po_isgift`;

ALTER TABLE `t_meta_purchaseorder_info` ADD CONSTRAINT `t_meta_purchaseorder_info_fk_1` FOREIGN KEY (`po_product_info_id`) REFERENCES `product` (`barcode`) ON DELETE RESTRICT ON UPDATE RESTRICT;

