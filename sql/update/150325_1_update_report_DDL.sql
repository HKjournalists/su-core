/*
Navicat MySQL Data Transfer

Source Server         : fsn
Source Server Version : 50534
Source Host           : localhost:3306
Source Database       : fsn_trunk

Target Server Type    : MYSQL
Target Server Version : 50534
File Encoding         : 65001

Date: 2015-03-25 11:43:08
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `update_report`
-- ----------------------------
DROP TABLE IF EXISTS `update_report`;
CREATE TABLE `update_report` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `PRODUCT_ID` bigint(20) DEFAULT NULL COMMENT '产品id',
  `REPORT_TYPE` varchar(16) DEFAULT NULL COMMENT '申请报告类型',
  `APPLY_DATE` date DEFAULT NULL COMMENT '最新申请时间',
  `APPLY_TIMES` bigint(20) DEFAULT NULL COMMENT '申请次数',
  `PRODUCT_BARCODE` varchar(16) DEFAULT NULL COMMENT '产品条形码',
  `PRODUCT_NAME` varchar(32) DEFAULT NULL COMMENT '产品名称',
  `ORGANIZATION` bigint(20) DEFAULT NULL COMMENT '组织机构id',
  `HANDLE_STATUS` int(2) DEFAULT NULL COMMENT '当前处于的状态   0 待处理状态  1 待处理中   2 已处理状态',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8;