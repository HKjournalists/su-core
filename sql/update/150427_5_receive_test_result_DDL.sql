/*
Navicat MySQL Data Transfer

Source Server         : GETTEC
Source Server Version : 50534
Source Host           : 127.0.0.1:3306
Source Database       : fsn_core

Target Server Type    : MYSQL
Target Server Version : 50534
File Encoding         : 65001

Date: 2015-04-27 14:10:16
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `receive_test_result`
-- ----------------------------
DROP TABLE IF EXISTS `receive_test_result`;
CREATE TABLE `receive_test_result` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `receive_id` varchar(50) NOT NULL COMMENT '检测报告ID',
  `device_sn` varchar(50) DEFAULT NULL COMMENT '检测设备序列号',
  `device_name` varchar(50) DEFAULT NULL COMMENT '检测设备名称',
  `username` varchar(30) DEFAULT NULL COMMENT '检测用户名',
  `deptname` varchar(100) NOT NULL COMMENT '检测部门',
  `name` varchar(255) DEFAULT NULL COMMENT '检测样品名称',
  `type` varchar(20) NOT NULL COMMENT '检测样品类型代码',
  `barcode` varchar(100) NOT NULL COMMENT '检测样品条码',
  `manufacturer_id` varchar(50) NOT NULL COMMENT '检测样品生产厂商唯一标识',
  `manufacturer_name` varchar(200) NOT NULL COMMENT '检测样品生产厂商名称',
  `manufacturer_licenseno` varchar(50) NOT NULL COMMENT '检测样品生产厂商营业执照',
  `brand` varchar(50) NOT NULL COMMENT '检测样品商标',
  `spec` varchar(200) NOT NULL COMMENT '检测样品规格',
  `batch` varchar(50) NOT NULL COMMENT '检测样品批号',
  `manufacture_date` datetime NOT NULL COMMENT '检测样品生产日期',
  `test_date` datetime NOT NULL COMMENT '检测时间',
  `retailer_id` varchar(50) NOT NULL COMMENT '测样品零售商唯一标识',
  `retailer_name` varchar(200) NOT NULL COMMENT '检测样品零售商名称',
  `retailer_licenseno` varchar(50) NOT NULL COMMENT '检测样品零售商营业执照号',
  `person_in_charge` varchar(30) DEFAULT NULL COMMENT '检测样品零售商负责人名称',
  `phone_in_charge` varchar(20) DEFAULT NULL COMMENT '检测样品零售商电话',
  `address` varchar(200) NOT NULL COMMENT '检测地点',
  `env` varchar(50) DEFAULT NULL COMMENT '检测环境信息',
  `amount` varchar(100) NOT NULL COMMENT '检测数量',
  `memo` varchar(300) DEFAULT NULL COMMENT '备注',
  `pass` tinyint(4) NOT NULL COMMENT '检测报告合格标记',
  `edition` varchar(20) NOT NULL COMMENT '报告来源标识',
  `attachments` varchar(300) DEFAULT NULL COMMENT '检测样品照片文件名',
  `test_result_id` bigint(20) DEFAULT NULL,
  `test_type` varchar(50) NOT NULL COMMENT '检验类别',
  `deadline` datetime DEFAULT NULL COMMENT '样品过期时间',
  `qs_no` varchar(50) DEFAULT NULL COMMENT '样品生产许可证',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of receive_test_result
-- ----------------------------
