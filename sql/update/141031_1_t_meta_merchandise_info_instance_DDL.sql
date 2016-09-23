ALTER TABLE `t_meta_merchandise_info_instance` DROP FOREIGN KEY `t_meta_merchandise_info_instance_fk1`;

ALTER TABLE `t_meta_merchandise_info_instance`
MODIFY COLUMN `INSTANCE_ID`  bigint(20) NOT NULL AUTO_INCREMENT FIRST ,
MODIFY COLUMN `BATCH_NUMBER`  varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '商品批次' AFTER `INSTANCE_ID`,
CHANGE COLUMN `MERCHANDISE_INFO_ID` `product_id`  bigint(20) NOT NULL AFTER `BATCH_NUMBER`;

ALTER TABLE `t_meta_merchandise_info_instance` ADD FOREIGN KEY (`product_id`) REFERENCES `product` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

