CREATE TABLE `t_liutong_field_value` (
`id`  bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID' ,
`produce_bus_id`  bigint(20) NOT NULL COMMENT '生产企业ID' ,
`value`  varchar(50) NULL COMMENT '字段值' ,
`full_flag`  bit(1) NULL COMMENT '资料完整性标志' ,
`display`  varchar(100) NULL COMMENT '字段类型' ,
`pass_flag`  varchar(50) NULL COMMENT '审核标记' ,
`msg`  varchar(300) NULL COMMENT '提示信息' ,
PRIMARY KEY (`id`),
CONSTRAINT `fk_lfv_produce_bus_id_to_business_unit_id` FOREIGN KEY (`produce_bus_id`) REFERENCES `business_unit` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
)ENGINE=InnoDB DEFAULT CHARSET=utf8;