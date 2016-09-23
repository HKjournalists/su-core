ALTER TABLE `product_to_businessunit` ADD CONSTRAINT `FK_qs_no_to_qs_no` FOREIGN KEY (`QS_NO`) REFERENCES `production_license_info` (`qs_no`) ON DELETE RESTRICT ON UPDATE RESTRICT;
ALTER TABLE `product_to_businessunit` ADD CONSTRAINT `FK_businessunit_id_to_busid` FOREIGN KEY (`business_id`) REFERENCES `business_unit` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT;
