/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50550
Source Host           : localhost:3306
Source Database       : stg_backfsn

Target Server Type    : MYSQL
Target Server Version : 50550
File Encoding         : 65001

Date: 2016-09-21 09:28:35
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `business_navigation`
-- ----------------------------
DROP TABLE IF EXISTS `business_navigation`;
CREATE TABLE `business_navigation` (
  `id` bigint(22) NOT NULL AUTO_INCREMENT,
  `big_option` varchar(255) DEFAULT NULL COMMENT '大的选项',
  `small_option` varchar(255) DEFAULT NULL COMMENT '小的选项',
  `navigation_url` varchar(255) DEFAULT NULL COMMENT '导航URL',
  `business_id` bigint(22) DEFAULT NULL COMMENT '企业ID',
  `organization` bigint(22) DEFAULT NULL COMMENT '组织机构ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of business_navigation
-- ----------------------------
