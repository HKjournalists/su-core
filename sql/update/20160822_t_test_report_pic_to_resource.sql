/*
Navicat MySQL Data Transfer

Source Server         : 本机数据库
Source Server Version : 50534
Source Host           : localhost:3306
Source Database       : fsn

Target Server Type    : MYSQL
Target Server Version : 50534
File Encoding         : 65001

Date: 2016-08-24 22:19:39
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `t_test_result_buy_to_resource`
-- ----------------------------
DROP TABLE IF EXISTS `t_test_result_buy_to_resource`;
CREATE TABLE `t_test_result_buy_to_resource` (
  `TEST_RESULT_ID` bigint(20) NOT NULL COMMENT '第三方报告表ID',
  `RESOURCE_ID` bigint(20) NOT NULL COMMENT '资源ID',
  PRIMARY KEY (`RESOURCE_ID`,`TEST_RESULT_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_test_result_buy_to_resource
-- ----------------------------
INSERT INTO `t_test_result_buy_to_resource` VALUES ('153008', '419562');
INSERT INTO `t_test_result_buy_to_resource` VALUES ('153001', '419598');

-- ----------------------------
-- Table structure for `t_test_result_check_to_resource`
-- ----------------------------
DROP TABLE IF EXISTS `t_test_result_check_to_resource`;
CREATE TABLE `t_test_result_check_to_resource` (
  `TEST_RESULT_ID` bigint(20) NOT NULL COMMENT '第三方报告表ID',
  `RESOURCE_ID` bigint(20) NOT NULL COMMENT '资源ID',
  PRIMARY KEY (`RESOURCE_ID`,`TEST_RESULT_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_test_result_check_to_resource
-- ----------------------------
INSERT INTO `t_test_result_check_to_resource` VALUES ('153008', '419563');
INSERT INTO `t_test_result_check_to_resource` VALUES ('153001', '419599');

-- ----------------------------
-- Table structure for `t_test_result_pic_to_resource`
-- ----------------------------
DROP TABLE IF EXISTS `t_test_result_pic_to_resource`;
CREATE TABLE `t_test_result_pic_to_resource` (
  `TEST_RESULT_ID` bigint(20) NOT NULL COMMENT '第三方报告表ID',
  `RESOURCE_ID` bigint(20) NOT NULL COMMENT '资源ID',
  PRIMARY KEY (`RESOURCE_ID`,`TEST_RESULT_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_test_result_pic_to_resource
-- ----------------------------
INSERT INTO `t_test_result_pic_to_resource` VALUES ('153008', '419564');
INSERT INTO `t_test_result_pic_to_resource` VALUES ('153001', '419600');
