/*
Navicat MySQL Data Transfer

Source Server         : GETTEC
Source Server Version : 50530
Source Host           : localhost:3306
Source Database       : fsn_core

Target Server Type    : MYSQL
Target Server Version : 50530
File Encoding         : 65001

Date: 2014-05-30 11:16:02
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `t_test_temp_busunit`
-- ----------------------------
DROP TABLE IF EXISTS `t_test_temp_busunit`;
CREATE TABLE `t_test_temp_busunit` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `LICENSE_NO` varchar(100) DEFAULT NULL COMMENT 'Ӫҵִ�պ�',
  `QS_NO` varchar(100) DEFAULT NULL COMMENT 'QS֤��',
  `NAME` varchar(255) DEFAULT NULL COMMENT '������ҵ����',
  `ADDRESS` varchar(255) DEFAULT NULL COMMENT '������ҵ��ַ',
  `CREAT_USER_REALNAME` varchar(20) NOT NULL COMMENT '����������',
  `LAST_MODIFY_TIME` datetime NOT NULL COMMENT '������ʱ��',
  `ORG_NAME` varchar(100) NOT NULL COMMENT '��֯����',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_test_temp_busunit
-- ----------------------------
