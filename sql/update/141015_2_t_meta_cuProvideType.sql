CREATE TABLE `t_meta_customer_and_provider_type` (
`ID`  int(11) NOT NULL AUTO_INCREMENT ,
`NAME`  varchar(50) NULL ,
`TYPE`  int(11) NULL COMMENT '1代表客户类型2代表供货商类型' ,
`organization`  bigint(20) NULL ,
PRIMARY KEY (`ID`)
)
;