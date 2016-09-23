ALTER TABLE `t_meta_merchandise_info_instance`
MODIFY COLUMN `MERCHANDISE_INFO_ID`  varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '商品编号' AFTER `BATCH_NUMBER`;

ALTER TABLE `t_meta_merchandise_info_instance`
MODIFY COLUMN `MERCHANDISE_INFO_ID`  varchar(100) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL COMMENT '商品编号' AFTER `BATCH_NUMBER`;

ALTER TABLE `t_meta_merchandise_info_instance` ADD CONSTRAINT `t_meta_merchandise_info_instance_fk1` FOREIGN KEY (`MERCHANDISE_INFO_ID`) REFERENCES `product` (`barcode`) ON DELETE RESTRICT ON UPDATE RESTRICT;



