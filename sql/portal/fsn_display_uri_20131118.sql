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
# Table structure for table fsn_display_uri
#

DROP TABLE IF EXISTS `fsn_display_uri`;
CREATE TABLE `fsn_display_uri` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `type` int(1) NOT NULL DEFAULT '0' COMMENT '资源类型 1-产品 2-广告 3-栏目 4-推荐 5-新闻',
  `content` text COLLATE utf8_unicode_ci COMMENT '资源内容',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='显示资源存储表';
INSERT INTO `fsn_display_uri` VALUES (1,1,'<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><prod><prodId>0</prodId><prodName>prodName:0</prodName><prodDesc>prodDesc:0</prodDesc><prodPic>prodPic:0</prodPic><prodUrl>prodUrl:0</prodUrl><colCode>0001</colCode><active>1</active><container>0</container></prod>');
INSERT INTO `fsn_display_uri` VALUES (2,1,'<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><prod><prodId>1</prodId><prodName>prodName:1</prodName><prodDesc>prodDesc:1</prodDesc><prodPic>prodPic:1</prodPic><prodUrl>prodUrl:1</prodUrl><colCode>0001</colCode><active>1</active><container>1</container></prod>');
INSERT INTO `fsn_display_uri` VALUES (3,1,'<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><prod><prodId>2</prodId><prodName>prodName:2</prodName><prodDesc>prodDesc:2</prodDesc><prodPic>prodPic:2</prodPic><prodUrl>prodUrl:2</prodUrl><colCode>0001</colCode><active>1</active><container>2</container></prod>');
INSERT INTO `fsn_display_uri` VALUES (4,1,'<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><prod><prodId>3</prodId><prodName>prodName:3</prodName><prodDesc>prodDesc:3</prodDesc><prodPic>prodPic:3</prodPic><prodUrl>prodUrl:3</prodUrl><colCode>0001</colCode><active>1</active><container>3</container></prod>');
INSERT INTO `fsn_display_uri` VALUES (5,1,'<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><prod><prodId>4</prodId><prodName>prodName:4</prodName><prodDesc>prodDesc:4</prodDesc><prodPic>prodPic:4</prodPic><prodUrl>prodUrl:4</prodUrl><colCode>0001</colCode><active>1</active><container>4</container></prod>');
INSERT INTO `fsn_display_uri` VALUES (6,1,'<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><prod><prodId>5</prodId><prodName>prodName:5</prodName><prodDesc>prodDesc:5</prodDesc><prodPic>prodPic:5</prodPic><prodUrl>prodUrl:5</prodUrl><colCode>0001</colCode><active>1</active><container>5</container></prod>');
INSERT INTO `fsn_display_uri` VALUES (7,1,'<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><prod><prodId>6</prodId><prodName>prodName:6</prodName><prodDesc>prodDesc:6</prodDesc><prodPic>prodPic:6</prodPic><prodUrl>prodUrl:6</prodUrl><colCode>0001</colCode><active>1</active><container>6</container></prod>');
INSERT INTO `fsn_display_uri` VALUES (8,1,'<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><prod><prodId>7</prodId><prodName>prodName:7</prodName><prodDesc>prodDesc:7</prodDesc><prodPic>prodPic:7</prodPic><prodUrl>prodUrl:7</prodUrl><colCode>0001</colCode><active>1</active><container>7</container></prod>');
INSERT INTO `fsn_display_uri` VALUES (9,1,'<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><prod><prodId>8</prodId><prodName>prodName:8</prodName><prodDesc>prodDesc:8</prodDesc><prodPic>prodPic:8</prodPic><prodUrl>prodUrl:8</prodUrl><colCode>0001</colCode><active>1</active><container>8</container></prod>');
INSERT INTO `fsn_display_uri` VALUES (10,1,'<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><prod><prodId>9</prodId><prodName>prodName:9</prodName><prodDesc>prodDesc:9</prodDesc><prodPic>prodPic:9</prodPic><prodUrl>prodUrl:9</prodUrl><colCode>0001</colCode><active>1</active><container>9</container></prod>');
INSERT INTO `fsn_display_uri` VALUES (11,2,'<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><ad><adPic>adPic:0</adPic><adUrl>adUrl:0</adUrl><colCode>0001</colCode><active>1</active><container>0</container></ad>');
INSERT INTO `fsn_display_uri` VALUES (12,2,'<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><ad><adPic>adPic:2</adPic><adUrl>adUrl:2</adUrl><colCode></colCode><active>1</active><container>2</container></ad>');
INSERT INTO `fsn_display_uri` VALUES (13,2,'<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><ad><adPic>adPic:3</adPic><adUrl>adUrl:3</adUrl><colCode></colCode><active>1</active><container>3</container></ad>');
INSERT INTO `fsn_display_uri` VALUES (14,2,'<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><ad><adPic>adPic:4</adPic><adUrl>adUrl:4</adUrl><colCode></colCode><active>1</active><container>4</container></ad>');
INSERT INTO `fsn_display_uri` VALUES (15,2,'<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><ad><adPic>adPic:5</adPic><adUrl>adUrl:5</adUrl><colCode></colCode><active>1</active><container>5</container></ad>');
INSERT INTO `fsn_display_uri` VALUES (16,3,'<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><col><colName>colName:0</colName><colCode>0001</colCode><active>1</active><container>0</container></col>');
INSERT INTO `fsn_display_uri` VALUES (17,4,'<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><re><rePic>rePic:0</rePic><reUrl>reUrl:0</reUrl><active>1</active><container>0</container></re>');
INSERT INTO `fsn_display_uri` VALUES (18,4,'<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><re><rePic>rePic:1</rePic><reUrl>reUrl:1</reUrl><active>1</active><container>1</container></re>');
INSERT INTO `fsn_display_uri` VALUES (19,4,'<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><re><rePic>rePic:2</rePic><reUrl>reUrl:2</reUrl><active>1</active><container>2</container></re>');
INSERT INTO `fsn_display_uri` VALUES (20,4,'<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><re><rePic>rePic:3</rePic><reUrl>reUrl:3</reUrl><active>1</active><container>3</container></re>');
INSERT INTO `fsn_display_uri` VALUES (21,4,'<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><re><rePic>rePic:4</rePic><reUrl>reUrl:4</reUrl><active>1</active><container>4</container></re>');
INSERT INTO `fsn_display_uri` VALUES (22,4,'<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><re><rePic>rePic:5</rePic><reUrl>reUrl:5</reUrl><active>1</active><container>5</container></re>');
INSERT INTO `fsn_display_uri` VALUES (23,4,'<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><re><rePic>rePic:6</rePic><reUrl>reUrl:6</reUrl><active>1</active><container>6</container></re>');
INSERT INTO `fsn_display_uri` VALUES (24,4,'<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><re><rePic>rePic:7</rePic><reUrl>reUrl:7</reUrl><active>1</active><container>7</container></re>');
INSERT INTO `fsn_display_uri` VALUES (25,4,'<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><re><rePic>rePic:8</rePic><reUrl>reUrl:8</reUrl><active>1</active><container>8</container></re>');
INSERT INTO `fsn_display_uri` VALUES (26,4,'<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><re><rePic>rePic:9</rePic><reUrl>reUrl:9</reUrl><active>1</active><container>9</container></re>');
INSERT INTO `fsn_display_uri` VALUES (27,2,'<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><ad><adPic>adPic:1</adPic><adUrl>adUrl:1</adUrl><colCode></colCode><active>1</active><container>1</container></ad>');
/*!40000 ALTER TABLE `fsn_display_uri` ENABLE KEYS */;
UNLOCK TABLES;

/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
