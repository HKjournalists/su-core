CREATE TABLE `t_meta_customer_and_provider_type` (
`ID`  int(11) NOT NULL AUTO_INCREMENT ,
`NAME`  varchar(50) NULL ,
`TYPE`  int(11) NULL COMMENT '1����ͻ�����2������������' ,
`organization`  bigint(20) NULL ,
PRIMARY KEY (`ID`)
)
;