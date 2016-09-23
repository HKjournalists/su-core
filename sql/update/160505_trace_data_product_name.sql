/*
Navicat MySQL Data Transfer

Source Server         : 本机数据库
Source Server Version : 50534
Source Host           : localhost:3306
Source Database       : fsn

Target Server Type    : MYSQL
Target Server Version : 50534
File Encoding         : 65001

Date: 2016-05-05 13:49:29
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
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of trace_data_product_name
-- ----------------------------
INSERT INTO `trace_data_product_name` VALUES ('1', '国台.适宜18度酒', '37142', '6927322002618');
INSERT INTO `trace_data_product_name` VALUES ('2', '都匀毛尖茶', '64866', '6932440900067');
INSERT INTO `trace_data_product_name` VALUES ('3', '特仑苏纯牛奶苗条装 250ml×12 盒', '37555', '6923644266318');
INSERT INTO `trace_data_product_name` VALUES ('4', '新飞天53%vol500ml贵州茅台酒（1×6）RFID', '59144', '6902952880294');
INSERT INTO `trace_data_product_name` VALUES ('5', '红宝石红茶一级钻石茶包30克', '51552', '6953424001815');
