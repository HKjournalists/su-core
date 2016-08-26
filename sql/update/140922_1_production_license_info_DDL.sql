/*
Navicat MySQL Data Transfer

Source Server         : GETTEC
Source Server Version : 50534
Source Host           : 127.0.0.1:3306
Source Database       : fsn_core

Target Server Type    : MYSQL
Target Server Version : 50534
File Encoding         : 65001

Date: 2014-09-23 10:31:50
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `production_license_info`
-- ----------------------------
DROP TABLE IF EXISTS `production_license_info`;
CREATE TABLE `production_license_info` (
  `qs_no` varchar(50) NOT NULL COMMENT '证书编号',
  `busunit_name` varchar(200) DEFAULT NULL COMMENT '生产企业名称',
  `product_name` varchar(200) DEFAULT NULL COMMENT '产品名称',
  `start_time` datetime DEFAULT NULL COMMENT '有效期: 开始时间',
  `end_time` datetime DEFAULT NULL COMMENT '有效期: 结束时间',
  `accommodation` varchar(200) DEFAULT NULL COMMENT '住所',
  `accommodation_other_address` varchar(300) DEFAULT NULL COMMENT '住所别名',
  `production_address` varchar(200) DEFAULT NULL COMMENT '生产地址',
  `production_other_address` varchar(300) DEFAULT NULL COMMENT '地址别名',
  `check_type` varchar(50) DEFAULT NULL COMMENT '检验方式',
  PRIMARY KEY (`qs_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;