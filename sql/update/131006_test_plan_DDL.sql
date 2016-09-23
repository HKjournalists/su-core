ALTER TABLE `fda_test_plan`
	ADD COLUMN `sampling_district` VARCHAR(50) NULL AFTER `plan_no`,
	ADD COLUMN `sampling_quantity` INT NULL AFTER `sampling_district`,
	ADD COLUMN `quarter` VARCHAR(10) NULL AFTER `sampling_quantity`,
	ADD COLUMN `create_date` DATETIME NULL AFTER `quarter`,
	ADD COLUMN `testee_id` BIGINT NULL AFTER `create_date`,
	ADD COLUMN `producer_id` BIGINT NULL AFTER `testee_id`,
	ADD COLUMN `sample_id` BIGINT NULL AFTER `producer_id`;
	
ALTER TABLE `fda_test_plan`
	ADD CONSTRAINT `fk_fda_test_plan_business_unit_testee` FOREIGN KEY (`testee_id`) REFERENCES `business_unit` (`id`),
	ADD CONSTRAINT `fk_fda_test_plan_business_unit_producer` FOREIGN KEY (`producer_id`) REFERENCES `business_unit` (`id`),
	ADD CONSTRAINT `fk_fda_test_plan_product_instnace` FOREIGN KEY (`sample_id`) REFERENCES `product_instance` (`id`);
	
ALTER TABLE `business_unit`
	ADD COLUMN `license_no` VARCHAR(50) NULL AFTER `category`,
	ADD COLUMN `contact` VARCHAR(50) NULL AFTER `license_no`,
	ADD COLUMN `note` VARCHAR(200) NULL AFTER `contact`;