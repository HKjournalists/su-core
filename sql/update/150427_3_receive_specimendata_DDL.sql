/*
Navicat MySQL Data Transfer

Source Server         : GETTEC
Source Server Version : 50534
Source Host           : 127.0.0.1:3306
Source Database       : fsn_core

Target Server Type    : MYSQL
Target Server Version : 50534
File Encoding         : 65001

Date: 2015-04-27 12:18:37
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `receive_specimendata`
-- ----------------------------
DROP TABLE IF EXISTS `receive_specimendata`;
CREATE TABLE `receive_specimendata` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `barcode` varchar(100) NOT NULL COMMENT '检测条码',
  `material` varchar(100) NOT NULL COMMENT '检测物名称',
  `density` int(11) DEFAULT NULL COMMENT '检测浓度(单位：ppm)',
  `temperature` int(11) DEFAULT NULL COMMENT '检测温度(单位：摄氏度)',
  `humidity` int(11) DEFAULT NULL COMMENT '检测湿度(单位：百分比)',
  `test_date` datetime NOT NULL COMMENT '检测时间',
  `tester` varchar(50) DEFAULT NULL COMMENT '检测人',
  `rec_test_result_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of receive_specimendata
-- ----------------------------
