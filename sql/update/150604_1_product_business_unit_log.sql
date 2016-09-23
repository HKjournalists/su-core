/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50534
Source Host           : localhost:3306
Source Database       : fsn_pro_20150601_1

Target Server Type    : MYSQL
Target Server Version : 50534
File Encoding         : 65001

Date: 2015-06-04 10:56:47
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `product_log`
-- ----------------------------
DROP TABLE IF EXISTS `product_log`;
CREATE TABLE `product_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `product_id` bigint(20) DEFAULT NULL COMMENT '产品ID',
  `product_name` varchar(200) DEFAULT NULL COMMENT '产品名称',
  `barcode` varchar(100) DEFAULT NULL COMMENT '条形码',
  `handlers` varchar(100) DEFAULT NULL COMMENT '操作者',
  `operation` varchar(255) DEFAULT NULL COMMENT '所做操作',
  `operation_time` datetime DEFAULT NULL COMMENT '操作时间',
  `operation_data` longtext COMMENT '操作数据',
  `errorMessage` longtext COMMENT '错误消息',
  `handlersIP` varchar(100) DEFAULT NULL COMMENT '操作者IP地址',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of product_log
-- ----------------------------

DROP TABLE IF EXISTS `business_unit_info_log`;
CREATE TABLE `business_unit_info_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `business_unit_id` bigint(20) DEFAULT NULL COMMENT '企业id',
  `business_unit_name` varchar(255) DEFAULT NULL COMMENT '企业名称',
  `handlers` varchar(100) DEFAULT NULL COMMENT '操作者',
  `operation` varchar(255) DEFAULT NULL COMMENT '所做操作',
  `operation_time` datetime DEFAULT NULL COMMENT '操作时间',
  `operation_data` text COMMENT '操作数据',
  `errorMessage` text COMMENT '错误消息',
  `handlersIP` varchar(100) DEFAULT NULL COMMENT '操作者IP地址',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;