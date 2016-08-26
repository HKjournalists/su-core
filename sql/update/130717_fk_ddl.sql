ALTER TABLE `test_property`  DROP FOREIGN KEY `fk_test_property_test_property_category`;
ALTER TABLE `test_property`  CHANGE COLUMN `category` `category` BIGINT(20) NULL DEFAULT NULL;
ALTER TABLE `test_property`  ADD CONSTRAINT `fk_test_property_test_property_category` FOREIGN KEY (`category`) REFERENCES `test_property_category` (`id`);

ALTER TABLE `business_unit`  DROP FOREIGN KEY `fk_business_unit_business_category`,  DROP FOREIGN KEY `fk_business_unit_business_type`;
ALTER TABLE `business_unit`  CHANGE COLUMN `type` `type` BIGINT(20) NULL DEFAULT NULL AFTER `website`,  CHANGE COLUMN `category` `category` BIGINT(20) NULL DEFAULT NULL AFTER `type`;
ALTER TABLE `business_unit`  ADD CONSTRAINT `fk_business_unit_business_category` FOREIGN KEY (`category`) REFERENCES `business_category` (`id`),  ADD CONSTRAINT `fk_business_unit_business_type` FOREIGN KEY (`type`) REFERENCES `business_type` (`id`);

