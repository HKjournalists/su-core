/*
Navicat MySQL Data Transfer

Source Server         : GETTEC
Source Server Version : 50534
Source Host           : 127.0.0.1:3306
Source Database       : ceshi_fsn_core

Target Server Type    : MYSQL
Target Server Version : 50534
File Encoding         : 65001

Date: 2014-09-10 09:27:50
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `organizing_institution`
-- ----------------------------
DROP TABLE IF EXISTS `organizing_institution`;
CREATE TABLE `organizing_institution` (
  `org_code` varchar(100) NOT NULL COMMENT '组织机构代码',
  `org_name` varchar(200) DEFAULT NULL COMMENT '组织机构名称',
  `start_time` datetime DEFAULT NULL COMMENT '有效期: 开始时间',
  `end_time` datetime DEFAULT NULL COMMENT '有效期: 结束时间',
  `units_awarded` varchar(200) DEFAULT NULL COMMENT '颁发单位',
  `org_type` varchar(100) DEFAULT NULL COMMENT '组织机构类型',
  `org_address` varchar(200) DEFAULT NULL COMMENT '组织机构地址',
  PRIMARY KEY (`org_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of organizing_institution
-- ----------------------------
