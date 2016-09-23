
ALTER TABLE `test_result` ADD COLUMN `edition` varchar(20) DEFAULT NULL;
ALTER TABLE `test_result` ADD COLUMN `dbflag` varchar(20) DEFAULT NULL;
ALTER TABLE `test_result` ADD COLUMN `organization` bigint DEFAULT NULL;



ALTER TABLE `product` ADD COLUMN `organization` bigint DEFAULT NULL;