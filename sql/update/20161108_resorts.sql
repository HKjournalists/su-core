/*
Navicat MySQL Data Transfer

Source Server         : mysql
Source Server Version : 50550
Source Host           : localhost:3306
Source Database       : stg_backfsn

Target Server Type    : MYSQL
Target Server Version : 50550
File Encoding         : 65001

Date: 2016-11-08 14:30:29
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `resorts`
-- ----------------------------
DROP TABLE IF EXISTS `resorts`;
CREATE TABLE `resorts` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) DEFAULT NULL COMMENT '景区名称',
  `level` varchar(50) DEFAULT NULL COMMENT '等级',
  `resort_info` varchar(255) DEFAULT NULL COMMENT '景区简介',
  `resort_price` varchar(50) DEFAULT NULL COMMENT '门票',
  `reserve_telephone` varchar(50) DEFAULT NULL COMMENT '订票电话',
  `resort_type` varchar(50) DEFAULT NULL COMMENT '景区类型',
  `resort_address` varchar(255) DEFAULT NULL COMMENT '景区地址',
  `ranks` varchar(50) DEFAULT NULL COMMENT '经纬度',
  `place_name` varchar(50) DEFAULT NULL COMMENT '覆盖物名称',
  `longitude` float(10,7) DEFAULT NULL COMMENT '经度',
  `latitude` float(10,7) DEFAULT NULL COMMENT '纬度',
  `organization` bigint(20) DEFAULT NULL COMMENT '当前组织机构ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of resorts
-- ----------------------------

-- ----------------------------
-- Table structure for `resorts_info_to_source`
-- ----------------------------
DROP TABLE IF EXISTS `resorts_info_to_source`;
CREATE TABLE `resorts_info_to_source` (
  `resorts_id` bigint(22) NOT NULL,
  `RESOURCE_ID` bigint(22) NOT NULL,
  PRIMARY KEY (`resorts_id`,`RESOURCE_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of resorts_info_to_source
-- ----------------------------

-- ----------------------------
-- Table structure for `resorts_logo_to_source`
-- ----------------------------
DROP TABLE IF EXISTS `resorts_logo_to_source`;
CREATE TABLE `resorts_logo_to_source` (
  `resorts_id` bigint(20) NOT NULL,
  `RESOURCE_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`resorts_id`,`RESOURCE_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of resorts_logo_to_source
-- ----------------------------
