ALTER TABLE `business_brand`
DROP INDEX `idx_name` ,
ADD INDEX `idx_name` (`name`) USING BTREE ;