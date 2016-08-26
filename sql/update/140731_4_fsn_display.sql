/*
Navicat MySQL Data Transfer

Source Server         : GETTEC
Source Server Version : 50534
Source Host           : 127.0.0.1:3306
Source Database       : fsn_core

Target Server Type    : MYSQL
Target Server Version : 50534
File Encoding         : 65001

Date: 2014-07-31 15:31:53
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `fsn_display`
-- ----------------------------
DROP TABLE IF EXISTS `fsn_display`;
CREATE TABLE `fsn_display` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `usrType` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '用户类型：匿名用户、cookie用户，实名用户',
  `uriId` bigint(20) DEFAULT NULL COMMENT 'URI编号',
  `container` int(3) DEFAULT NULL COMMENT '显示槽位',
  `active` int(1) NOT NULL DEFAULT '0' COMMENT '激活',
  `pid` bigint(20) DEFAULT NULL COMMENT '父ID',
  `type` int(1) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=11 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='根据用户类型在页面显示对应的信息，包含推荐栏、广告栏、楼';

-- ----------------------------
-- Records of fsn_display
-- ----------------------------
INSERT INTO `fsn_display` VALUES ('1', 'default', '159', '1', '1', null, '4');
INSERT INTO `fsn_display` VALUES ('2', 'default', '20', '2', '1', null, '4');
INSERT INTO `fsn_display` VALUES ('3', 'default', '161', '3', '1', null, '4');
INSERT INTO `fsn_display` VALUES ('4', 'default', '24', '4', '1', null, '4');
INSERT INTO `fsn_display` VALUES ('5', 'default', '23', '5', '1', null, '4');
INSERT INTO `fsn_display` VALUES ('6', 'default', '17', '6', '1', null, '4');
INSERT INTO `fsn_display` VALUES ('7', 'default', '184', '7', '1', null, '4');
INSERT INTO `fsn_display` VALUES ('8', 'default', '18', '8', '1', null, '4');
INSERT INTO `fsn_display` VALUES ('9', 'default', '19', '9', '1', null, '4');
INSERT INTO `fsn_display` VALUES ('10', 'default', '26', '10', '1', null, '4');
