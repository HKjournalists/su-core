/*
Navicat MySQL Data Transfer

Source Server         : GETTEC
Source Server Version : 50534
Source Host           : 127.0.0.1:3306
Source Database       : fsn_core

Target Server Type    : MYSQL
Target Server Version : 50534
File Encoding         : 65001

Date: 2014-07-31 15:32:08
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `fsn_display_new_type`
-- ----------------------------
DROP TABLE IF EXISTS `fsn_display_new_type`;
CREATE TABLE `fsn_display_new_type` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) CHARACTER SET utf8 DEFAULT NULL COMMENT '显示名称',
  `keyword` varchar(100) CHARACTER SET utf8 DEFAULT NULL COMMENT '搜索关键字',
  `position` int(1) DEFAULT NULL COMMENT '位置，0-不激活，1-上栏，2-下栏',
  PRIMARY KEY (`Id`)
) ENGINE=MyISAM AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Records of fsn_display_new_type
-- ----------------------------
INSERT INTO `fsn_display_new_type` VALUES ('1', '相关信息', '贵州食品安全', '1');
INSERT INTO `fsn_display_new_type` VALUES ('2', '用户操作', '饮食常识', '2');
