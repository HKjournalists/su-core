/*
Navicat MySQL Data Transfer

Source Server         : nicp_server
Source Server Version : 50535
Source Host           : 192.168.0.8:3306
Source Database       : fsn-ht

Target Server Type    : MYSQL
Target Server Version : 50535
File Encoding         : 65001

Date: 2016-05-24 09:31:43
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
  `login_id` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of ledger_prepackno
-- ----------------------------
INSERT INTO `ledger_prepackno` VALUES ('1', '2', '2', '2', '2', '2016-05-18', '2', '13880452962', '2', '2', null);
INSERT INTO `ledger_prepackno` VALUES ('3', '啊', '加', '啊', '1', '2016-05-05', '啊', '13845688900', '1', '啊', null);
INSERT INTO `ledger_prepackno` VALUES ('4', '2312', '12312', '312', '131', '2016-05-18', '2312', '15245678901', '1', '1', null);
INSERT INTO `ledger_prepackno` VALUES ('5', '1', '1', '1', '1', '2016-05-05', '1', '13856789087', '1', '1说的', null);
