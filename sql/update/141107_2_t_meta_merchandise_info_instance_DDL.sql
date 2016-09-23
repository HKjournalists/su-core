ALTER TABLE `t_meta_merchandise_info_instance` DROP FOREIGN KEY `t_meta_merchandise_info_instance_ibfk_1`;

ALTER TABLE `t_meta_merchandise_info_instance` ADD CONSTRAINT `t_meta_merchandise_info_instance_fk1` FOREIGN KEY (`product_id`) REFERENCES `t_meta_initialize_product` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

