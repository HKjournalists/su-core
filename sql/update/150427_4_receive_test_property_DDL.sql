/*
Navicat MySQL Data Transfer

Source Server         : GETTEC
Source Server Version : 50534
Source Host           : 127.0.0.1:3306
Source Database       : fsn_core

Target Server Type    : MYSQL
Target Server Version : 50534
File Encoding         : 65001

Date: 2015-04-27 12:18:43
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `receive_test_property`
-- ----------------------------
DROP TABLE IF EXISTS `receive_test_property`;
CREATE TABLE `receive_test_property` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(200) NOT NULL COMMENT '检测项目名称',
  `unit` varchar(255) DEFAULT NULL COMMENT '单位',
  `indicant` varchar(255) DEFAULT NULL COMMENT '技术指标',
  `result` varchar(300) DEFAULT NULL COMMENT '检测结果',
  `conclusion` varchar(255) DEFAULT NULL COMMENT '单项评价',
  `standard` varchar(300) DEFAULT NULL COMMENT '执行标准',
  `rec_test_result_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of receive_test_property
-- ----------------------------
