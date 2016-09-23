CREATE TABLE `product_poll` (  
	`id` BIGINT NOT NULL AUTO_INCREMENT,
	`product_instance_id` BIGINT NULL,  
	`user_id` BIGINT NULL,  
	`like_` TINYINT NULL,  
	`rate` INT NULL,  
	`poll_date` DATETIME NULL,
	`comment` VARCHAR(200) NULL,
	PRIMARY KEY (id),
	CONSTRAINT `fk_product_poll_product_instance` FOREIGN KEY (`product_instance_id`) 
	REFERENCES `product_instance` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
	) COLLATE='utf8_general_ci' ENGINE=InnoDB ROW_FORMAT=DEFAULT;