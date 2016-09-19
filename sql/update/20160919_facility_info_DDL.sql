/*
Navicat MySQL Data Transfer

Source Server         : lims
Source Server Version : 50534
Source Host           : localhost:3306
Source Database       : stg_backfsn

Target Server Type    : MYSQL
Target Server Version : 50534
File Encoding         : 65001

Date: 2016-09-19 14:26:49
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `facility_info`
-- ----------------------------
DROP TABLE IF EXISTS `facility_info`;
CREATE TABLE `facility_info` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `business_id` bigint(11) NOT NULL COMMENT 'Q企业ID',
  `facility_name` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '名称',
  `manufacturer` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '生产厂家',
  `facility_type` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '型号',
  `facility_count` bigint(11) DEFAULT NULL COMMENT '数量',
  `buying_time` datetime DEFAULT NULL COMMENT '采购时间',
  `application` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '用途',
  `RESOURCE_ID` bigint(11) DEFAULT NULL COMMENT '设备图片id',
  `remark` varchar(1000) COLLATE utf8_bin DEFAULT NULL,
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of facility_info
-- ----------------------------
DROP TABLE IF EXISTS `facility_maintenance_record`;
CREATE TABLE `facility_maintenance_record` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `facility_id` bigint(11) NOT NULL COMMENT '设备信息ID',
  `maintenance_name` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '养护人',
  `maintenance_time` datetime DEFAULT NULL COMMENT '养护时间',
  `maintenance_content` varchar(1000) COLLATE utf8_bin DEFAULT NULL COMMENT '养护内容',
  `remark` varchar(1000) COLLATE utf8_bin DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;


DROP TABLE IF EXISTS `operate_info`;
CREATE TABLE `operate_info` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `business_id` bigint(11) NOT NULL,
  `operate_type` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '经营类型',
  `operate_scope` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '经营规模',
  `person_count` bigint(11) DEFAULT NULL COMMENT '企业人数',
  `floor_area` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '占地面积',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改日期',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;