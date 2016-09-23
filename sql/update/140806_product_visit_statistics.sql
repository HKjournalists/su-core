/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50534
Source Host           : localhost:3306
Source Database       : fsn_cloud_qa

Target Server Type    : MYSQL
Target Server Version : 50534
File Encoding         : 65001

Date: 2014-08-06 09:09:09
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `product_visit_statistics`
-- ----------------------------
DROP TABLE IF EXISTS `product_visit_statistics`;
CREATE TABLE `product_visit_statistics` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `product_id` bigint(20) NOT NULL COMMENT '产品id',
  `app_statistics` bigint(50) DEFAULT '0' COMMENT 'app访问次数统计',
  `portal_statistics` bigint(50) DEFAULT '0' COMMENT '大众portal访问次数统计',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;
