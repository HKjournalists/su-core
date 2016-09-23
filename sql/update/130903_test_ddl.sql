ALTER TABLE `test_result`  DROP INDEX `fk_test_result_test_case`;
ALTER TABLE `test_result`  DROP COLUMN `test_case_id`;
ALTER TABLE `test_result`  ADD COLUMN `fda_test_plan_id` BIGINT NULL DEFAULT NULL AFTER `tester_type`,  
	ADD COLUMN `testee_id` BIGINT NULL DEFAULT NULL AFTER `fda_test_plan_id`,  
	ADD COLUMN `sample_id` BIGINT NULL DEFAULT NULL AFTER `testee_id`,  
	ADD COLUMN `sample_quantity` VARCHAR(20) NULL DEFAULT NULL AFTER `sample_id`,  
	ADD COLUMN `sampling_location` VARCHAR(200) NULL DEFAULT NULL AFTER `sample_quantity`,  
	ADD COLUMN `sampling_date` DATETIME NULL DEFAULT NULL AFTER `sampling_location`,  
	ADD COLUMN `test_type` VARCHAR(200) NULL DEFAULT NULL AFTER `sampling_date`,  
	ADD COLUMN `equipment` VARCHAR(200) NULL DEFAULT NULL AFTER `test_type`,  
	ADD COLUMN `standard` VARCHAR(200) NULL DEFAULT NULL AFTER `equipment`,  
	ADD COLUMN `result` VARCHAR(2000) NULL DEFAULT NULL AFTER `standard`,  
	ADD COLUMN `approve_by` VARCHAR(20) NULL DEFAULT NULL AFTER `result`,  
	ADD COLUMN `audit_by` VARCHAR(20) NULL DEFAULT NULL AFTER `approve_by`,  
	ADD COLUMN `key_tester` VARCHAR(20) NULL DEFAULT NULL AFTER `audit_by`,  
	ADD COLUMN `pdf_report` VARCHAR(1024) NULL DEFAULT NULL AFTER `key_tester`;
ALTER TABLE `test_result`  ADD CONSTRAINT `fk_test_result_fda_test_plan` FOREIGN KEY (`fda_test_plan_id`) REFERENCES `fda_test_plan` (`id`);
ALTER TABLE `test_result`  ADD CONSTRAINT `fk_test_result_business_unit` FOREIGN KEY (`testee_id`) REFERENCES `business_unit` (`id`);
ALTER TABLE `test_result`  ADD CONSTRAINT `fk_test_result_product_instance` FOREIGN KEY (`sample_id`) REFERENCES `product_instance` (`id`);

ALTER TABLE `test_property`  CHANGE COLUMN `value` `result` VARCHAR(50) NULL DEFAULT NULL AFTER `name`;
ALTER TABLE `test_property`  ADD COLUMN `unit` VARCHAR(20) NULL DEFAULT NULL AFTER `result`,  
	ADD COLUMN `tech_indicator` VARCHAR(50) NULL DEFAULT NULL AFTER `unit`,  
	ADD COLUMN `assessment` VARCHAR(50) NULL DEFAULT NULL AFTER `tech_indicator`,  
	ADD COLUMN `standard` VARCHAR(50) NULL DEFAULT NULL AFTER `assessment`;

ALTER TABLE `fda_test_plan`  ADD COLUMN `plan_no` VARCHAR(200) NULL AFTER `fda_id`;

ALTER TABLE `test_result`  ADD COLUMN `brand_id` BIGINT(20) NULL DEFAULT NULL AFTER `fda_test_plan_id`;
ALTER TABLE `test_result`  ADD CONSTRAINT `fk_test_result_business_brand` FOREIGN KEY (`brand_id`) REFERENCES `business_brand` (`id`);

ALTER TABLE `test_result`  ADD COLUMN `test_lab_id` BIGINT NULL AFTER `pdf_report`;
ALTER TABLE `test_result`  ADD CONSTRAINT `fk_test_result_test_lab` FOREIGN KEY (`test_lab_id`) REFERENCES `test_lab` (`id`);

ALTER TABLE `fda_test_plan`  CHANGE COLUMN `plan_no` `plan_no` VARCHAR(200) NOT NULL AFTER `fda_id`,  ADD UNIQUE INDEX `plan_no` (`plan_no`);