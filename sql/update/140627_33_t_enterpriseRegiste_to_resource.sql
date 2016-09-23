/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50534
Source Host           : localhost:3306
Source Database       : fsn_cloud_qa

Target Server Type    : MYSQL
Target Server Version : 50534
File Encoding         : 65001

Date: 2014-06-25 17:31:34
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `t_enterpriseregiste_to_resource`
-- ----------------------------
DROP TABLE IF EXISTS `t_enterpriseregiste_to_resource`;
CREATE TABLE `t_enterpriseregiste_to_resource` (
  `RESOURCE_ID` bigint(20) NOT NULL COMMENT '资源ID',
  `ENTERPRISE_REGISTE_ID` bigint(20) NOT NULL COMMENT '注册企业ID',
  PRIMARY KEY (`RESOURCE_ID`,`ENTERPRISE_REGISTE_ID`),
  KEY `FK_enterprise_registe_to_id` (`ENTERPRISE_REGISTE_ID`),
  CONSTRAINT `FK_enterprise_registe_to_id` FOREIGN KEY (`ENTERPRISE_REGISTE_ID`) REFERENCES `enterprise_registe` (`id`),
  CONSTRAINT `FK_resource_id_to_resource` FOREIGN KEY (`RESOURCE_ID`) REFERENCES `t_test_resource` (`RESOURCE_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_enterpriseregiste_to_resource
-- ----------------------------
