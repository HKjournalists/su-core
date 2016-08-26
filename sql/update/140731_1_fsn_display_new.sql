/*
Navicat MySQL Data Transfer

Source Server         : GETTEC
Source Server Version : 50534
Source Host           : 127.0.0.1:3306
Source Database       : fsn_core

Target Server Type    : MYSQL
Target Server Version : 50534
File Encoding         : 65001

Date: 2014-07-31 15:32:00
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `fsn_display_new`
-- ----------------------------
DROP TABLE IF EXISTS `fsn_display_new`;
CREATE TABLE `fsn_display_new` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `TITLE` varchar(50) DEFAULT NULL,
  `SUMMARY` varchar(100) DEFAULT NULL,
  `URL` varchar(50) DEFAULT NULL,
  `TYPE` int(1) DEFAULT NULL COMMENT '资源类型 1-版本新闻 2-功能接受 ',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of fsn_display_new
-- ----------------------------
INSERT INTO `fsn_display_new` VALUES ('1', 'FSN1.5.0新版本于2014年8月2日正式发布。欢迎广大用户试用！', '点击可查看详细内容', 'newVersion/newVersion_2_0_0.html', '1');
INSERT INTO `fsn_display_new` VALUES ('2', '生产企业用户操作说明', '点击可查看详细说明文档', 'userguid/production.html', '2');
