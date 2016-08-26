ALTER TABLE `fda_test`  ADD COLUMN `comment` VARCHAR(500) NULL;
ALTER TABLE `fda_test`  DROP FOREIGN KEY `fk_fda_test_fda_test_plan`;
ALTER TABLE `fda_test`  DROP COLUMN `fda_test_plan_id`;