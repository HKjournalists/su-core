ALTER TABLE `business_unit` DROP FOREIGN KEY `business_unit_fk1`;
ALTER TABLE `business_unit` DROP FOREIGN KEY `business_unit_fk2`;
ALTER TABLE `business_unit`
DROP COLUMN `PROVIDER_TYPE_ID`,
DROP COLUMN `CUSTOMER_TYPE_ID`;
drop table `t_meta_provider_type`;
drop table `t_meta_customer_type`;