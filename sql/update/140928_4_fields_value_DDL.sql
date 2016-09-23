/*
Navicat MySQL Data Transfer

Source Server         : GETTEC
Source Server Version : 50534
Source Host           : localhost:3306
Source Database       : fsn_core

Target Server Type    : MYSQL
Target Server Version : 50534
File Encoding         : 65001

Date: 2014-09-27 21:53:02
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `fields_value`
-- ----------------------------
DROP TABLE IF EXISTS `fields_value`;
CREATE TABLE `fields_value` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `value` varchar(100) DEFAULT NULL,
  `field_id` bigint(20) DEFAULT NULL,
  `template_id` bigint(20) DEFAULT NULL,
  `busunit_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of fields_value
-- ----------------------------
