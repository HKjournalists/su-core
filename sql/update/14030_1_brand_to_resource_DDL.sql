/*
Navicat MySQL Data Transfer

Source Server         : GETTEC
Source Server Version : 50534
Source Host           : localhost:3306
Source Database       : fsn_core

Target Server Type    : MYSQL
Target Server Version : 50534
File Encoding         : 65001

Date: 2014-06-30 09:29:44
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `brand_to_resource`
-- ----------------------------
DROP TABLE IF EXISTS `brand_to_resource`;
CREATE TABLE `brand_to_resource` (
  `BRAND_ID` bigint(20) NOT NULL,
  `RESOURCE_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`BRAND_ID`,`RESOURCE_ID`),
  KEY `FK_resourceId_resource_id` (`RESOURCE_ID`),
  CONSTRAINT `FK_brandId_brand_id` FOREIGN KEY (`BRAND_ID`) REFERENCES `business_brand` (`id`),
  CONSTRAINT `FK_resourceId_resource_id` FOREIGN KEY (`RESOURCE_ID`) REFERENCES `t_test_resource` (`RESOURCE_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of brand_to_resource
-- ----------------------------
