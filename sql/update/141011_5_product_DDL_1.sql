ALTER TABLE `product`
ADD COLUMN `first_storage_id`  varchar(20) NULL AFTER `last_modify_time`;

ALTER TABLE `product`
ADD COLUMN `TYPE_ID`  int NULL AFTER `first_storage_id`;

ALTER TABLE `product`
ADD COLUMN `UNIT_ID`  int NULL AFTER `TYPE_ID`;

ALTER TABLE `product`
ADD COLUMN `SAFE_NUMBER`  bigint NULL COMMENT '��Ʒ��ȫ���' AFTER `UNIT_ID`;

ALTER TABLE `product`
MODIFY COLUMN `first_storage_id`  varchar(20) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '��ѡ�ֿ�' AFTER `last_modify_time`;

ALTER TABLE `product`
MODIFY COLUMN `TYPE_ID`  int(11) NULL DEFAULT NULL COMMENT '��Ʒ����id' AFTER `first_storage_id`;

ALTER TABLE `product`
MODIFY COLUMN `UNIT_ID`  int(11) NULL DEFAULT NULL COMMENT '��Ʒ��λid' AFTER `TYPE_ID`;

ALTER TABLE `product`
ADD COLUMN `INSPECTION_REPORT`  bit NULL COMMENT '�Ƿ���Ҫ�ʼ챨��' AFTER `SAFE_NUMBER`;

ALTER TABLE `product`
MODIFY COLUMN `first_storage_id`  varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '��ѡ�ֿ�' AFTER `last_modify_time`;

ALTER TABLE `product` ADD CONSTRAINT `product_fk1` FOREIGN KEY (`first_storage_id`) REFERENCES `t_meta_storage_info` (`NO`) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE `product` ADD CONSTRAINT `product_fk2` FOREIGN KEY (`TYPE_ID`) REFERENCES `t_meta_merchandise_type` (`ID`) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE `product` ADD CONSTRAINT `product_fk3` FOREIGN KEY (`UNIT_ID`) REFERENCES `t_meta_unit` (`ID`) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE `product`
ADD COLUMN `PRODUCT_CATEGORY_ID`  bigint NULL COMMENT '��Ʒ����id' AFTER `INSPECTION_REPORT`;

ALTER TABLE `product` ADD CONSTRAINT `product_fk4` FOREIGN KEY (`PRODUCT_CATEGORY_ID`) REFERENCES `product_category` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE `product` DROP FOREIGN KEY `product_fk4`;

ALTER TABLE `product`
DROP COLUMN `PRODUCT_CATEGORY_ID`;

ALTER TABLE `product` ADD CONSTRAINT `product_fk4` FOREIGN KEY (`category`) REFERENCES `product_category` (`code`) ON DELETE RESTRICT ON UPDATE RESTRICT;

