CREATE TABLE `t_tax_register_cert_to_resource` (
`ENTERPRISE_REGISTE_ID`  bigint(20) NULL ,
`RESOURCE_ID`  bigint(20) NULL ,
PRIMARY KEY (`ENTERPRISE_REGISTE_ID`, `RESOURCE_ID`),
CONSTRAINT `fk_enterprise_id` FOREIGN KEY (`ENTERPRISE_REGISTE_ID`) REFERENCES `enterprise_registe` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
CONSTRAINT `fk_resource_id` FOREIGN KEY (`RESOURCE_ID`) REFERENCES `t_test_resource` (`RESOURCE_ID`) ON DELETE RESTRICT ON UPDATE RESTRICT
);