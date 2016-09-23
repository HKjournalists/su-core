CREATE TABLE `business_market_to_business` (
`id`  bigint(20) NOT NULL AUTO_INCREMENT ,
`business_id`  bigint(20) NULL COMMENT '企业id' ,
`name`  varchar(200) NOT NULL COMMENT '商户名称' ,
`license`  varchar(50) NOT NULL COMMENT '营业执照号' ,
`personInCharge`  varchar(30) NOT NULL COMMENT '负责人' ,
`telephone`  varchar(20) NOT NULL COMMENT '联系电话' ,
`email`  varchar(50) NOT NULL COMMENT '邮箱' ,
`date`  date NULL COMMENT '最后一次发送邮件通知日期' ,
`count`  bigint(2) NULL COMMENT '最后一天发送邮件次数' ,
`organization`  bigint(20) NOT NULL COMMENT '交易市场组织机构' ,
PRIMARY KEY (`id`),
FOREIGN KEY (`business_id`) REFERENCES `business_unit` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
)ENGINE=InnoDB DEFAULT CHARSET=utf8
;

