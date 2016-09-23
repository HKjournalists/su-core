/*
Navicat MySQL Data Transfer

Source Server         : fsn
Source Server Version : 50545
Source Host           : localhost:3306
Source Database       : fsn_core

Target Server Type    : MYSQL
Target Server Version : 50545
File Encoding         : 65001

Date: 2016-09-09 18:39:34
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `trace_data_to_resource`
-- ----------------------------
DROP TABLE IF EXISTS `trace_data_to_resource`;
CREATE TABLE `trace_data_to_resource` (
  `TRACE_DATA_ID` bigint(20) NOT NULL,
  `RESOURCE_ID` bigint(20) NOT NULL COMMENT '资源ID',
  `RECORD_INSERT_TIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`TRACE_DATA_ID`,`RESOURCE_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of trace_data_to_resource
-- ----------------------------
