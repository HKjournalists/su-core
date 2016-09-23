/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50530
Source Host           : localhost:3306
Source Database       : fsn_cloud

Target Server Type    : MYSQL
Target Server Version : 50530
File Encoding         : 65001

Date: 2014-09-27 21:14:37
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `liquor_sales_license`
-- ----------------------------
DROP TABLE IF EXISTS `liquor_sales_license`;
CREATE TABLE `liquor_sales_license` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `certificate_no` varchar(100) DEFAULT NULL COMMENT '许可证编号',
  `legal_name` varchar(100) DEFAULT NULL COMMENT '法定代表人',
  `address` varchar(200) DEFAULT NULL COMMENT '地址',
  `business_type` varchar(100) DEFAULT NULL COMMENT '经营类型',
  `business_scope` varchar(100) DEFAULT NULL COMMENT '经营范围',
  `start_time` datetime DEFAULT NULL COMMENT '有效期: 结束时间',
  `end_time` datetime DEFAULT NULL COMMENT '有效期: 结束时间',
  `issuing_authority` varchar(100) DEFAULT NULL COMMENT '发证机关',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of liquor_sales_license
-- ----------------------------
