/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50534
Source Host           : localhost:3306
Source Database       : fsn_new

Target Server Type    : MYSQL
Target Server Version : 50534
File Encoding         : 65001

Date: 2015-04-20 15:33:08
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `lims_not_find_product`
-- ----------------------------
DROP TABLE IF EXISTS `lims_not_find_product`;
CREATE TABLE `lims_not_find_product` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `proName` varchar(255) DEFAULT NULL COMMENT '产品名称',
  `barcode` varchar(100) DEFAULT NULL COMMENT '产品条形码',
  `jsonURL` varchar(255) DEFAULT NULL COMMENT 'json路径',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

