/*
Navicat MySQL Data Transfer

Source Server         : GETTEC
Source Server Version : 50530
Source Host           : localhost:3306
Source Database       : fsn_core

Target Server Type    : MYSQL
Target Server Version : 50530
File Encoding         : 65001

Date: 2014-05-30 11:16:12
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `t_test_temp_item`
-- ----------------------------
DROP TABLE IF EXISTS `t_test_temp_item`;
CREATE TABLE `t_test_temp_item` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `ITEM_NAME` varchar(100) DEFAULT NULL,
  `ITEM_UNIT` varchar(100) DEFAULT NULL,
  `SPECIFICATION` varchar(100) DEFAULT NULL,
  `TEST_RESULT` varchar(100) DEFAULT NULL,
  `ITEM_CONCLUSION` varchar(100) DEFAULT NULL,
  `STANDARD` varchar(100) DEFAULT NULL,
  `REPORT_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FFK_reportID_to_reportID` (`REPORT_ID`),
  CONSTRAINT `FFK_reportID_to_reportID` FOREIGN KEY (`REPORT_ID`) REFERENCES `t_test_temp_report` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_test_temp_item
-- ----------------------------
