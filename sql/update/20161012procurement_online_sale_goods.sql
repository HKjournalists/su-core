/*
Navicat MySQL Data Transfer

Source Server         : mysql
Source Server Version : 50550
Source Host           : localhost:3306
Source Database       : stg_backfsn

Target Server Type    : MYSQL
Target Server Version : 50550
File Encoding         : 65001

Date: 2016-10-12 10:44:59
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `procurement_online_sale_goods`
-- ----------------------------
DROP TABLE IF EXISTS `procurement_online_sale_goods`;
CREATE TABLE `procurement_online_sale_goods` (
  `id` bigint(22) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL COMMENT '名称',
  `barcode` varchar(255) DEFAULT NULL COMMENT '条码',
  `unit` varchar(255) DEFAULT NULL COMMENT '单位',
  `batch` varchar(255) DEFAULT NULL COMMENT '批次',
  `procurement_num` int(20) DEFAULT NULL COMMENT '采购数量',
  `procurement_date` datetime DEFAULT NULL COMMENT '采购日期',
  `expire_date` datetime DEFAULT NULL COMMENT '过期日期',
  `surplus_num` int(11) DEFAULT NULL COMMENT '剩余数量',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `food_type` varchar(255) DEFAULT NULL COMMENT '食品分类',
  `production_date` datetime DEFAULT NULL COMMENT '生产日期',
  `expiration` int(11) DEFAULT NULL COMMENT '保质期',
  `production_name` varchar(200) DEFAULT NULL COMMENT '生产企业名称',
  `standard` varchar(200) DEFAULT NULL COMMENT '执行标准',
  `formate` varchar(50) DEFAULT NULL COMMENT '规格',
  `organization_id` bigint(20) DEFAULT NULL COMMENT '所属企业机构id',
  `create_date` datetime DEFAULT NULL COMMENT '创建日期',
  `creator` varchar(20) DEFAULT NULL COMMENT '创建者',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of procurement_online_sale_goods
-- ----------------------------

-- ----------------------------
-- Table structure for `procurement_online_sale_goods_dispose`
-- ----------------------------
DROP TABLE IF EXISTS `procurement_online_sale_goods_dispose`;
CREATE TABLE `procurement_online_sale_goods_dispose` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `online_sale_id` bigint(20) DEFAULT NULL COMMENT '在线销售商品id',
  `online_sale_name` varchar(200) DEFAULT NULL COMMENT '在线销售商品名称',
  `format` varchar(50) DEFAULT NULL COMMENT '规格',
  `batch` varchar(50) DEFAULT NULL COMMENT '批次',
  `dispose_num` int(11) DEFAULT '0' COMMENT '处理数量',
  `dispose_date` datetime DEFAULT NULL COMMENT '处理时间',
  `dispose_cause` varchar(200) DEFAULT NULL COMMENT '处理原因',
  `dispose_place` varchar(200) DEFAULT NULL COMMENT '处理地点',
  `handler` varchar(50) NOT NULL COMMENT '处理人',
  `dispose_method` varchar(200) DEFAULT NULL COMMENT '处理方式',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `create_date` datetime DEFAULT NULL COMMENT '创建日期',
  `creator` varchar(20) DEFAULT NULL COMMENT '创建者',
  `organization_id` bigint(20) DEFAULT NULL COMMENT '所属企业机构id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of procurement_online_sale_goods_dispose
-- ----------------------------

-- ----------------------------
-- Table structure for `procurement_online_sale_goods_dispose_to_source`
-- ----------------------------
DROP TABLE IF EXISTS `procurement_online_sale_goods_dispose_to_source`;
CREATE TABLE `procurement_online_sale_goods_dispose_to_source` (
  `online_dispose_id` bigint(20) NOT NULL,
  `RESOURCE_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`online_dispose_id`,`RESOURCE_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of procurement_online_sale_goods_dispose_to_source
-- ----------------------------

-- ----------------------------
-- Table structure for `procurement_online_sale_goods_to_source`
-- ----------------------------
DROP TABLE IF EXISTS `procurement_online_sale_goods_to_source`;
CREATE TABLE `procurement_online_sale_goods_to_source` (
  `online_id` bigint(20) NOT NULL,
  `RESOURCE_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`online_id`,`RESOURCE_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of procurement_online_sale_goods_to_source
-- ----------------------------

-- ----------------------------
-- Table structure for `procurement_sale_record`
-- ----------------------------
DROP TABLE IF EXISTS `procurement_sale_record`;
CREATE TABLE `procurement_sale_record` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `online_sale_id` bigint(20) DEFAULT NULL COMMENT '在售商品ID',
  `online_sale_name` varchar(200) DEFAULT NULL COMMENT '在售商品名称',
  `sale_date` datetime DEFAULT NULL COMMENT '销售时间',
  `sale_num` int(20) DEFAULT NULL COMMENT '销售数量',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `creator` varchar(50) DEFAULT NULL COMMENT '销售者',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of procurement_sale_record
-- ----------------------------
