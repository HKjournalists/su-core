# MySQL-Front 5.0  (Build 1.0)

/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE */;
/*!40101 SET SQL_MODE='' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES */;
/*!40103 SET SQL_NOTES='ON' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS */;
/*!40014 SET FOREIGN_KEY_CHECKS=0 */;


# Host: 192.168.1.208    Database: fsn_cloud
# ------------------------------------------------------
# Server version 5.5.34-0ubuntu0.13.04.1

USE `fsn_cloud`;

#
# Table structure for table fsn_comment
#

DROP TABLE IF EXISTS `fsn_comment`;
CREATE TABLE `fsn_comment` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `product_id` bigint(20) NOT NULL DEFAULT '0',
  `user_id` bigint(20) NOT NULL DEFAULT '0',
  `star` int(1) NOT NULL DEFAULT '0',
  `content` varchar(255) COLLATE utf8_unicode_ci NOT NULL DEFAULT '',
  `method` varchar(20) COLLATE utf8_unicode_ci NOT NULL DEFAULT '',
  `method_detail` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `time` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  `product_detail` varchar(50) CHARACTER SET utf8 DEFAULT '',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
INSERT INTO `fsn_comment` VALUES (1,1,0,1,'111','111','111','2013-11-12 17:01:00','11');
INSERT INTO `fsn_comment` VALUES (2,1,0,2,'22','222','22','2013-10-20','22');
INSERT INTO `fsn_comment` VALUES (3,1,0,3,'333','333','333','2012-12-20','33');
INSERT INTO `fsn_comment` VALUES (4,1,1,5,'44','444','444','2013-11-12 17:35:42','44');
INSERT INTO `fsn_comment` VALUES (5,1,1,3,'中文测试','中文测试','中文测试','2013-11-15 19:41:08','');
INSERT INTO `fsn_comment` VALUES (6,1,2,3,'中文测试','中文测试','123','2013-11-15 20:10:23','30');
INSERT INTO `fsn_comment` VALUES (7,1,2,3,'中文测试','中文测试','123','2013-11-15 20:10:38','30');
INSERT INTO `fsn_comment` VALUES (8,1,2,3,'中文测试','中文测试','123','2013-11-15 20:10:43','30');
INSERT INTO `fsn_comment` VALUES (9,1,2,3,'中文测试','中文测试','123','2013-11-15 20:22:20','35');
INSERT INTO `fsn_comment` VALUES (10,1,2,4,'中文测试','中文测试','123','2013-11-15 20:23:08','35');
/*!40000 ALTER TABLE `fsn_comment` ENABLE KEYS */;
UNLOCK TABLES;

/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
