/*
Navicat MySQL Data Transfer

Source Server         : lims
Source Server Version : 50534
Source Host           : localhost:3306
Source Database       : stg_backfsn

Target Server Type    : MYSQL
Target Server Version : 50534
File Encoding         : 65001

Date: 2016-08-02 14:34:20
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `deal_to_problem`
-- ----------------------------
DROP TABLE IF EXISTS `deal_to_problem`;
CREATE TABLE `deal_to_problem` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `deal_id` bigint(20) NOT NULL COMMENT 'deal_problem的ID',
  `business_id` bigint(20) NOT NULL COMMENT '企业ID',
  `product_name` varchar(255) DEFAULT NULL COMMENT '产品名称',
  `license_no` varchar(200) DEFAULT NULL COMMENT '营业执照号',
  `business_name` varchar(200) DEFAULT NULL COMMENT '企业名称',
  `barcode` varchar(100) DEFAULT NULL COMMENT '条形码',
  `problem_type` tinyint(3) DEFAULT '0' COMMENT '问题类型',
  `production_date` datetime DEFAULT NULL COMMENT '生产日期',
  `create_time` datetime DEFAULT NULL COMMENT '用户问题发现时间',
  `deal_time` datetime DEFAULT NULL COMMENT '企业处理时间',
  `finish_time` datetime DEFAULT NULL COMMENT '企业处理完成时间',
  `deal_status` tinyint(3) DEFAULT '0' COMMENT '企业处理：已处理/未处理',
  `commit_status` tinyint(3) DEFAULT '0' COMMENT '管理员：提交企业/提交监管/忽略',
  `remark` varchar(1000) DEFAULT NULL COMMENT '备注',
  `problem_code` varchar(50) DEFAULT NULL COMMENT '发现问题的行政区域代码',
  `longitude` double DEFAULT '0' COMMENT '经度',
  `latitude` double DEFAULT '0' COMMENT '纬度',
  `address` varchar(255) DEFAULT NULL COMMENT '发现问题的地址',
  `info_source` varchar(20) DEFAULT NULL COMMENT '信息来源',
  `info_status` tinyint(3) DEFAULT '0' COMMENT '信息状态',
  `backup` varchar(255) DEFAULT NULL COMMENT '备用位',
  `operator` varchar(255) DEFAULT NULL COMMENT '处理人',
  `opunit` varchar(255) DEFAULT NULL COMMENT '处理单位',
  `scode` varchar(255) DEFAULT NULL COMMENT '问题流水号',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of deal_to_problem
-- ----------------------------
INSERT INTO `deal_to_problem` VALUES ('1', '21', '42698', '苹果', '8965482454512', null, '693123456789', '0', null, null, null, null, '0', '0', '而且为而为为而为而且为', null, '0', '0', null, null, '0', null, null, null, null);
