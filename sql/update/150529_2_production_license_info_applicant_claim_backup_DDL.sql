CREATE TABLE `production_license_info_applicant_claim_backup` (
`id`  bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT ,
`qs_id`  bigint NOT NULL ,
`busunit_name`  varchar(200) NOT NULL DEFAULT '' COMMENT '生产企业名称' ,
`product_name`  varchar(200) NOT NULL DEFAULT '' COMMENT '产品名称' ,
`start_time`  datetime NULL COMMENT '有效期: 开始时间' ,
`end_time`  datetime NULL COMMENT '有效期: 结束时间' ,
`accommodation`  varchar(200) NOT NULL DEFAULT '' COMMENT '住所' ,
`accommodation_other_address`  varchar(300) NOT NULL DEFAULT '' COMMENT '住所别名' ,
`production_address`  varchar(200) NOT NULL DEFAULT '' COMMENT '生产地址' ,
`production_other_address`  varchar(300) NOT NULL DEFAULT '' COMMENT '地址别名' ,
`check_type`  varchar(50) NOT NULL DEFAULT '' COMMENT '检验方式' ,
`qsformat_id`  int NOT NULL ,
PRIMARY KEY (`id`)
);