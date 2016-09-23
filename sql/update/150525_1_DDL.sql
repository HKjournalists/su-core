ALTER TABLE `businessunit_to_prolicinfo`
ADD COLUMN `qs_id`  bigint UNSIGNED NULL AFTER `business_id`;

ALTER TABLE `productionlicenseinfo_to_resource`
ADD COLUMN `qs_id`  bigint NULL FIRST ;
ALTER TABLE `productionlicenseinfo_to_resource`
ADD COLUMN `id`  bigint UNSIGNED NOT NULL AUTO_INCREMENT FIRST ,
DROP PRIMARY KEY,
ADD PRIMARY KEY (`id`);

ALTER TABLE `product_to_businessunit`
ADD COLUMN `qs_id`  bigint UNSIGNED NULL AFTER `PRODUCT_ID`;
ALTER TABLE `product_to_businessunit` DROP FOREIGN KEY `FK_qs_no_to_qs_no`;

ALTER TABLE `t_liutong_produce_qs`
ADD COLUMN `qs_id`  bigint UNSIGNED NULL AFTER `produce_bus_id`;

ALTER TABLE `production_license_info`
ADD COLUMN `id`  bigint NULL FIRST ;
ALTER TABLE `production_license_info`
MODIFY COLUMN `id`  bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT FIRST ,
DROP PRIMARY KEY,
ADD PRIMARY KEY (`id`);
