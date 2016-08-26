CREATE TABLE `t_liutong_produce_qs` (
`id`  bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID' ,
`liutong_org_id`  bigint(20) NOT NULL COMMENT '流通企业组织机构id' ,
`produce_bus_id`  bigint(20) NOT NULL COMMENT '生产企业id' ,
`qs_no`  varchar(50) NULL COMMENT 'qs号' ,
`full_flag`  bit(1) NULL COMMENT '资料是否完整' ,
`pass_flag`  varchar(50) NULL COMMENT '审核标志' ,
`msg`  varchar(300) NULL COMMENT '提示信息' ,
PRIMARY KEY (`id`),
CONSTRAINT `fk_lps_liutong_org_id_to_business_unit_orgid` FOREIGN KEY (`liutong_org_id`) REFERENCES `business_unit` (`organization`) ON DELETE RESTRICT ON UPDATE RESTRICT,
CONSTRAINT `fk_lps_produce_bus_id_to_business_unit_id` FOREIGN KEY (`produce_bus_id`) REFERENCES `business_unit` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
)ENGINE=InnoDB DEFAULT CHARSET=utf8;