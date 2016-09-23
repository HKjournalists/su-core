/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50534
Source Host           : localhost:3306
Source Database       : fsn_stg_20160912

Target Server Type    : MYSQL
Target Server Version : 50534
File Encoding         : 65001

Date: 2016-09-18 09:05:26
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `procurement_info`
-- ----------------------------
DROP TABLE IF EXISTS `procurement_info`;
CREATE TABLE `procurement_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(200) DEFAULT NULL COMMENT '名称',
  `provider_id` bigint(20) DEFAULT NULL COMMENT '供应商企业id',
  `provider_name` varchar(200) DEFAULT NULL COMMENT '供应商名称',
  `format` varchar(50) DEFAULT NULL COMMENT '规格',
  `batch` varchar(50) DEFAULT NULL COMMENT '批次',
  `procurement_num` int(20) DEFAULT '0' COMMENT '采购数量',
  `procurement_date` datetime DEFAULT NULL COMMENT '采购日期',
  `expire_date` datetime DEFAULT NULL COMMENT '过期日期',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `surplus_num` int(11) DEFAULT '0' COMMENT '剩余数量',
  `type` int(11) DEFAULT '0' COMMENT '采购类型   1：原辅料  2：添加剂  3：包装材料',
  `create_date` datetime DEFAULT NULL COMMENT '创建日期',
  `creator` varchar(20) DEFAULT NULL COMMENT '创建者',
  `organization_id` bigint(20) DEFAULT NULL COMMENT '所属企业机构id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of procurement_info
-- ----------------------------

DROP TABLE IF EXISTS `procurement_dispose`;
CREATE TABLE `procurement_dispose` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `procurement_id` bigint(20) DEFAULT NULL COMMENT '采购信息id',
  `procurement_name` varchar(200) DEFAULT NULL COMMENT '采购名称',
  `format` varchar(50) DEFAULT NULL COMMENT '规格',
  `batch` varchar(50) DEFAULT NULL COMMENT '批次',
  `dispose_num` int(11) DEFAULT '0' COMMENT '处理数量',
  `dispose_date` datetime DEFAULT NULL COMMENT '处理时间',
  `dispose_cause` varchar(200) DEFAULT NULL COMMENT '处理原因',
  `dispose_place` varchar(200) DEFAULT NULL COMMENT '处理地点',
  `handler` varchar(50) DEFAULT NULL COMMENT '处理人',
  `dispose_method` varchar(200) DEFAULT NULL COMMENT '处理方式',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `type` int(11) DEFAULT '0' COMMENT '处理类型  1：原辅料  2：添加剂',
  `create_date` datetime DEFAULT NULL COMMENT '创建日期',
  `creator` varchar(20) DEFAULT NULL COMMENT '创建者',
  `organization_id` bigint(20) DEFAULT NULL COMMENT '所属企业机构id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `procurement_usage_record`;
CREATE TABLE `procurement_usage_record` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `procurement_id` bigint(20) DEFAULT NULL COMMENT '采购信息表id',
  `procurement_name` varchar(200) DEFAULT NULL COMMENT '采购名称',
  `use_date` date DEFAULT NULL COMMENT '使用日期',
  `use_num` int(11) DEFAULT '0' COMMENT '使用数量',
  `purpose` varchar(100) DEFAULT NULL COMMENT '用途',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `creator` varchar(50) DEFAULT NULL COMMENT '创建者',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS `procurement_info_to_resource`;
CREATE TABLE `procurement_info_to_resource` (
  `procurement_id` bigint(20) NOT NULL,
  `RESOURCE_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`procurement_id`,`RESOURCE_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS `procurement_dispose_to_source`;
CREATE TABLE `procurement_dispose_to_source` (
  `dispose_id` bigint(20) NOT NULL,
  `RESOURCE_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`dispose_id`,`RESOURCE_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;