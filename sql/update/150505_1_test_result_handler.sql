/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50534
Source Host           : localhost:3306
Source Database       : fsn_new

Target Server Type    : MYSQL
Target Server Version : 50534
File Encoding         : 65001

Date: 2015-05-05 17:45:49
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `test_result_handler`
-- ----------------------------
DROP TABLE IF EXISTS `test_result_handler`;
CREATE TABLE `test_result_handler` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `test_result_id` bigint(20) NOT NULL COMMENT '报告ID与test_result表关联',
  `handler` varchar(100) DEFAULT NULL COMMENT '报告结构化处理人员',
  `creation_time` datetime DEFAULT NULL COMMENT '创建时间',
  `completion_time` datetime DEFAULT NULL COMMENT '完成时间',
  `status` char(1) DEFAULT NULL COMMENT '报告状态',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of test_result_handler
-- ----------------------------
