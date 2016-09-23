ALTER TABLE `production_license_info`
ADD COLUMN `qsformat_id`  int(5) NULL AFTER `check_type`;

ALTER TABLE `production_license_info` ADD CONSTRAINT `FK_qsformat_id` FOREIGN KEY (`qsformat_id`) REFERENCES `licenceno_format` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT;

