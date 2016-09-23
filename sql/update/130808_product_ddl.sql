ALTER TABLE `product`  ADD COLUMN `producer_id` BIGINT NULL;
ALTER TABLE `product`  ADD CONSTRAINT `fk_product_business_unit` FOREIGN KEY (`producer_id`) REFERENCES `business_unit` (`id`);