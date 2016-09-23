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
# Table structure for table certification
#

DROP TABLE IF EXISTS `certification`;
CREATE TABLE `certification` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `imgurl` varchar(255) CHARACTER SET utf8 DEFAULT NULL COMMENT '认证的图片url',
  `message` varchar(255) CHARACTER SET utf8 DEFAULT NULL COMMENT '认证信息简介',
  `name` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '认证名称',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
INSERT INTO `certification` VALUES (1,NULL,NULL,'3C认证');
INSERT INTO `certification` VALUES (2,NULL,NULL,'Ⅲ型环境标志');
INSERT INTO `certification` VALUES (3,NULL,NULL,'CE');
INSERT INTO `certification` VALUES (4,NULL,NULL,'FCC');
INSERT INTO `certification` VALUES (5,NULL,NULL,'ISO9001 2000');
INSERT INTO `certification` VALUES (6,NULL,NULL,'ISO9001');
INSERT INTO `certification` VALUES (7,NULL,NULL,'ISO14000体系认证');
INSERT INTO `certification` VALUES (8,NULL,NULL,'ISO22000食品安全管理体系');
INSERT INTO `certification` VALUES (9,NULL,NULL,'保健食品');
INSERT INTO `certification` VALUES (10,NULL,NULL,'国家免检产品');
INSERT INTO `certification` VALUES (11,NULL,NULL,'华夏认证');
INSERT INTO `certification` VALUES (12,NULL,NULL,'绿色食品');
INSERT INTO `certification` VALUES (13,NULL,NULL,'生产许可');
INSERT INTO `certification` VALUES (14,NULL,NULL,'速冻食品');
INSERT INTO `certification` VALUES (15,NULL,NULL,'无公害农产品');
INSERT INTO `certification` VALUES (16,NULL,NULL,'无公害农产品 旧');
INSERT INTO `certification` VALUES (17,NULL,NULL,'循环再生标志');
INSERT INTO `certification` VALUES (18,NULL,NULL,'有机食品');
INSERT INTO `certification` VALUES (19,NULL,NULL,'中国环境标志');
INSERT INTO `certification` VALUES (20,NULL,NULL,'中国环境管理体系认证机构国家认可');
INSERT INTO `certification` VALUES (21,NULL,NULL,'中国名牌');
INSERT INTO `certification` VALUES (22,NULL,NULL,'中华人民共和国原产地域产品');
INSERT INTO `certification` VALUES (23,NULL,NULL,'unknow');
/*!40000 ALTER TABLE `certification` ENABLE KEYS */;
UNLOCK TABLES;

/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
