/*
Navicat MySQL Data Transfer

Source Server         : GETTEC
Source Server Version : 50530
Source Host           : localhost:3306
Source Database       : lims_core_market

Target Server Type    : MYSQL
Target Server Version : 50530
File Encoding         : 65001

Date: 2014-06-11 17:36:42
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `t_test_resource`
-- ----------------------------
DROP TABLE IF EXISTS `t_test_resource`;
CREATE TABLE `t_test_resource` (
  `RESOURCE_ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `FILE_NAME` varchar(100) DEFAULT NULL COMMENT '文件名称',
  `RESOURCE_NAME` varchar(255) DEFAULT NULL COMMENT '资源名称',
  `URL` varchar(255) DEFAULT NULL,
  `RESOURCE_TYPE_ID` bigint(20) DEFAULT NULL COMMENT '资源类型ID',
  PRIMARY KEY (`RESOURCE_ID`),
  KEY `FK_resource_type_id_to_id` (`RESOURCE_TYPE_ID`),
  CONSTRAINT `FK_resource_type_id_to_id` FOREIGN KEY (`RESOURCE_TYPE_ID`) REFERENCES `t_sys_resource_type` (`RESOURCE_TYPE_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_test_resource
-- ----------------------------
