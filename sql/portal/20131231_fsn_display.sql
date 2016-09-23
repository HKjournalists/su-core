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
# Table structure for table fsn_display
#

DROP TABLE IF EXISTS `fsn_display`;
CREATE TABLE `fsn_display` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `usrType` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '用户类型：匿名用户、cookie用户，实名用户',
  `uriId` bigint(20) DEFAULT NULL COMMENT 'URI编号',
  `container` int(3) DEFAULT NULL COMMENT '显示槽位',
  `active` int(1) NOT NULL DEFAULT '0' COMMENT '激活',
  `pid` bigint(20) DEFAULT NULL COMMENT '父ID',
  `type` int(1) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=71 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='根据用户类型在页面显示对应的信息，包含推荐栏、广告栏、楼';
INSERT INTO `fsn_display` VALUES (1,'default',18,1,1,NULL,4);
INSERT INTO `fsn_display` VALUES (2,'default',19,2,1,NULL,4);
INSERT INTO `fsn_display` VALUES (3,'default',20,3,1,NULL,4);
INSERT INTO `fsn_display` VALUES (4,'default',NULL,4,0,NULL,4);
INSERT INTO `fsn_display` VALUES (5,'default',NULL,5,0,NULL,4);
INSERT INTO `fsn_display` VALUES (6,'default',22,6,1,NULL,4);
INSERT INTO `fsn_display` VALUES (7,'default',23,7,0,NULL,4);
INSERT INTO `fsn_display` VALUES (8,'default',24,8,0,NULL,4);
INSERT INTO `fsn_display` VALUES (9,'default',25,9,0,NULL,4);
INSERT INTO `fsn_display` VALUES (10,'default',26,10,0,NULL,4);
INSERT INTO `fsn_display` VALUES (11,'default',36,1,1,NULL,3);
INSERT INTO `fsn_display` VALUES (12,'default',16,2,1,NULL,3);
INSERT INTO `fsn_display` VALUES (13,'default',45,3,1,NULL,3);
INSERT INTO `fsn_display` VALUES (14,'default',54,4,0,NULL,3);
INSERT INTO `fsn_display` VALUES (15,'default',115,5,1,NULL,3);
INSERT INTO `fsn_display` VALUES (16,'default',94,6,0,NULL,3);
INSERT INTO `fsn_display` VALUES (17,'default',NULL,7,0,NULL,3);
INSERT INTO `fsn_display` VALUES (18,'default',NULL,8,0,NULL,3);
INSERT INTO `fsn_display` VALUES (19,'default',NULL,9,0,NULL,3);
INSERT INTO `fsn_display` VALUES (20,'default',NULL,10,0,NULL,3);
INSERT INTO `fsn_display` VALUES (21,'default',28,1,1,11,1);
INSERT INTO `fsn_display` VALUES (22,'default',29,2,1,11,1);
INSERT INTO `fsn_display` VALUES (23,'default',30,3,1,11,1);
INSERT INTO `fsn_display` VALUES (24,'default',31,4,1,11,1);
INSERT INTO `fsn_display` VALUES (25,'default',32,5,1,11,1);
INSERT INTO `fsn_display` VALUES (26,'default',1,1,1,12,1);
INSERT INTO `fsn_display` VALUES (27,'default',2,2,1,12,1);
INSERT INTO `fsn_display` VALUES (28,'default',3,3,1,12,1);
INSERT INTO `fsn_display` VALUES (29,'default',4,4,1,12,1);
INSERT INTO `fsn_display` VALUES (30,'default',5,5,1,12,1);
INSERT INTO `fsn_display` VALUES (31,'default',37,1,1,13,1);
INSERT INTO `fsn_display` VALUES (32,'default',38,2,1,13,1);
INSERT INTO `fsn_display` VALUES (33,'default',39,3,1,13,1);
INSERT INTO `fsn_display` VALUES (34,'default',40,4,1,13,1);
INSERT INTO `fsn_display` VALUES (35,'default',41,5,1,13,1);
INSERT INTO `fsn_display` VALUES (36,'default',46,1,1,14,1);
INSERT INTO `fsn_display` VALUES (37,'default',47,2,1,14,1);
INSERT INTO `fsn_display` VALUES (38,'default',48,3,1,14,1);
INSERT INTO `fsn_display` VALUES (39,'default',49,4,1,14,1);
INSERT INTO `fsn_display` VALUES (40,'default',50,5,1,14,1);
INSERT INTO `fsn_display` VALUES (41,'default',116,1,1,15,1);
INSERT INTO `fsn_display` VALUES (42,'default',117,2,1,15,1);
INSERT INTO `fsn_display` VALUES (43,'default',118,3,1,15,1);
INSERT INTO `fsn_display` VALUES (44,'default',119,4,1,15,1);
INSERT INTO `fsn_display` VALUES (45,'default',120,5,1,15,1);
INSERT INTO `fsn_display` VALUES (46,'default',95,1,1,16,1);
INSERT INTO `fsn_display` VALUES (47,'default',96,2,1,16,1);
INSERT INTO `fsn_display` VALUES (48,'default',97,3,1,16,1);
INSERT INTO `fsn_display` VALUES (49,'default',98,4,1,16,1);
INSERT INTO `fsn_display` VALUES (50,'default',99,5,1,16,1);
INSERT INTO `fsn_display` VALUES (51,'default',NULL,1,1,17,1);
INSERT INTO `fsn_display` VALUES (52,'default',NULL,2,1,17,1);
INSERT INTO `fsn_display` VALUES (53,'default',NULL,3,1,17,1);
INSERT INTO `fsn_display` VALUES (54,'default',NULL,4,1,17,1);
INSERT INTO `fsn_display` VALUES (55,'default',NULL,5,1,17,1);
INSERT INTO `fsn_display` VALUES (56,'default',NULL,1,1,18,1);
INSERT INTO `fsn_display` VALUES (57,'default',NULL,2,1,18,1);
INSERT INTO `fsn_display` VALUES (58,'default',NULL,3,1,18,1);
INSERT INTO `fsn_display` VALUES (59,'default',NULL,4,1,18,1);
INSERT INTO `fsn_display` VALUES (60,'default',NULL,5,1,18,1);
INSERT INTO `fsn_display` VALUES (61,'default',NULL,1,1,19,1);
INSERT INTO `fsn_display` VALUES (62,'default',NULL,2,1,19,1);
INSERT INTO `fsn_display` VALUES (63,'default',NULL,3,1,19,1);
INSERT INTO `fsn_display` VALUES (64,'default',NULL,4,1,19,1);
INSERT INTO `fsn_display` VALUES (65,'default',NULL,5,1,19,1);
INSERT INTO `fsn_display` VALUES (66,'default',NULL,1,1,20,1);
INSERT INTO `fsn_display` VALUES (67,'default',NULL,2,1,20,1);
INSERT INTO `fsn_display` VALUES (68,'default',NULL,3,1,20,1);
INSERT INTO `fsn_display` VALUES (69,'default',NULL,4,1,20,1);
INSERT INTO `fsn_display` VALUES (70,'default',NULL,5,1,20,1);
/*!40000 ALTER TABLE `fsn_display` ENABLE KEYS */;
UNLOCK TABLES;

/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
