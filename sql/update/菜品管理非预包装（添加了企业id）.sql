/*
Navicat MySQL Data Transfer

Source Server         : nicp_server
Source Server Version : 50535
Source Host           : 192.168.0.8:3306
Source Database       : fsn-ht

Target Server Type    : MYSQL
Target Server Version : 50535
File Encoding         : 65001

Date: 2016-05-25 11:17:58
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `dishs_no`
-- ----------------------------
DROP TABLE IF EXISTS `dishs_no`;
CREATE TABLE `dishs_no` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `dishs_name` varchar(100) DEFAULT NULL,
  `alias` varchar(100) DEFAULT NULL,
  `baching` varchar(100) DEFAULT NULL,
  `qiyeId` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of dishs_no
-- ----------------------------
INSERT INTO `dishs_no` VALUES ('5', '1', '1', '1', '42551');
INSERT INTO `dishs_no` VALUES ('6', '1', '2', '1', '42551');
INSERT INTO `dishs_no` VALUES ('7', '2', '2', '2', '42551');
INSERT INTO `dishs_no` VALUES ('8', '2', '2', '2', '42551');

-- ----------------------------
-- Table structure for `dishsno_to_resource`
-- ----------------------------
DROP TABLE IF EXISTS `dishsno_to_resource`;
CREATE TABLE `dishsno_to_resource` (
  `dishsno_id` bigint(20) NOT NULL,
  `resource_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of dishsno_to_resource
-- ----------------------------
INSERT INTO `dishsno_to_resource` VALUES ('7', '373340');
INSERT INTO `dishsno_to_resource` VALUES ('5', '373357');
INSERT INTO `dishsno_to_resource` VALUES ('8', '373358');
INSERT INTO `dishsno_to_resource` VALUES ('8', '373359');
