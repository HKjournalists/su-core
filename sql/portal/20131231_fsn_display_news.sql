# MySQL-Front 5.0  (Build 1.0)

/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE */;
/*!40101 SET SQL_MODE='' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES */;
/*!40103 SET SQL_NOTES='ON' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS */;
/*!40014 SET FOREIGN_KEY_CHECKS=0 */;


# Host: 192.168.1.208    Database: FSNPortal
# ------------------------------------------------------
# Server version 5.5.34-0ubuntu0.13.04.1

#
# Table structure for table fsn_display_news
#

DROP TABLE IF EXISTS `fsn_display_news`;
CREATE TABLE `fsn_display_news` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) CHARACTER SET utf8 DEFAULT NULL COMMENT '显示名称',
  `keyword` varchar(100) CHARACTER SET utf8 DEFAULT NULL COMMENT '搜索关键字',
  `position` int(1) DEFAULT NULL COMMENT '位置，0-不激活，1-上栏，2-下栏',
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
INSERT INTO `fsn_display_news` VALUES (1,'政务信息','贵州食安动态',1);
INSERT INTO `fsn_display_news` VALUES (2,'小知识库','冬季饮食小知识',2);
INSERT INTO `fsn_display_news` VALUES (3,'','',0);
INSERT INTO `fsn_display_news` VALUES (4,'','',0);
INSERT INTO `fsn_display_news` VALUES (5,'','',0);
INSERT INTO `fsn_display_news` VALUES (6,'','',0);
INSERT INTO `fsn_display_news` VALUES (7,'','',0);
INSERT INTO `fsn_display_news` VALUES (8,'','',0);
INSERT INTO `fsn_display_news` VALUES (9,'','',0);
INSERT INTO `fsn_display_news` VALUES (10,'','',0);
/*!40000 ALTER TABLE `fsn_display_news` ENABLE KEYS */;
UNLOCK TABLES;

/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
