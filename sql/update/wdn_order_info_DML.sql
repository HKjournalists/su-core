DROP TABLE IF EXISTS `wdn_order_info`;
CREATE TABLE `wdn_order_info` (
	`id` bigint(10) NOT NULL AUTO_INCREMENT,
	`orderId` varchar(255) NOT NULL,
	`title` varchar(255) NOT NULL,
	`issn` varchar(255) DEFAULT NULL,
	`authors` varchar(255) DEFAULT NULL,
	`year` varchar(255) DEFAULT NULL,
	`volume` varchar(255) DEFAULT NULL,
	`issue` varchar(255) DEFAULT NULL,
	`journal` varchar(255) DEFAULT NULL,
	`dataSource` varchar(255) DEFAULT NULL,
	`applyDate` date DEFAULT NULL,
	`userId` bigint(10) NOT NULL,	
	`realName` varchar(255) DEFAULT NULL,
	`phone` varchar(255) DEFAULT NULL,
	`email` varchar(255) DEFAULT NULL,
	`status` varchar(255) DEFAULT NULL,
	PRIMARY KEY(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;