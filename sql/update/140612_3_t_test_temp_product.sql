/*
Navicat MySQL Data Transfer

Source Server         : GETTEC
Source Server Version : 50530
Source Host           : localhost:3306
Source Database       : fsn_core

Target Server Type    : MYSQL
Target Server Version : 50530
File Encoding         : 65001

Date: 2014-05-30 11:16:22
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `t_test_temp_product`
-- ----------------------------
DROP TABLE IF EXISTS `t_test_temp_product`;
CREATE TABLE `t_test_temp_product` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `BAR_CODE` varchar(100) DEFAULT NULL COMMENT '������',
  `NAME` varchar(255) DEFAULT NULL COMMENT '��Ʒ����',
  `SPECIFICATION` varchar(100) DEFAULT NULL COMMENT '���',
  `MODEL_NO` varchar(100) DEFAULT NULL COMMENT '�ͺ�',
  `BRAND` varchar(100) DEFAULT NULL COMMENT '�̱�',
  `PRO_STATUS` varchar(100) DEFAULT NULL COMMENT '��Ʒ״̬',
  `EXPIRATION` varchar(100) DEFAULT NULL COMMENT '�ʱ���',
  `EXPIR_DAY` varchar(100) DEFAULT NULL COMMENT '�ʱ�����',
  `CATEGORY` varchar(100) DEFAULT NULL COMMENT '��Ʒ���',
  `MIN_UNIT` varchar(50) DEFAULT NULL COMMENT '��λ',
  `STANDARD` varchar(255) DEFAULT NULL COMMENT 'ִ�б�׼',
  `PRO_DATE` datetime DEFAULT NULL,
  `BATCH_NO` varchar(100) DEFAULT NULL,
  `SAM_ADDRESS` varchar(100) DEFAULT NULL,
  `CREAT_USER_REALNAME` varchar(20) NOT NULL COMMENT '����������',
  `LAST_MODIFY_TIME` datetime NOT NULL COMMENT '������ʱ��',
  `ORG_NAME` varchar(100) NOT NULL COMMENT '��֯����',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_test_temp_product
-- ----------------------------
