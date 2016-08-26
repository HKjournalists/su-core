ALTER TABLE `product_certification`
ADD COLUMN `business_id`  bigint(20) NULL COMMENT '企业id' AFTER `resource_id`;

ALTER TABLE `product_certification` ADD CONSTRAINT `fk_busId` FOREIGN KEY (`business_id`) REFERENCES `business_unit` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT;