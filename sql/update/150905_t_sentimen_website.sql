

SET FOREIGN_KEY_CHECKS=0;
DROP TABLE IF EXISTS `t_sentimen_website`;
CREATE TABLE `t_sentimen_website` (
  `website_name` varchar(255) NOT NULL,
  `website_url` varchar(255) NOT NULL,
  `organizationId` bigint(20) NOT NULL,
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `organizationName` varchar(255) DEFAULT NULL,
  `status` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8;