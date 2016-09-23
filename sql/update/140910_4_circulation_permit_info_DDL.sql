/*
Navicat MySQL Data Transfer

Source Server         : GETTEC
Source Server Version : 50534
Source Host           : 127.0.0.1:3306
Source Database       : fsn_core

Target Server Type    : MYSQL
Target Server Version : 50534
File Encoding         : 65001

Date: 2014-09-10 15:23:50
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `circulation_permit_info`
-- ----------------------------
DROP TABLE IF EXISTS `circulation_permit_info`;
CREATE TABLE `circulation_permit_info` (
  `distribution_no` varchar(50) NOT NULL COMMENT '食品流通许可证编号',
  `licensing_authority` varchar(200) DEFAULT NULL COMMENT '许可机关',
  `license_name` varchar(200) DEFAULT NULL COMMENT '许可证名称',
  `start_time` datetime DEFAULT NULL COMMENT '有效期: 开始时间',
  `end_time` datetime DEFAULT NULL COMMENT '有效期: 结束时间',
  `subject_type` varchar(100) DEFAULT NULL COMMENT '主体类型',
  `business_address` varchar(200) DEFAULT NULL COMMENT '经营场所',
  `tolerance_range` varchar(200) DEFAULT NULL COMMENT '许可范围',
  `legal_name` varchar(100) DEFAULT NULL COMMENT '负责人',
  PRIMARY KEY (`distribution_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of circulation_permit_info
-- ----------------------------
