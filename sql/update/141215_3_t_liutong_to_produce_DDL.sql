ALTER TABLE `business_unit` ADD INDEX (`organization`);

CREATE TABLE `t_liutong_to_produce` (
`id`  bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键' ,
`liutong_org_id`  bigint(20) NOT NULL COMMENT '流通企业组织机构id' ,
`produce_bus_id`  bigint(20) NOT NULL COMMENT '生产企业id' ,
`producer_name`  varchar(255) NULL COMMENT '生产企业名称' ,
`full_flag`  bit(1) NOT NULL DEFAULT b'0' COMMENT '资料是否完整' ,
`msg`  varchar(300) NULL DEFAULT NULL COMMENT '提示信息' ,
PRIMARY KEY (`id`),
CONSTRAINT `fk_liutong_org_id_to_business_unit_orgid` FOREIGN KEY (`liutong_org_id`) REFERENCES `business_unit` (`organization`) ON DELETE RESTRICT ON UPDATE RESTRICT,
CONSTRAINT `fk_produce_bus_id_to_business_unit_id` FOREIGN KEY (`produce_bus_id`) REFERENCES `business_unit` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
)ENGINE=InnoDB DEFAULT CHARSET=utf8;