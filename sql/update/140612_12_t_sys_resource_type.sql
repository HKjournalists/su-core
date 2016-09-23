/*
Navicat MySQL Data Transfer

Source Server         : GETTEC
Source Server Version : 50530
Source Host           : localhost:3306
Source Database       : fsn_core_ceshi

Target Server Type    : MYSQL
Target Server Version : 50530
File Encoding         : 65001

Date: 2014-06-11 17:47:08
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `t_sys_resource_type`
-- ----------------------------
DROP TABLE IF EXISTS `t_sys_resource_type`;
CREATE TABLE `t_sys_resource_type` (
  `RESOURCE_TYPE_ID` bigint(20) NOT NULL,
  `CONTENT_TYPE` varchar(255) DEFAULT NULL,
  `RESOURCE_TYPE_DESC` varchar(255) DEFAULT NULL,
  `RESOURCE_TYPE_NAME` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`RESOURCE_TYPE_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_sys_resource_type
-- ----------------------------
INSERT INTO `t_sys_resource_type` VALUES ('1', 'gif', 'gif', 'image/gif');
INSERT INTO `t_sys_resource_type` VALUES ('2', 'txt', 'txt', 'text/plain');
INSERT INTO `t_sys_resource_type` VALUES ('3', 'pdf', 'pdf', 'application/pdf');
INSERT INTO `t_sys_resource_type` VALUES ('4', 'xls', 'xls', 'application/vnd.ms-excel');
INSERT INTO `t_sys_resource_type` VALUES ('5', 'xlsx', 'xlsx', 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet');
INSERT INTO `t_sys_resource_type` VALUES ('7', 'doc', 'doc', 'application/msword');
INSERT INTO `t_sys_resource_type` VALUES ('8', 'jpg', 'jpg', 'image/jpeg');
INSERT INTO `t_sys_resource_type` VALUES ('9', 'jrxml', 'jrxml', 'application/jrxml');
INSERT INTO `t_sys_resource_type` VALUES ('10', 'jpeg', 'jpeg', 'image/jpeg');
INSERT INTO `t_sys_resource_type` VALUES ('11', 'bmp', 'bmp', 'image/bmp');
INSERT INTO `t_sys_resource_type` VALUES ('12', 'png', 'png', 'image/png');
