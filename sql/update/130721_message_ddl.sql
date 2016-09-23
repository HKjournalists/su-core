CREATE TABLE `message` (  
	`id` BIGINT NOT NULL AUTO_INCREMENT,  
	`content` VARCHAR(200) NULL,  
	`type` INT NULL,  
	`sender_id` BIGINT NULL,  
	`sender_type` INT NULL,  
	`create_date` DATETIME NULL,  
	PRIMARY KEY (`id`) 
) COLLATE='utf8_general_ci' ENGINE=InnoDB ROW_FORMAT=DEFAULT;