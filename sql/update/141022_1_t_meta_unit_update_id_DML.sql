ALTER TABLE `product` DROP FOREIGN KEY `product_fk3`;

ALTER TABLE `t_buss_business_to_merchandises` DROP FOREIGN KEY `t_buss_business_to_merchandises_fk1`;

ALTER TABLE `t_meta_out_goods_info` DROP FOREIGN KEY `t_meta_out_goods_info_fk_2`;

ALTER TABLE `t_meta_purchaseorder_info` DROP FOREIGN KEY `t_meta_purchaseorder_info_fk_2`;

ALTER TABLE `t_meta_receivingnote` DROP FOREIGN KEY `t_meta_receivingnote_fk_3`;




ALTER TABLE `t_meta_unit`
MODIFY COLUMN `ID`  bigint(20) NOT NULL AUTO_INCREMENT FIRST ;



ALTER TABLE `product`
MODIFY COLUMN `UNIT_ID`  bigint(20) NULL DEFAULT NULL COMMENT '' AFTER `TYPE_ID`;

ALTER TABLE `t_buss_business_to_merchandises`
MODIFY COLUMN `unit`  bigint(20) NULL DEFAULT NULL AFTER `PRICE`;

ALTER TABLE `t_meta_out_goods_info`
MODIFY COLUMN `UNIT_ID`  bigint(20) NULL DEFAULT NULL COMMENT '单位' AFTER `first_storage_id`;

ALTER TABLE `t_meta_purchaseorder_info`
MODIFY COLUMN `po_unit`  bigint(20) NULL DEFAULT NULL COMMENT '单位' AFTER `po_receivenum`;

ALTER TABLE `t_meta_receivingnote`
MODIFY COLUMN `re_unit`  bigint(20) NULL DEFAULT NULL COMMENT '单位' AFTER `re_contact_id`;




ALTER TABLE `product` ADD CONSTRAINT `product_fk3` FOREIGN KEY (`UNIT_ID`) REFERENCES `t_meta_unit` (`ID`) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE `t_buss_business_to_merchandises` ADD CONSTRAINT `t_buss_business_to_merchandises_fk1` FOREIGN KEY (`unit`) REFERENCES `t_meta_unit` (`ID`) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE `t_meta_out_goods_info` ADD CONSTRAINT `t_meta_out_goods_info_fk_2` FOREIGN KEY (`UNIT_ID`) REFERENCES `t_meta_unit` (`ID`) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE `t_meta_purchaseorder_info` ADD CONSTRAINT `t_meta_purchaseorder_info_fk_2` FOREIGN KEY (`po_unit`) REFERENCES `t_meta_unit` (`ID`) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE `t_meta_receivingnote` ADD CONSTRAINT `t_meta_receivingnote_fk_3` FOREIGN KEY (`re_unit`) REFERENCES `t_meta_unit` (`ID`) ON DELETE RESTRICT ON UPDATE RESTRICT;


