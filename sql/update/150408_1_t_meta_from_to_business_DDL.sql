/*
Navicat MySQL Data Transfer

Source Server         : GETTEC
Source Server Version : 50534
Source Host           : 127.0.0.1:3306
Source Database       : fsn_core

Target Server Type    : MYSQL
Target Server Version : 50534
File Encoding         : 65001

Date: 2015-04-08 09:35:17
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `t_meta_from_to_business`
-- ----------------------------
DROP TABLE IF EXISTS `t_meta_from_to_business`;
CREATE TABLE `t_meta_from_to_business` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `pro_id` bigint(20) NOT NULL COMMENT '产品id',
  `from_bus_id` bigint(20) NOT NULL COMMENT '出货商',
  `to_bus_id` bigint(20) NOT NULL COMMENT '收货商',
  `del` bit(1) NOT NULL COMMENT '0:未删除 1:已删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_meta_from_to_business
-- ----------------------------
