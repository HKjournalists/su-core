ALTER TABLE `product_certification`
ADD COLUMN `resource_id`  bigint(20) NULL AFTER `del`;

ALTER TABLE `product_certification` ADD CONSTRAINT `fk_resource_id_resourceId` FOREIGN KEY (`resource_id`) REFERENCES `t_test_resource` (`RESOURCE_ID`) ON DELETE RESTRICT ON UPDATE RESTRICT;