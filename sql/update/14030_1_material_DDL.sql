/*
Navicat MySQL Data Transfer

Source Server         : GETTEC
Source Server Version : 50534
Source Host           : localhost:3306
Source Database       : fsn_core

Target Server Type    : MYSQL
Target Server Version : 50534
File Encoding         : 65001

Date: 2014-06-30 17:37:03
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `material`
-- ----------------------------
DROP TABLE IF EXISTS `material`;
CREATE TABLE `material` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(100) NOT NULL COMMENT '材料名称',
  `MATERIAL_NO` varchar(100) NOT NULL COMMENT '唯一标识号',
  `FORMATE` varchar(100) DEFAULT NULL COMMENT '规格型号',
  `INVENTORY_UNIT` varchar(50) DEFAULT NULL COMMENT '库存单位',
  `EXPIRATION` varchar(100) DEFAULT NULL COMMENT '保质期',
  `EXPIRATION_COUNT` int(10) DEFAULT NULL COMMENT '保质天数',
  `INVENTORY_ALARM_COUNT` int(10) DEFAULT NULL COMMENT '库存报警值',
  `INVENTORY_COUNT` varchar(100) DEFAULT NULL COMMENT '库存数量',
  `ORGNIZATION` bigint(20) NOT NULL COMMENT '增加原材料的生产企业的组织机构id',
  `LAST_MODIFY_TIME` datetime NOT NULL COMMENT '最后更新时间',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of material
-- ----------------------------
