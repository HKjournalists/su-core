/*
Navicat MySQL Data Transfer

Source Server         : GETTEC
Source Server Version : 50534
Source Host           : 127.0.0.1:3306
Source Database       : fsn_core

Target Server Type    : MYSQL
Target Server Version : 50534
File Encoding         : 65001

Date: 2015-04-27 12:18:27
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `receive_bus_key_config`
-- ----------------------------
DROP TABLE IF EXISTS `receive_bus_key_config`;
CREATE TABLE `receive_bus_key_config` (
  `NO` varchar(50) NOT NULL DEFAULT '' COMMENT '企业唯一编码',
  `KEY` varchar(50) NOT NULL COMMENT '签名key',
  `STATUS` tinyint(4) NOT NULL COMMENT '状态',
  `NOTE` varchar(50) DEFAULT NULL COMMENT '备注',
  `BUS_NAME` varchar(200) NOT NULL COMMENT '企业名称',
  `CREATE_DATE` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`NO`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of receive_bus_key_config
-- ----------------------------
INSERT INTO `receive_bus_key_config` VALUES ('T_BoYin_10000000', '7a66bbec1c7928d975db30fb7c878fb0', '0', '', '泊银', '2015-04-27 10:28:28');
