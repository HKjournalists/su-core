CREATE TABLE `business_market` (
`id`  bigint(20) NOT NULL AUTO_INCREMENT,
`business_id`  bigint(20) NOT NULL COMMENT '��ҵ��Ϣ' ,
`administrative`  bigint(20) NULL COMMENT '��������' ,
`office`  bigint(20) NULL COMMENT '��Ͻʳҩ�����' ,
`placeType`  bigint(20) NULL COMMENT '��������' ,
`areaType`  bigint(20) NULL COMMENT '��������' ,
`territoryType`  bigint(20) NULL COMMENT '��������' ,
`prepositionQS`  varchar(50) NULL COMMENT 'ǰ�����֤��' ,
`buildName`  varchar(50) NULL COMMENT '���췽����' ,
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

