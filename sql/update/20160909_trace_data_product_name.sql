/*
Navicat MySQL Data Transfer

Source Server         : fsn
Source Server Version : 50545
Source Host           : localhost:3306
Source Database       : fsn_core_new

Target Server Type    : MYSQL
Target Server Version : 50545
File Encoding         : 65001

Date: 2016-09-09 17:20:19
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `trace_data_product_name`
-- ----------------------------
DROP TABLE IF EXISTS `trace_data_product_name`;
CREATE TABLE `trace_data_product_name` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `trace_data_product_name` varchar(255) NOT NULL,
  `product_id` int(10) unsigned NOT NULL,
  `barcode` varchar(30) NOT NULL,
  `RECORD_INSERT_TIME` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of trace_data_product_name
-- ----------------------------
