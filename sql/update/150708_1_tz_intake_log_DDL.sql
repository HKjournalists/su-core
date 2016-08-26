/*
Navicat MySQL Data Transfer

Source Server         : fsn-core
Source Server Version : 50622
Source Host           : localhost:3306
Source Database       : stg_fsn_20150701

Target Server Type    : MYSQL
Target Server Version : 50622
File Encoding         : 65001

Date: 2015-07-07 11:49:19
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `tz_intake_log`
-- ----------------------------
DROP TABLE IF EXISTS `tz_intake_log`;
CREATE TABLE `tz_intake_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `bus_id` bigint(20) DEFAULT NULL COMMENT '企业id',
  `product_id` bigint(20) DEFAULT NULL COMMENT '产品id',
  `ops_date` varchar(50) DEFAULT NULL COMMENT '操作时间',
  `operator` varchar(80) DEFAULT NULL COMMENT '操作者',
  `pos_count` int(16) DEFAULT NULL COMMENT '操作数量',
  `stock_id` bigint(20) DEFAULT NULL COMMENT '库存id',
  `stockinfo_id` bigint(20) DEFAULT NULL COMMENT '库存详情id',
  `UUID` varchar(50) DEFAULT NULL COMMENT 'UUID 唯一性',
  PRIMARY KEY (`id`),
  KEY `index_business_id` (`bus_id`) USING BTREE,
  KEY `index_product_id` (`product_id`) USING BTREE,
  KEY `index_stock_id` (`stock_id`) USING BTREE,
  KEY `index_stockinfo_id` (`stockinfo_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tz_intake_log
-- ----------------------------
