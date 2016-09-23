/*
Navicat MySQL Data Transfer

Source Server         : GETTEC
Source Server Version : 50530
Source Host           : localhost:3306
Source Database       : fsn_core_ceshi

Target Server Type    : MYSQL
Target Server Version : 50530
File Encoding         : 65001

Date: 2014-06-11 17:44:59
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `t_test_template`
-- ----------------------------
DROP TABLE IF EXISTS `t_test_template`;
CREATE TABLE `t_test_template` (
  `BAR_CODE` varchar(100) NOT NULL,
  `REPORT_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`BAR_CODE`),
  KEY `FK_TT_TO_REPORT` (`REPORT_ID`) USING BTREE
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_test_template
-- ----------------------------
