/*
Navicat MySQL Data Transfer

Source Server         : nicp_server
Source Server Version : 50535
Source Host           : 192.168.0.8:3306
Source Database       : fsn-ht

Target Server Type    : MYSQL
Target Server Version : 50535
File Encoding         : 65001

Date: 2016-05-24 09:33:20
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
INSERT INTO `picefile_to_resource` VALUES ('35', '373289');
INSERT INTO `picefile_to_resource` VALUES ('36', '373322');
INSERT INTO `picefile_to_resource` VALUES ('37', '373324');

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
  `creater` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=38 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of waste_dispoa
-- ----------------------------
INSERT INTO `waste_dispoa` VALUES ('35', '2', '3', '1', '2016-05-11 08:00:00', '3', '2', null, null, null);
INSERT INTO `waste_dispoa` VALUES ('36', '2', '2阿斯顿发', '1', '2016-05-23 08:00:00', '是打发', '22222', null, null, null);
INSERT INTO `waste_dispoa` VALUES ('37', '2', '2', '1', '2016-05-02 08:00:00', '驱蚊器', '2', null, null, null);
