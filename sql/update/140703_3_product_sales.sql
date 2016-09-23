/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50534
Source Host           : localhost:3306
Source Database       : fsn_cloud_qa

Target Server Type    : MYSQL
Target Server Version : 50534
File Encoding         : 65001

Date: 2014-07-03 18:11:55
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `product_sales`
-- ----------------------------
DROP TABLE IF EXISTS `product_sales`;
CREATE TABLE `product_sales` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `product_id` bigint(20) DEFAULT NULL,
  `batch_serial_no` varchar(50) DEFAULT NULL COMMENT '批次号',
  `target_customer` varchar(200) DEFAULT NULL COMMENT '销售对象',
  `sales_territory` varchar(200) DEFAULT NULL COMMENT '销售地区',
  `sales_quantity` int(20) DEFAULT NULL COMMENT '销售数量',
  `pArea_id` int(20) DEFAULT '0' COMMENT '省级地区id',
  `mArea_id` int(20) DEFAULT '0' COMMENT '市级地区id',
  `cArea_id` int(20) DEFAULT '0' COMMENT '县级地区id',
  `organization` bigint(20) DEFAULT NULL COMMENT '组织机构id',
  PRIMARY KEY (`id`),
  KEY `fk_product_sales_product` (`product_id`),
  CONSTRAINT `fk_product_sales_product` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of product_sales
-- ----------------------------
INSERT INTO `product_sales` VALUES ('1', '12288', '53543543', 'bfgngf', 'hgjhgj', '32', '1', '2', '4', '30');
INSERT INTO `product_sales` VALUES ('2', '12288', 'fg', 'gfg', '贵州省贵阳市南明区', '4343', '1', '2', '4', '30');
INSERT INTO `product_sales` VALUES ('3', '12288', 'fdf', 'rerer', '贵州省贵阳市--请选择--', '4343', '1', '2', '0', '30');
INSERT INTO `product_sales` VALUES ('4', '12288', 'rer', 'dfdf', '贵州省', '323', '1', '14', '0', '30');
INSERT INTO `product_sales` VALUES ('5', '12289', 'dffdf34343', 'sdsd', '贵州省', '33', '1', '2', '0', '30');
INSERT INTO `product_sales` VALUES ('6', '12292', 'trt544545', '54545', '贵州省', '545', '1', '19', '0', '30');
INSERT INTO `product_sales` VALUES ('7', '12292', '434rer', 'rerer', '贵州省遵义市', '434', '1', '19', '0', '30');
