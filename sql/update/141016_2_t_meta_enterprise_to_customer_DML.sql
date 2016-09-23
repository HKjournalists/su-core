CREATE TABLE `t_meta_enterprise_to_customer` (
`business_id`  bigint NOT NULL ,
`customer_id`  bigint NOT NULL ,
PRIMARY KEY (`business_id`, `customer_id`),
CONSTRAINT `t_meta_enterprise_to_customer_fk1` FOREIGN KEY (`business_id`) REFERENCES `business_unit` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
CONSTRAINT `t_meta_enterprise_to_customer_fk2` FOREIGN KEY (`customer_id`) REFERENCES `business_unit` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
)
;

