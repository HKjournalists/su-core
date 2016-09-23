/*
Navicat MySQL Data Transfer

Source Server         : 本机数据库
Source Server Version : 50534
Source Host           : localhost:3306
Source Database       : fsn

Target Server Type    : MYSQL
Target Server Version : 50534
File Encoding         : 65001

Date: 2016-08-23 19:58:38
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `t_map_product`
-- ----------------------------
DROP TABLE IF EXISTS `t_map_product`;
CREATE TABLE `t_map_product` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `product_id` bigint(20) unsigned NOT NULL COMMENT '产品id',
  `organization` bigint(20) unsigned NOT NULL COMMENT '组织机构',
  `product_name` varchar(255) NOT NULL DEFAULT '',
  `lat` float NOT NULL,
  `lng` float NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;


-- ----------------------------
-- Table structure for `t_map_product_addr`
-- ----------------------------
DROP TABLE IF EXISTS `t_map_product_addr`;
CREATE TABLE `t_map_product_addr` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `lat` float NOT NULL,
  `lng` float NOT NULL,
  `map_product_id` int(10) unsigned NOT NULL,
  `describe` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=71 DEFAULT CHARSET=utf8;

ALTER TABLE `t_map_product`
ADD UNIQUE INDEX (`product_id`) ;

ALTER TABLE `t_map_product_addr`
ADD INDEX (`map_product_id`) ;

