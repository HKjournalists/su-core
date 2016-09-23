CREATE TABLE `business_pdf_to_resource` (
`business_id`  bigint(20) NULL ,
`resource_id`  bigint(20) NULL ,
PRIMARY KEY (`business_id`, `resource_id`),
CONSTRAINT `fk_business_id_to_businessId` FOREIGN KEY (`business_id`) REFERENCES `business_unit` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
CONSTRAINT `fk_resource_id_to_resId` FOREIGN KEY (`resource_id`) REFERENCES `t_test_resource` (`RESOURCE_ID`) ON DELETE RESTRICT ON UPDATE RESTRICT
);