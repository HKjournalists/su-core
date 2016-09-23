CREATE TABLE `business_certification` (
`id`  bigint(20) NOT NULL AUTO_INCREMENT ,
`cert_id`  bigint(20) NULL COMMENT '标准认证id' ,
`enddate`  datetime NULL COMMENT '认证的截止日期' ,
`resource_id`  bigint(20) NULL COMMENT '认证关联的图片资源id' ,
`business_id`  bigint(20) NULL COMMENT '企业id' ,
PRIMARY KEY (`id`)
);

ALTER TABLE `business_certification` ADD CONSTRAINT `fk_cert_id_11` FOREIGN KEY (`cert_id`) REFERENCES `certification` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE `business_certification` ADD CONSTRAINT `fk_resource_id_2` FOREIGN KEY (`resource_id`) REFERENCES `t_test_resource` (`RESOURCE_ID`) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE `business_certification` ADD CONSTRAINT `fk_bus_id_3` FOREIGN KEY (`business_id`) REFERENCES `business_unit` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT;


