ALTER TABLE `test_property`  ADD COLUMN `category` VARCHAR(20) NULL DEFAULT NULL;
ALTER TABLE `test_property`  DROP FOREIGN KEY `fk_test_property_test_property_category`;
ALTER TABLE `test_property`  DROP INDEX `fk_test_property_test_property_category`;
ALTER TABLE `test_property`  ADD UNIQUE INDEX `fk_test_property_test_property_category` (`category`);
ALTER TABLE `test_property`  DROP COLUMN `categroy`;
ALTER TABLE `test_property`  ADD CONSTRAINT `fk_test_property_test_property_category` FOREIGN KEY (`category`) REFERENCES `test_property_category` (`code`);
