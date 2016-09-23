ALTER TABLE `t_std_kms_label` ADD INDEX (`kms_label_id`);

CREATE TABLE `qrcode_product_info` (
`id`  bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID主键' ,
`product_id`  bigint(20) NOT NULL COMMENT '产品ID' ,
`inner_code`  varchar(50) NULL COMMENT '超市内部码' ,
`product_area_code`  varchar(10) NULL COMMENT '产品区域代码' ,
`kms_label_id`  bigint(20) NULL COMMENT 'kms提供的标签ID',
`serial_number`  bigint(20) NOT NULL COMMENT '流水号',
`resource_id`  bigint(20) NULL COMMENT '二维码图片ID' ,
PRIMARY KEY (`id`),
CONSTRAINT `fk_qpi_product_id` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
CONSTRAINT `fk_qpi_kms_label_id` FOREIGN KEY (`kms_label_id`) REFERENCES `t_std_kms_label` (`kms_label_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
CONSTRAINT `fk_qpi_resource_id` FOREIGN KEY (`resource_id`) REFERENCES `t_test_resource` (`RESOURCE_ID`) ON DELETE RESTRICT ON UPDATE RESTRICT
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

