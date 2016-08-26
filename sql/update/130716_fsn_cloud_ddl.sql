ALTER TABLE `test_result`  DROP FOREIGN KEY `fk_test_result_product_instance`;
ALTER TABLE `test_result`  DROP COLUMN `product_instance_id`;
ALTER TABLE `product_instance`  ADD COLUMN `business_unit_test_id` BIGINT NULL DEFAULT NULL;
ALTER TABLE `product_instance`  ADD COLUMN `fda_test_id` BIGINT NULL DEFAULT NULL;
ALTER TABLE `product_instance`  ADD COLUMN `test_lab_test_id` BIGINT NULL DEFAULT NULL;
ALTER TABLE `product_instance`  ADD CONSTRAINT `fk_product_instance_business_unit_test` FOREIGN KEY (`business_unit_test_id`) REFERENCES `business_unit_test` (`id`);
ALTER TABLE `product_instance`  ADD CONSTRAINT `fk_product_instance_fda_test` FOREIGN KEY (`fda_test_id`) REFERENCES `fda_test` (`id`);
ALTER TABLE `product_instance`  ADD CONSTRAINT `fk_product_instance_test_lab_test` FOREIGN KEY (`test_lab_test_id`) REFERENCES `test_lab_test` (`id`);