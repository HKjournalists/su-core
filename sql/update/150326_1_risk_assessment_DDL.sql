/*
Navicat MySQL Data Transfer

Source Server         : admin
Source Server Version : 50534
Source Host           : localhost:3306
Source Database       : fsn_core

Target Server Type    : MYSQL
Target Server Version : 50534
File Encoding         : 65001

Date: 2015-03-26 11:22:15
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `risk_assessment`
-- ----------------------------
DROP TABLE IF EXISTS `risk_assessment`;
CREATE TABLE `risk_assessment` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `risk_Index` double(20,2) DEFAULT NULL COMMENT '风险指数',
  `product_id` bigint(20) DEFAULT NULL COMMENT '产品实例id',
  `risk_succeed` int(1) DEFAULT NULL COMMENT '风险指数的计算成功:0：失败、1：成功',
  `risk_failure` varchar(10000) DEFAULT NULL COMMENT '风险指数计算失败的原因',
  `user_name` varchar(50) DEFAULT NULL COMMENT '记录人',
  `risk_Date` datetime DEFAULT NULL COMMENT '风险指数的计算时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of risk_assessment
-- ----------------------------
