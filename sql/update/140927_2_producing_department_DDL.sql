/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50530
Source Host           : localhost:3306
Source Database       : fsn_cloud

Target Server Type    : MYSQL
Target Server Version : 50530
File Encoding         : 65001

Date: 2014-09-27 21:14:53
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `producing_department`
-- ----------------------------
DROP TABLE IF EXISTS `producing_department`;
CREATE TABLE `producing_department` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) DEFAULT NULL COMMENT '名称',
  `legal_name` varchar(100) DEFAULT NULL COMMENT '法定代表人',
  `address` varchar(200) DEFAULT NULL COMMENT '地址',
  `telephone` varchar(20) DEFAULT NULL COMMENT '联系电话',
  `in_commission_number` int(10) DEFAULT NULL COMMENT '投产窖池数',
  `year` varchar(10) DEFAULT NULL COMMENT '年限',
  `department_flga` bit(1) DEFAULT NULL COMMENT '车间标志 为1时是生产车间，为0时是认可的‘挂靠’酒厂',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of producing_department
-- ----------------------------
