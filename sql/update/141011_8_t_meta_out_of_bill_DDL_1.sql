ALTER TABLE `t_meta_out_of_bill` ADD CONSTRAINT `t_meta_out_of_bill_fk_1` FOREIGN KEY (`CUSTOMER_NO`) REFERENCES `business_unit` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT;

