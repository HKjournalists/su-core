/*
Navicat MySQL Data Transfer

Source Server         : GETTEC
Source Server Version : 50534
Source Host           : localhost:3306
Source Database       : fsn_core

Target Server Type    : MYSQL
Target Server Version : 50534
File Encoding         : 65001

Date: 2014-07-03 11:40:27
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `material_store`
-- ----------------------------
DROP TABLE IF EXISTS `material_store`;
CREATE TABLE `material_store` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `PURCHASES` int(11) DEFAULT NULL COMMENT '购买量',
  `BATCH_NO` varchar(50) NOT NULL COMMENT '批次号',
  `UNIT_PRICE` float(3,0) DEFAULT NULL COMMENT '单价',
  `TOTAL_PRICE` float(10,0) DEFAULT NULL COMMENT '总价',
  `TOTAL_MONEY` float(10,0) DEFAULT NULL COMMENT '总费用',
  `PRICE_UNIT` varchar(20) DEFAULT NULL,
  `COMPANY_NAME` varchar(50) DEFAULT NULL COMMENT '供货商名称',
  `COMPANY_ADDRESS` varchar(100) DEFAULT NULL COMMENT '供货商地址',
  `STORAGE_TIME` datetime DEFAULT NULL COMMENT '入库时间',
  `CREATE_BY_USER` varchar(50) DEFAULT NULL COMMENT '创建者',
  `MATERIAL_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK_material_id_to_materialIs` (`MATERIAL_ID`),
  CONSTRAINT `FK_material_id_to_materialIs` FOREIGN KEY (`MATERIAL_ID`) REFERENCES `material` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of material_store
-- ----------------------------
