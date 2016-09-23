/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50534
Source Host           : localhost:3306
Source Database       : fsn_new

Target Server Type    : MYSQL
Target Server Version : 50534
File Encoding         : 65001

Date: 2014-10-24 10:31:59
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `businessunit_to_prolicinfo`
-- ----------------------------
DROP TABLE IF EXISTS `businessunit_to_prolicinfo`;
CREATE TABLE `businessunit_to_prolicinfo` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `business_id` bigint(20) DEFAULT NULL,
  `qs_no` varchar(50) DEFAULT NULL COMMENT 'qs号',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of businessunit_to_prolicinfo
-- ----------------------------
