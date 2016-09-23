/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50530
Source Host           : localhost:3306
Source Database       : fsn_cloud

Target Server Type    : MYSQL
Target Server Version : 50530
File Encoding         : 65001

Date: 2014-09-27 21:14:16
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `tax_register_cert`
-- ----------------------------
DROP TABLE IF EXISTS `tax_register_cert`;
CREATE TABLE `tax_register_cert` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `taxer_name` varchar(100) DEFAULT NULL COMMENT '纳税人名称',
  `legal_name` varchar(100) DEFAULT NULL COMMENT '法定代表人',
  `address` varchar(200) DEFAULT NULL COMMENT '地址',
  `register_type` varchar(100) DEFAULT NULL COMMENT '登记注册类型',
  `business_scope` varchar(200) DEFAULT NULL COMMENT '经营范围',
  `approve_setUp_authority` varchar(200) DEFAULT NULL COMMENT '批准设立机关',
  `issuing_authority` varchar(200) DEFAULT NULL COMMENT '发证机关',
  `withholding_obligations` varchar(200) DEFAULT NULL COMMENT '扣缴义务',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tax_register_cert
-- ----------------------------
