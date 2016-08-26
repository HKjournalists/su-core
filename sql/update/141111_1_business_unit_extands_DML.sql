CREATE TABLE `business_unit_to_lims` (
`id`  bigint(20) NOT NULL AUTO_INCREMENT ,
`create_user`  varchar(255) NULL COMMENT '创建者名称' ,
`create_time`  datetime NULL COMMENT '创建日期' ,
`edition`  varchar(255) NULL COMMENT '数据来源/环境：XX LIMS' ,
PRIMARY KEY (`id`));
