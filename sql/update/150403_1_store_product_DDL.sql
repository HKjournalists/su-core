/*
Navicat MySQL Data Transfer

Source Server         : admin
Source Server Version : 50534
Source Host           : localhost:3306
Source Database       : fsn1

Target Server Type    : MYSQL
Target Server Version : 50534
File Encoding         : 65001

Date: 2015-04-03 11:08:03
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `store_product`
-- ----------------------------
DROP TABLE IF EXISTS `store_product`;
CREATE TABLE `store_product` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `product_id` bigint(20) DEFAULT NULL COMMENT '关联产品id',
  `userId` bigint(20) DEFAULT NULL COMMENT '用户id',
  `add_date` date DEFAULT NULL COMMENT '产品收藏时间',
  `productName` varchar(10000) DEFAULT NULL COMMENT '产品名称',
  `productImg` varchar(10000) DEFAULT NULL COMMENT '产品照片',
  PRIMARY KEY (`id`),
  KEY `product_id` (`product_id`),
  CONSTRAINT `store_product_ibfk_1` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of store_product
-- ----------------------------
