CREATE TABLE `t_meta_initialize_product` (
`id`  bigint(20) NOT NULL ,
`product_id`  bigint(20) NOT NULL ,
`first_storage_id`  varchar(20) NULL ,
`TYPE_ID`  int(11) NULL ,
`SAFE_NUMBER`  bigint(20) NULL ,
`INSPECTION_REPORT`  bit(1) NULL ,
`local`  bit(1) NOT NULL ,
`organization`  bigint(20) NOT NULL ,
PRIMARY KEY (`id`),
CONSTRAINT `t_meta_initialize_product_fk1` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
CONSTRAINT `t_meta_initialize_product_fk2` FOREIGN KEY (`first_storage_id`) REFERENCES `t_meta_storage_info` (`NO`) ON DELETE CASCADE ON UPDATE CASCADE,
CONSTRAINT `t_meta_initialize_product_fk3` FOREIGN KEY (`TYPE_ID`) REFERENCES `t_meta_merchandise_type` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE
)ENGINE=InnoDB DEFAULT CHARSET=utf8;
;
ALTER TABLE `t_meta_initialize_product`
MODIFY COLUMN `id`  bigint(20) NOT NULL AUTO_INCREMENT FIRST ;

ALTER TABLE `t_meta_initialize_product`
MODIFY COLUMN `product_id`  bigint(20) NOT NULL COMMENT '��ƷID' AFTER `id`,
MODIFY COLUMN `first_storage_id`  varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '��ѡ�ֿ�' AFTER `product_id`,
MODIFY COLUMN `TYPE_ID`  int(11) NULL DEFAULT NULL COMMENT '��Ʒ����' AFTER `first_storage_id`,
MODIFY COLUMN `SAFE_NUMBER`  bigint(20) NULL DEFAULT NULL COMMENT '��ȫ���' AFTER `TYPE_ID`,
MODIFY COLUMN `INSPECTION_REPORT`  bit(1) NULL DEFAULT NULL COMMENT '�Ƿ���Ҫ�ʼ챨��' AFTER `SAFE_NUMBER`,
MODIFY COLUMN `local`  bit(1) NOT NULL COMMENT '��Ʒ�Ƿ��Ǳ���ҵ' AFTER `INSPECTION_REPORT`,
MODIFY COLUMN `organization`  bigint(20) NOT NULL COMMENT '��֯�ṹID' AFTER `local`;


