/*
Navicat MySQL Data Transfer

Source Server         : lims
Source Server Version : 50534
Source Host           : localhost:3306
Source Database       : stg_backfsn

Target Server Type    : MYSQL
Target Server Version : 50534
File Encoding         : 65001

Date: 2016-09-22 21:15:31
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `facility_to_resource`
-- ----------------------------
DROP TABLE IF EXISTS `facility_to_resource`;
CREATE TABLE `facility_to_resource` (
  `facility_id` bigint(11) DEFAULT NULL COMMENT '设备ID',
  `resource_id` bigint(11) DEFAULT NULL COMMENT '图片ID',
  `create_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of facility_to_resource
-- ----------------------------


alter table facility_info drop RESOURCE_ID;