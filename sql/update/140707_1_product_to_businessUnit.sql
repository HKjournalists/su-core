/*
Navicat MySQL Data Transfer

Source Server         : GETTEC
Source Server Version : 50534
Source Host           : localhost:3306
Source Database       : fsn_core

Target Server Type    : MYSQL
Target Server Version : 50534
File Encoding         : 65001

Date: 2014-07-07 15:15:28
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `product_to_businessunit`
-- ----------------------------
DROP TABLE IF EXISTS `product_to_businessunit`;
CREATE TABLE `product_to_businessunit` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `PRODUCT_ID` bigint(20) DEFAULT NULL COMMENT '产品id',
  `ORGNIZATION` bigint(20) DEFAULT NULL COMMENT '子公司的组织机构id',
  `QS_NO` varchar(50) DEFAULT NULL COMMENT '子企业qs号',
  `BARCORD` varchar(100) DEFAULT NULL COMMENT '产品条形码',
  PRIMARY KEY (`ID`),
  KEY `FK_product_id_to_product_id` (`PRODUCT_ID`),
  CONSTRAINT `FK_product_id_to_product_id` FOREIGN KEY (`PRODUCT_ID`) REFERENCES `product` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of product_to_businessunit
-- ----------------------------
