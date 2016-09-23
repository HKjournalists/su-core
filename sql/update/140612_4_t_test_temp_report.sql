/*
Navicat MySQL Data Transfer

Source Server         : GETTEC
Source Server Version : 50530
Source Host           : localhost:3306
Source Database       : fsn_core

Target Server Type    : MYSQL
Target Server Version : 50530
File Encoding         : 65001

Date: 2014-05-30 11:16:31
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `t_test_temp_report`
-- ----------------------------
DROP TABLE IF EXISTS `t_test_temp_report`;
CREATE TABLE `t_test_temp_report` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `REPORT_NO` varchar(100) DEFAULT NULL COMMENT '������',
  `TESTEE_NAME` varchar(255) DEFAULT NULL COMMENT '�ܼ�ⵥλ/������',
  `TEST_ORGNIZ_NAME` varchar(255) DEFAULT NULL COMMENT '�����������',
  `TEST_TYPE` varchar(100) DEFAULT NULL COMMENT '�������',
  `CONCLUSION` varchar(100) DEFAULT NULL COMMENT '�������',
  `TEST_PLACE` varchar(100) DEFAULT NULL COMMENT '����ص�',
  `TEST_DATE` datetime DEFAULT NULL COMMENT '��������',
  `SAM_COUNT` varchar(100) DEFAULT NULL COMMENT '������',
  `JUDGE_STANDARD` varchar(255) DEFAULT NULL COMMENT 'ִ�б�׼',
  `RESULT_DESCRIBE` varchar(255) DEFAULT NULL COMMENT '�����������',
  `REMARK` varchar(255) DEFAULT NULL COMMENT '��ע',
  `PRO_DAARESS` varchar(255) DEFAULT NULL COMMENT '����',
  `CREAT_USER_REALNAME` varchar(20) NOT NULL COMMENT '����������',
  `LAST_MODIFY_TIME` datetime NOT NULL COMMENT '������ʱ��',
  `ORG_NAME` varchar(100) NOT NULL COMMENT '��֯����',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_test_temp_report
-- ----------------------------
