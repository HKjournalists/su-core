ALTER TABLE `fda`  DROP FOREIGN KEY `fk_fda_fda`;
ALTER TABLE `fda`  DROP COLUMN `supervisor`;
ALTER TABLE `fda`  ADD COLUMN `code` VARCHAR(200) NULL DEFAULT NULL;
ALTER TABLE `fda`  ADD UNIQUE INDEX `idx_fda_code` (`code`);