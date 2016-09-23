/*
Navicat MySQL Data Transfer

Source Server         : admin
Source Server Version : 50534
Source Host           : localhost:3306
Source Database       : fsn_core

Target Server Type    : MYSQL
Target Server Version : 50534
File Encoding         : 65001

Date: 2015-03-26 11:22:05
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `risk_to_property`
-- ----------------------------
DROP TABLE IF EXISTS `risk_to_property`;
CREATE TABLE `risk_to_property` (
  `risk_id` bigint(20) NOT NULL COMMENT '风险指数id',
  `property_id` bigint(20) NOT NULL COMMENT '检测项目id',
  PRIMARY KEY (`risk_id`,`property_id`),
  KEY `property_id` (`property_id`),
  CONSTRAINT `risk_to_property_ibfk_1` FOREIGN KEY (`risk_id`) REFERENCES `risk_assessment` (`id`),
  CONSTRAINT `risk_to_property_ibfk_2` FOREIGN KEY (`property_id`) REFERENCES `test_property` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of risk_to_property
-- ----------------------------
