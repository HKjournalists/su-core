/*
Navicat MySQL Data Transfer

Source Server         : fsn
Source Server Version : 50534
Source Host           : localhost:3306
Source Database       : fsn_trunk

Target Server Type    : MYSQL
Target Server Version : 50534
File Encoding         : 65001

Date: 2015-01-29 11:41:11
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `licenceno_format`
-- ----------------------------
DROP TABLE IF EXISTS `licenceno_format`;
CREATE TABLE `licenceno_format` (
  `id` int(4) NOT NULL AUTO_INCREMENT,
  `format_name` varchar(36) DEFAULT NULL COMMENT '生产许可证的样式',
  `format_type` varchar(4) DEFAULT NULL COMMENT '生产许可证中间的分隔符',
  `format_value` varchar(8) DEFAULT NULL COMMENT '生产许可证编号的开头部分',
  `format_length` int(4) DEFAULT NULL COMMENT '生产许可证的后面数字的位数长度',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of licenceno_format
-- ----------------------------
INSERT INTO `licenceno_format` VALUES ('1', 'QS(格式：QSxx-xxxxx-xxxxx)', '-', 'QS', '12');
INSERT INTO `licenceno_format` VALUES ('2', 'QS(格式：QSxxxx xxxx xxxx)', ' ', 'QS', '12');
INSERT INTO `licenceno_format` VALUES ('3', 'XK(格式：XKxx-xxx-xxxxx)', '-', 'XK', '10');
INSERT INTO `licenceno_format` VALUES ('4', '(?)XK(格式：(?)XKxx-xxx-xxxxx)', '-', '(?)XK', '10');
