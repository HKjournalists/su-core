/*
Navicat MySQL Data Transfer

Source Server         : GETTEC
Source Server Version : 50534
Source Host           : 127.0.0.1:3306
Source Database       : ceshi_fsn_core

Target Server Type    : MYSQL
Target Server Version : 50534
File Encoding         : 65001

Date: 2014-09-10 09:28:02
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `license_info`
-- ----------------------------
DROP TABLE IF EXISTS `license_info`;
CREATE TABLE `license_info` (
  `license_no` varchar(50) NOT NULL COMMENT '营业执照注册号',
  `license_name` varchar(200) DEFAULT NULL COMMENT '经营主体名称',
  `legal_name` varchar(100) DEFAULT NULL COMMENT '法人代表(负责人)',
  `start_time` datetime DEFAULT NULL COMMENT '有效期: 开始时间',
  `end_time` datetime DEFAULT NULL COMMENT '有效期: 结束时间',
  `registration_time` datetime DEFAULT NULL COMMENT '注册时间',
  `issuing_authority` varchar(200) DEFAULT NULL COMMENT '发照机关',
  `subject_type` varchar(100) DEFAULT NULL COMMENT '主体类型',
  `business_address` varchar(200) DEFAULT NULL COMMENT '经营场所(企业地址)',
  `tolerance_range` varchar(200) DEFAULT NULL COMMENT '许可范围',
  `registered_capital` varchar(100) DEFAULT NULL COMMENT '注册资金',
  PRIMARY KEY (`license_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of license_info
-- ----------------------------
