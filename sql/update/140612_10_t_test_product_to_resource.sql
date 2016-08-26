/*
Navicat MySQL Data Transfer

Source Server         : GETTEC
Source Server Version : 50530
Source Host           : localhost:3306
Source Database       : fsn_core_ceshi

Target Server Type    : MYSQL
Target Server Version : 50530
File Encoding         : 65001

Date: 2014-06-11 17:52:06
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `t_test_product_to_resource`
-- ----------------------------
DROP TABLE IF EXISTS `t_test_product_to_resource`;
CREATE TABLE `t_test_product_to_resource` (
  `RESOURCE_ID` bigint(20) NOT NULL COMMENT '资源ID',
  `PRODUCT_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`RESOURCE_ID`,`PRODUCT_ID`),
  KEY `FK_resourceId_to_resource_iid` (`RESOURCE_ID`),
  KEY `PRODUCT_ID` (`PRODUCT_ID`),
  CONSTRAINT `FK_productId_productId` FOREIGN KEY (`PRODUCT_ID`) REFERENCES `product` (`id`),
  CONSTRAINT `FK_resourceId_to_resource_id` FOREIGN KEY (`RESOURCE_ID`) REFERENCES `t_test_resource` (`RESOURCE_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_test_product_to_resource
-- ----------------------------
