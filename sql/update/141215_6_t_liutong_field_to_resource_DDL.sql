CREATE TABLE `t_liutong_field_to_resource` (
`liutong_field_id`  bigint(20) NOT NULL ,
`resource_id`  bigint(20) NOT NULL ,
PRIMARY KEY (`liutong_field_id`, `resource_id`),
CONSTRAINT `fk_lftr_liutong_field_id_tot_liutong_field_value_id` FOREIGN KEY (`liutong_field_id`) REFERENCES `t_liutong_field_value` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
CONSTRAINT `fk_lftr_resource_id_to_resource_id` FOREIGN KEY (`resource_id`) REFERENCES `t_test_resource` (`RESOURCE_ID`) ON DELETE RESTRICT ON UPDATE RESTRICT
)ENGINE=InnoDB DEFAULT CHARSET=utf8;