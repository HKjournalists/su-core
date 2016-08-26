CREATE TABLE `test_cycle` (  
	`id` BIGINT NOT NULL AUTO_INCREMENT,  
	`name` VARCHAR(50) NOT NULL,  
	`start_date` DATETIME NOT NULL,  
	`end_date` DATETIME NOT NULL,  
	`create_date` DATETIME NOT NULL,  
	PRIMARY KEY (`id`) 
	) COLLATE='utf8_general_ci' ENGINE=InnoDB ROW_FORMAT=DEFAULT;
	
CREATE TABLE `test_cycle_detail` (  
	`id` BIGINT NOT NULL AUTO_INCREMENT,  
	`test_cycle_id` BIGINT NULL ,  
	`product_instance_id` BIGINT NULL,  
	`status` INT NULL,  
	PRIMARY KEY (`id`),  
	CONSTRAINT `fk_test_cycle_detail_test_cycle` FOREIGN KEY (`test_cycle_id`) REFERENCES `test_cycle` (`id`),  
	CONSTRAINT `fk_test_cycle_detail_product_instance` FOREIGN KEY (`product_instance_id`) REFERENCES `product_instance` (`id`) 
	) COLLATE='utf8_general_ci' ENGINE=InnoDB ROW_FORMAT=DEFAULT;