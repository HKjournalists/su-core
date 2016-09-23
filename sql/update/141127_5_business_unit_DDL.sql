ALTER TABLE `business_unit`
ADD COLUMN `area_id`  bigint(20) NULL AFTER `lims_busunit_id`,
ADD COLUMN `office_id`  bigint(20) NULL AFTER `area_id`;

ALTER TABLE `business_unit` ADD CONSTRAINT `fk_busId_to_area_id` FOREIGN KEY (`area_id`) REFERENCES `sys_area` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE `business_unit` ADD CONSTRAINT `fk_busId_to_officeId` FOREIGN KEY (`office_id`) REFERENCES `sys_office` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT;