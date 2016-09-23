/*
Navicat MySQL Data Transfer

Source Server         : GETTEC
Source Server Version : 50534
Source Host           : 127.0.0.1:3306
Source Database       : fsn_core

Target Server Type    : MYSQL
Target Server Version : 50534
File Encoding         : 65001

Date: 2014-09-23 09:45:24
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `t_business_productionlicense_to_resource`
-- ----------------------------
DROP TABLE IF EXISTS `t_business_productionlicense_to_resource`;
CREATE TABLE `t_business_productionlicense_to_resource` (
  `RESOURCE_ID` bigint(20) NOT NULL,
  `ENTERPRISE_REGISTE_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`RESOURCE_ID`,`ENTERPRISE_REGISTE_ID`),
  KEY `FK_enterpriseRegiste_to_iid` (`ENTERPRISE_REGISTE_ID`),
  CONSTRAINT `FK_enterpriseRegiste_to_iid` FOREIGN KEY (`ENTERPRISE_REGISTE_ID`) REFERENCES `enterprise_registe` (`id`),
  CONSTRAINT `FK_resource_id_to_iid` FOREIGN KEY (`RESOURCE_ID`) REFERENCES `t_test_resource` (`RESOURCE_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_business_productionlicense_to_resource
-- ----------------------------
