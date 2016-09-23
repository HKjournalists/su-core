CREATE TABLE `business_market_to_business` (
`id`  bigint(20) NOT NULL AUTO_INCREMENT ,
`business_id`  bigint(20) NULL COMMENT '��ҵid' ,
`name`  varchar(200) NOT NULL COMMENT '�̻�����' ,
`license`  varchar(50) NOT NULL COMMENT 'Ӫҵִ�պ�' ,
`personInCharge`  varchar(30) NOT NULL COMMENT '������' ,
`telephone`  varchar(20) NOT NULL COMMENT '��ϵ�绰' ,
`email`  varchar(50) NOT NULL COMMENT '����' ,
`date`  date NULL COMMENT '���һ�η����ʼ�֪ͨ����' ,
`count`  bigint(2) NULL COMMENT '���һ�췢���ʼ�����' ,
`organization`  bigint(20) NOT NULL COMMENT '�����г���֯����' ,
PRIMARY KEY (`id`),
FOREIGN KEY (`business_id`) REFERENCES `business_unit` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
)ENGINE=InnoDB DEFAULT CHARSET=utf8
;

