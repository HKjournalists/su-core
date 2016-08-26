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
# Table structure for table fsn_user
#

DROP TABLE IF EXISTS `fsn_user`;
CREATE TABLE `fsn_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `usr_name` varchar(20) CHARACTER SET utf8 NOT NULL DEFAULT '' COMMENT '登录名',
  `usr_pwd` varchar(20) CHARACTER SET utf8 NOT NULL DEFAULT '' COMMENT '登录密码',
  `usr_type` varchar(20) CHARACTER SET utf8 NOT NULL DEFAULT 'default' COMMENT '用户类型：匿名用户、cookie用户，实名用户',
  `usr_email` varchar(100) CHARACTER SET utf8 NOT NULL DEFAULT '' COMMENT '电子邮件',
  `usr_gender` int(1) DEFAULT NULL COMMENT '0-男 1-女',
  `usr_birth` date DEFAULT NULL COMMENT '出生年',
  `usr_birthplace` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '出生地',
  `usr_occupation` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '职业',
  `usr_income` varchar(50) CHARACTER SET utf8 DEFAULT NULL COMMENT '收入',
  `usr_sta` int(1) NOT NULL DEFAULT '0' COMMENT '用户状态: 0-未激活,1-激活,2-高级用户审核被退回',
  `usr_key` varchar(15) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '验证码（账号激活码）',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
INSERT INTO `fsn_user` VALUES (0,'abc','123456','default','qq@126.com',1,'2013-01-01','1ç1å¸',NULL,'1',1,'1384396392379');
INSERT INTO `fsn_user` VALUES (1,'test','test','default','test@test.test',0,NULL,NULL,NULL,NULL,0,'0');
INSERT INTO `fsn_user` VALUES (2,'tuyx','123456','default','qq@qq.com',NULL,NULL,NULL,NULL,NULL,1,'123465789945');
INSERT INTO `fsn_user` VALUES (4,'koo','123456','default','gcx-001@163.com',NULL,NULL,NULL,NULL,NULL,1,'1384307364198');
INSERT INTO `fsn_user` VALUES (5,'aaa','123456','default','abc@11.com',0,'2013-01-01','zhejiang','aa','100',0,'1384395347865');
INSERT INTO `fsn_user` VALUES (6,'qwert','123456','default','jeiotj@126.com',0,'2013-01-01','1省1市',NULL,'1',0,'1384395909090');
INSERT INTO `fsn_user` VALUES (8,'aeg','111111','default','gaeaew',0,'2013-01-01','1ç??1å¸?','','1',0,'1384516163594');
INSERT INTO `fsn_user` VALUES (9,'123123','3213','default','21321',1,'2011-02-01','1ç??1å¸?','321321321','2',0,'1384751833378');
INSERT INTO `fsn_user` VALUES (10,'','','default','',0,'2013-01-01','1ç??1å¸?','','1',0,'1384752067281');
/*!40000 ALTER TABLE `fsn_user` ENABLE KEYS */;
UNLOCK TABLES;

/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
