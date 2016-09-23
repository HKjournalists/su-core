/*
Navicat MySQL Data Transfer

Source Server         : nicp_server
Source Server Version : 50535
Source Host           : 192.168.0.8:3306
Source Database       : fsn-ht

Target Server Type    : MYSQL
Target Server Version : 50535
File Encoding         : 65001

Date: 2016-05-25 11:19:11
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `picefile_to_resource`
-- ----------------------------
DROP TABLE IF EXISTS `picefile_to_resource`;
CREATE TABLE `picefile_to_resource` (
  `PICEFILE_ID` bigint(20) NOT NULL,
  `RESOURCE_ID` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of picefile_to_resource
-- ----------------------------
INSERT INTO `picefile_to_resource` VALUES ('39', '373339');
INSERT INTO `picefile_to_resource` VALUES ('39', '373347');
INSERT INTO `picefile_to_resource` VALUES ('39', '373348');
INSERT INTO `picefile_to_resource` VALUES ('40', '373360');
INSERT INTO `picefile_to_resource` VALUES ('41', '373361');
INSERT INTO `picefile_to_resource` VALUES ('41', '373362');
INSERT INTO `picefile_to_resource` VALUES ('41', '373363');

-- ----------------------------
-- Table structure for `waste_dispoa`
-- ----------------------------
DROP TABLE IF EXISTS `waste_dispoa`;
CREATE TABLE `waste_dispoa` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `handle_number` varchar(100) DEFAULT NULL,
  `handle_way` varchar(100) DEFAULT NULL,
  `handler` varchar(100) DEFAULT NULL,
  `handle_time` timestamp NULL DEFAULT NULL,
  `participation` varchar(100) DEFAULT NULL,
  `destory` varchar(100) DEFAULT NULL,
  `pic_file` varchar(100) DEFAULT NULL,
  `create_time` date DEFAULT NULL,
  `qiyeId` bigint(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=42 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of waste_dispoa
-- ----------------------------
INSERT INTO `waste_dispoa` VALUES ('39', '2', '1', '1', '2016-05-03 08:00:00', '2', '13', null, null, '42551');
INSERT INTO `waste_dispoa` VALUES ('40', '2', '3', '1', '2016-05-04 08:00:00', '2', '1', null, null, '42551');
INSERT INTO `waste_dispoa` VALUES ('41', '1', '1da', '1jhajhajhjh', '2016-05-02 08:00:00', '1', '1', null, null, '42551');
