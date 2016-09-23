ALTER TABLE `business_unit`
ADD COLUMN `type_id`  int(11) NULL COMMENT '客户或者供货商类型' AFTER `CUSTOMER_OR_PROVIDER_TYPE`;
ALTER TABLE `business_unit` ADD CONSTRAINT `business_unit_fk1` FOREIGN KEY (`type_id`) REFERENCES `t_meta_customer_and_provider_type` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION;