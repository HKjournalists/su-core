/*
Navicat MySQL Data Transfer

Source Server         : lims
Source Server Version : 50534
Source Host           : localhost:3306
Source Database       : stg_backfsn

Target Server Type    : MYSQL
Target Server Version : 50534
File Encoding         : 65001

Date: 2016-11-02 11:25:21

此表只针对餐饮企业所设计
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `business_unit_to_catering`
-- ----------------------------
DROP TABLE IF EXISTS `business_unit_to_catering`;
CREATE TABLE `business_unit_to_catering` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `business_id` bigint(11) NOT NULL AUTO_INCREMENT COMMENT '企业ID',
  `consume` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '人均消费',
  `telephone` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '订餐电话',
  `store_type` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '店铺类型',
  `longitude` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '经度',
  `latitude` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '纬度',
  `placeName` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '所在位置',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of business_unit_to_catering
-- ----------------------------


/**
为菜品列表添加相关信息字段
 */
ALTER TABLE dishs_no  ADD COLUMN price DOUBLE(9,2) DEFAULT NULL DEFAULT '0' ;
ALTER TABLE dishs_no  ADD COLUMN about VARCHAR(1000) NULL DEFAULT NULL ;
ALTER TABLE dishs_no  ADD COLUMN characteristic VARCHAR(255) NULL DEFAULT NULL ;