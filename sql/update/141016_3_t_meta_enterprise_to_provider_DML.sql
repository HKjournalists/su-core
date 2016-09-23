CREATE TABLE `t_meta_enterprise_to_provider` (
`business_id`  bigint NOT NULL ,
`provider_id`  bigint NOT NULL ,
PRIMARY KEY (`business_id`, `provider_id`),
CONSTRAINT `t_meta_enterprise_to_provider_fk1` FOREIGN KEY (`business_id`) REFERENCES `business_unit` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
CONSTRAINT `t_meta_enterprise_to_provider_fk2` FOREIGN KEY (`provider_id`) REFERENCES `business_unit` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
)
;