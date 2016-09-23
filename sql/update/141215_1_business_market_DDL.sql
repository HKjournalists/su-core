CREATE TABLE `business_market` (
`id`  bigint(20) NOT NULL AUTO_INCREMENT,
`business_id`  bigint(20) NOT NULL COMMENT '企业信息' ,
`administrative`  bigint(20) NULL COMMENT '行政区划' ,
`office`  bigint(20) NULL COMMENT '管辖食药监机关' ,
`placeType`  bigint(20) NULL COMMENT '场所类型' ,
`areaType`  bigint(20) NULL COMMENT '地域类型' ,
`territoryType`  bigint(20) NULL COMMENT '地区类型' ,
`prepositionQS`  varchar(50) NULL COMMENT '前置许可证号' ,
`buildName`  varchar(50) NULL COMMENT '开办方名称' ,
`note`  varchar(200) NULL COMMENT 'note' ,
PRIMARY KEY (`id`),
FOREIGN KEY (`business_id`) REFERENCES `business_unit` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
FOREIGN KEY (`administrative`) REFERENCES `sys_area` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
FOREIGN KEY (`office`) REFERENCES `sys_office` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
FOREIGN KEY (`placeType`) REFERENCES `sys_dict` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
FOREIGN KEY (`areaType`) REFERENCES `sys_dict` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
FOREIGN KEY (`territoryType`) REFERENCES `sys_dict` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
)ENGINE=InnoDB DEFAULT CHARSET=utf8
;

