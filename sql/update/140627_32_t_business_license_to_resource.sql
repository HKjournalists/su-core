/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50534
Source Host           : localhost:3306
Source Database       : fsn_cloud_qa

Target Server Type    : MYSQL
Target Server Version : 50534
File Encoding         : 65001

Date: 2014-06-25 17:31:29
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `t_business_license_to_resource`
-- ----------------------------
DROP TABLE IF EXISTS `t_business_license_to_resource`;
CREATE TABLE `t_business_license_to_resource` (
  `RESOURCE_ID` bigint(20) NOT NULL COMMENT '资源ID',
  `ENTERPRISE_REGISTE_ID` bigint(20) NOT NULL COMMENT '注册企业ID',
  PRIMARY KEY (`RESOURCE_ID`,`ENTERPRISE_REGISTE_ID`),
  KEY `FK_enterpriseRegiste_id` (`ENTERPRISE_REGISTE_ID`),
  CONSTRAINT `FK_resource_id_id` FOREIGN KEY (`RESOURCE_ID`) REFERENCES `t_test_resource` (`RESOURCE_ID`),
  CONSTRAINT `FK_enterpriseRegiste_id` FOREIGN KEY (`ENTERPRISE_REGISTE_ID`) REFERENCES `enterprise_registe` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_business_license_to_resource
-- ----------------------------
