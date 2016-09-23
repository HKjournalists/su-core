/*
Navicat MySQL Data Transfer

Source Server         : mysql
Source Server Version : 50550
Source Host           : localhost:3306
Source Database       : stg_backfsn

Target Server Type    : MYSQL
Target Server Version : 50550
File Encoding         : 65001

Date: 2016-07-29 10:15:39
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `public_deal`
-- ----------------------------
DROP TABLE IF EXISTS `public_deal`;
CREATE TABLE `public_deal` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '序号',
  `province` varchar(40) DEFAULT NULL COMMENT '省',
  `city` varchar(40) DEFAULT NULL COMMENT '市',
  `counties` varchar(40) DEFAULT NULL COMMENT '区/县',
  `product_name` varchar(255) DEFAULT NULL COMMENT '产品名称',
  `business_name` varchar(200) DEFAULT NULL COMMENT '企业名称',
  `license_no` varchar(50) DEFAULT NULL COMMENT '营业执照',
  `barcode` varchar(100) DEFAULT NULL COMMENT '条形码',
  `product_time` datetime DEFAULT NULL COMMENT '产品生产日期',
  `problem_type` int(10) DEFAULT NULL COMMENT '问题类型',
  `create_time` datetime DEFAULT NULL COMMENT '用户问题发现时间',
  `deal_time` datetime DEFAULT NULL COMMENT '企业处理时间',
  `request_deal_time` date DEFAULT NULL COMMENT '监管要求处理完成时间',
  `origin` varchar(20) DEFAULT NULL COMMENT '信息来源:监管/食安测/终端',
  `info_resource_address` varchar(100) DEFAULT NULL COMMENT '信息来源地址定位:经纬度',
  `deal_status` int(2) DEFAULT NULL COMMENT '企业处理：已处理/未处理',
  `commit_status` int(3) DEFAULT NULL COMMENT '管理员：提交企业/提交监管/忽略',
  `complain_status` int(3) DEFAULT NULL COMMENT '投诉处理/新增投诉(1)/已提交投诉(2)/忽略(3)',
  `remark` varchar(1000) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of public_deal
-- ----------------------------
