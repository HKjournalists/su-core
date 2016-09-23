ALTER TABLE `business_brand`
ADD COLUMN `brand_category_id`  int(10) NULL AFTER `last_modify_time`;

ALTER TABLE `business_brand` ADD CONSTRAINT `fk_brand_category_id` FOREIGN KEY (`brand_category_id`) REFERENCES `brand_category` (`ID`) ON DELETE RESTRICT ON UPDATE RESTRICT;