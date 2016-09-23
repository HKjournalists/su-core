
SET FOREIGN_KEY_CHECKS=0;
DROP TABLE IF EXISTS `fda`;
CREATE TABLE `fda` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `address` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `code` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_fda_code` (`code`)
) ENGINE=InnoDB AUTO_INCREMENT=36 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;


DROP TABLE IF EXISTS `test_lab`;
CREATE TABLE `test_lab` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `address` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=41 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;