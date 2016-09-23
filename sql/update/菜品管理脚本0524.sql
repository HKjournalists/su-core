/*
Navicat MySQL Data Transfer

Source Server         : nicp_server
Source Server Version : 50535
Source Host           : 192.168.0.8:3306
Source Database       : fsn-ht

Target Server Type    : MYSQL
Target Server Version : 50535
File Encoding         : 65001

Date: 2016-05-24 09:30:25
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `dishs_no`
-- ----------------------------
DROP TABLE IF EXISTS `dishs_no`;
CREATE TABLE `dishs_no` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `dishs_name` varchar(100) DEFAULT NULL,
  `alias` varchar(100) DEFAULT NULL,
  `baching` varchar(100) DEFAULT NULL,
  `qiyeId` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of dishs_no
-- ----------------------------
INSERT INTO `dishs_no` VALUES ('1', '2', '3', '4', null);
INSERT INTO `dishs_no` VALUES ('3', '1', '2', '123123', null);
INSERT INTO `dishs_no` VALUES ('4', '好菜', '菜', '就哈哈加粉快静安寺大家放开数据啊领导就昂克拉dasfl1接口\\(^o^)/YES! 很快就就爱上了饭卡里就亏大了可接口的拉拉裤', null);

-- ----------------------------
-- Table structure for `dishsno_to_resource`
-- ----------------------------
DROP TABLE IF EXISTS `dishsno_to_resource`;
CREATE TABLE `dishsno_to_resource` (
  `dishsno_id` bigint(20) NOT NULL,
  `resource_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of dishsno_to_resource
-- ----------------------------
INSERT INTO `dishsno_to_resource` VALUES ('3', '373282');
INSERT INTO `dishsno_to_resource` VALUES ('4', '373323');
