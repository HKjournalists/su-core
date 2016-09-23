CREATE TABLE `t_business_to_sys_dict` (
`business_id`  bigint(20) NOT NULL COMMENT '企业id' ,
`dict_id`  bigint(20) NOT NULL COMMENT '主体类型id' ,
PRIMARY KEY (`business_id`, `dict_id`));

ALTER TABLE `t_business_to_sys_dict` ADD CONSTRAINT `fk_busId_to_bus_id` FOREIGN KEY (`business_id`) REFERENCES `business_unit` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT;
ALTER TABLE `t_business_to_sys_dict` ADD CONSTRAINT `fk_dictId_to_dict_id` FOREIGN KEY (`dict_id`) REFERENCES `sys_dict` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT;

