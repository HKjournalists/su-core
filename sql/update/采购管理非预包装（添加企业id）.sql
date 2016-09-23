/*
Navicat MySQL Data Transfer

Source Server         : nicp_server
Source Server Version : 50535
Source Host           : 192.168.0.8:3306
Source Database       : fsn-ht

Target Server Type    : MYSQL
Target Server Version : 50535
File Encoding         : 65001

Date: 2016-05-25 11:21:46
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `ledger_prepackno`
-- ----------------------------
DROP TABLE IF EXISTS `ledger_prepackno`;
CREATE TABLE `ledger_prepackno` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `product_name` varchar(100) DEFAULT NULL,
  `alias` varchar(100) DEFAULT NULL,
  `standard` varchar(100) DEFAULT NULL,
  `number` varchar(100) DEFAULT NULL,
  `purchase_time` date DEFAULT NULL,
  `company_name` varchar(100) DEFAULT NULL,
  `company_phone` varchar(100) DEFAULT NULL,
  `supplier` varchar(100) DEFAULT NULL,
  `company_address` varchar(100) DEFAULT NULL,
  `qiyeId` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of ledger_prepackno
-- ----------------------------
INSERT INTO `ledger_prepackno` VALUES ('9', '123', '123', '123', '123', '2016-05-04', '123', '13880124567', '13', '123', '42551');
INSERT INTO `ledger_prepackno` VALUES ('10', '回锅肉', '回锅肉', '12', '1', '2016-05-17', '山崖', '15234906321', '好吃', '四川', '42551');
