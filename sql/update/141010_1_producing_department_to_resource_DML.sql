CREATE TABLE `producing_department_to_resource` (
`pro_dep_id`  bigint(20) NULL ,
`resource_id`  bigint(20) NULL ,
PRIMARY KEY (`pro_dep_id`, `resource_id`),
CONSTRAINT `fk_pro_dep_id_to_id` FOREIGN KEY (`pro_dep_id`) REFERENCES `producing_department` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
CONSTRAINT `fk_pro_resId_to_resId` FOREIGN KEY (`resource_id`) REFERENCES `t_test_resource` (`RESOURCE_ID`) ON DELETE RESTRICT ON UPDATE RESTRICT
);