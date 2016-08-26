ALTER TABLE `test_cycle_detail`  RENAME TO `test_case`;

ALTER TABLE `product_instance`  
	DROP FOREIGN KEY `fk_product_instance_business_unit_test`,  
	DROP FOREIGN KEY `fk_product_instance_fda_test`,  
	DROP FOREIGN KEY `fk_product_instance_test_lab_test`;

ALTER TABLE `product_instance`  
	DROP COLUMN `business_unit_test_id`,  
	DROP COLUMN `fda_test_id`,  
	DROP COLUMN `test_lab_test_id`;
	
ALTER TABLE `test_lab_test`  ADD COLUMN `test_cycle_id` BIGINT(20) NULL DEFAULT NULL;
ALTER TABLE `test_lab_test`  ADD CONSTRAINT `fk_test_lab_test_test_cycle` FOREIGN KEY (`test_cycle_id`) REFERENCES `test_cycle` (`id`);
ALTER TABLE `test_lab_test`  DROP FOREIGN KEY `fk_test_lab_test_result`;
ALTER TABLE `test_lab_test`  DROP COLUMN `test_result_id`;

ALTER TABLE `fda_test`  ADD COLUMN `test_cycle_id` BIGINT(20) NULL DEFAULT NULL;
ALTER TABLE `fda_test`  ADD CONSTRAINT `fk_fda_test_test_cycle` FOREIGN KEY (`test_cycle_id`) REFERENCES `test_cycle` (`id`);
ALTER TABLE `fda_test`  DROP FOREIGN KEY `fk_fda_test_test_result`;
ALTER TABLE `fda_test`  DROP COLUMN `test_result_id`;

ALTER TABLE `business_unit_test`  ADD COLUMN `test_cycle_id` BIGINT(20) NULL DEFAULT NULL;
ALTER TABLE `business_unit_test`  ADD CONSTRAINT `fk_business_unit_test_test_cycle` FOREIGN KEY (`test_cycle_id`) REFERENCES `test_cycle` (`id`);
ALTER TABLE `business_unit_test`  DROP FOREIGN KEY `fk_business_unit_test_test_result`;
ALTER TABLE `business_unit_test`  DROP COLUMN `test_result_id`;

ALTER TABLE `test_case` 
DROP FOREIGN KEY `fk_test_cycle_detail_product_instance`, 
DROP FOREIGN KEY `fk_test_cycle_detail_test_cycle`;
ALTER TABLE `test_case`  
ADD CONSTRAINT `fk_test_case_product_instance` FOREIGN KEY (`product_instance_id`) REFERENCES `product_instance` (`id`),  
ADD CONSTRAINT `fk_test_case_test_cycle` FOREIGN KEY (`test_cycle_id`) REFERENCES `test_cycle` (`id`);

ALTER TABLE `test_result`  ADD COLUMN `test_case_id` BIGINT NULL AFTER `test_date`;
ALTER TABLE `test_result`  ADD CONSTRAINT `fk_test_result_test_case` FOREIGN KEY (`test_case_id`) REFERENCES `test_case` (`id`);

ALTER TABLE `test_result`  ADD COLUMN `tester_id` BIGINT NULL AFTER `test_case_id`,  
ADD COLUMN `tester_type` INT NULL AFTER `tester_id`;