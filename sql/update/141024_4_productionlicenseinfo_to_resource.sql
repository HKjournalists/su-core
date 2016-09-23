/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50534
Source Host           : localhost:3306
Source Database       : fsn_new

Target Server Type    : MYSQL
Target Server Version : 50534
File Encoding         : 65001

Date: 2014-10-24 17:42:45
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `productionlicenseinfo_to_resource`
-- ----------------------------
DROP TABLE IF EXISTS `productionlicenseinfo_to_resource`;
CREATE TABLE `productionlicenseinfo_to_resource` (
  `qs_no` varchar(50) NOT NULL,
  `resource_id` bigint(20) NOT NULL,
  PRIMARY KEY (`qs_no`,`resource_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of productionlicenseinfo_to_resource
-- ----------------------------
