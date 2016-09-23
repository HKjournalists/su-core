/*
Navicat MySQL Data Transfer

Source Server         : GETTEC
Source Server Version : 50530
Source Host           : localhost:3306
Source Database       : fsn_core_ceshi

Target Server Type    : MYSQL
Target Server Version : 50530
File Encoding         : 65001

Date: 2014-06-11 17:52:31
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `t_test_report_to_resource`
-- ----------------------------
DROP TABLE IF EXISTS `t_test_report_to_resource`;
CREATE TABLE `t_test_report_to_resource` (
  `REPORT_ID` bigint(20) NOT NULL COMMENT '报告ID',
  `RESOURCE_ID` bigint(20) NOT NULL COMMENT '资源ID',
  PRIMARY KEY (`RESOURCE_ID`,`REPORT_ID`),
  KEY `FK_report_iid_to_id` (`REPORT_ID`),
  KEY `FK_resource_iid_to_id` (`RESOURCE_ID`) USING BTREE,
  CONSTRAINT `FK_report_iid_to_id` FOREIGN KEY (`REPORT_ID`) REFERENCES `test_result` (`id`),
  CONSTRAINT `FK_resource_iid_to_id` FOREIGN KEY (`RESOURCE_ID`) REFERENCES `t_test_resource` (`RESOURCE_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_test_report_to_resource
-- ----------------------------
