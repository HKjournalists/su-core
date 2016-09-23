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
# Table structure for table fsn_display
#

DROP TABLE IF EXISTS `fsn_display`;
CREATE TABLE `fsn_display` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `usrType` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '用户类型：匿名用户、cookie用户，实名用户',
  `uriId` bigint(20) DEFAULT NULL COMMENT 'URI编号',
  `container` int(3) DEFAULT NULL COMMENT '显示槽位',
  `active` int(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='根据用户类型在页面显示对应的信息，包含推荐栏、广告栏、楼';
INSERT INTO `fsn_display` VALUES (1,'default',1,1,1);
INSERT INTO `fsn_display` VALUES (2,'default',2,2,1);
INSERT INTO `fsn_display` VALUES (3,'default',3,3,1);
INSERT INTO `fsn_display` VALUES (4,'default',4,4,1);
INSERT INTO `fsn_display` VALUES (5,'default',5,5,1);
INSERT INTO `fsn_display` VALUES (6,'default',6,6,1);
INSERT INTO `fsn_display` VALUES (7,'default',7,7,1);
INSERT INTO `fsn_display` VALUES (8,'default',8,8,1);
INSERT INTO `fsn_display` VALUES (9,'default',9,9,1);
INSERT INTO `fsn_display` VALUES (10,'default',10,10,1);
INSERT INTO `fsn_display` VALUES (11,'default',11,1,1);
INSERT INTO `fsn_display` VALUES (12,'default',12,3,1);
INSERT INTO `fsn_display` VALUES (13,'default',13,4,1);
INSERT INTO `fsn_display` VALUES (14,'default',14,5,1);
INSERT INTO `fsn_display` VALUES (15,'default',15,6,1);
INSERT INTO `fsn_display` VALUES (16,'default',16,1,1);
INSERT INTO `fsn_display` VALUES (17,'default',17,1,1);
INSERT INTO `fsn_display` VALUES (18,'default',18,2,1);
INSERT INTO `fsn_display` VALUES (19,'default',19,3,1);
INSERT INTO `fsn_display` VALUES (20,'default',20,4,1);
INSERT INTO `fsn_display` VALUES (21,'default',21,5,1);
INSERT INTO `fsn_display` VALUES (22,'default',22,6,1);
INSERT INTO `fsn_display` VALUES (23,'default',23,7,1);
INSERT INTO `fsn_display` VALUES (24,'default',24,8,1);
INSERT INTO `fsn_display` VALUES (25,'default',25,9,1);
INSERT INTO `fsn_display` VALUES (26,'default',26,10,1);
INSERT INTO `fsn_display` VALUES (27,'default',27,2,1);
/*!40000 ALTER TABLE `fsn_display` ENABLE KEYS */;
UNLOCK TABLES;

/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
