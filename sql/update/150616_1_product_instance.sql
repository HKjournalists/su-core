ALTER TABLE `product_instance`
ADD COLUMN `qs_no`  varchar(50) NOT NULL DEFAULT '' AFTER `producer_id`;