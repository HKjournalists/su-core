/*
Navicat MySQL Data Transfer

Source Server         : Web
Source Server Version : 50134
Source Host           : localhost:3306
Source Database       : fsn2.0_160224

Target Server Type    : MYSQL
Target Server Version : 50134
File Encoding         : 65001

Date: 2016-06-14 15:22:34
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `trace_data`
-- ----------------------------
DROP TABLE IF EXISTS `trace_data`;
CREATE TABLE `trace_data` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `productID` bigint(20) DEFAULT NULL,
  `address` varchar(255) DEFAULT NULL COMMENT '企业地址',
  `areaCode` varchar(255) DEFAULT NULL COMMENT '所属区域',
  `batchCode` varchar(255) DEFAULT NULL COMMENT '批号',
  `className` varchar(255) DEFAULT NULL COMMENT '生产班组',
  `degree` varchar(255) DEFAULT NULL COMMENT '白酒度数，制剂规格',
  `departmentID` varchar(255) DEFAULT NULL COMMENT '对方企业ID',
  `departmentName` varchar(255) DEFAULT NULL COMMENT '企业名称',
  `description` varchar(255) DEFAULT NULL COMMENT '产品描述',
  `expireDate` varchar(255) DEFAULT NULL COMMENT '有效期',
  `factoryName` varchar(255) DEFAULT NULL COMMENT '工厂名称',
  `netContent` varchar(255) DEFAULT NULL COMMENT '净含量',
  `orgCode` varchar(255) DEFAULT NULL COMMENT '组织机构代码',
  `packageSpec` varchar(255) DEFAULT NULL COMMENT '包装规格',
  `prodLevel` varchar(255) DEFAULT NULL COMMENT '产品等级',
  `prodLine` varchar(255) DEFAULT NULL COMMENT '生产线',
  `prodMixture` varchar(255) DEFAULT NULL COMMENT '配料',
  `prodPics` text COMMENT '产品图片',
  `productDate` date DEFAULT NULL COMMENT '生产日期',
  `productName` varchar(255) DEFAULT NULL COMMENT '产品名称',
  `timeTrack` text COMMENT '追溯数据',
  `volume` varchar(255) DEFAULT NULL COMMENT '容量',
  `workShop` varchar(255) DEFAULT NULL COMMENT '生产车间',
  `keyWord` varchar(255) DEFAULT NULL,
  `fsnCode` varchar(255) DEFAULT NULL COMMENT '食安码',
  `productorID` bigint(20) DEFAULT NULL,
  `sourceArea` varchar(255) DEFAULT NULL COMMENT '原材料来源区域',
  `sourceCertify` varchar(255) DEFAULT NULL COMMENT '原材料其他证明',
  `sourceDate` date DEFAULT NULL COMMENT '原材料入仓时间',
  `processor` varchar(255) DEFAULT NULL COMMENT '加工者名称',
  `packagePlant` varchar(255) DEFAULT NULL COMMENT '包装厂名称',
  `warehouseDate` date DEFAULT NULL COMMENT '入库时间',
  `leaveDate` date DEFAULT NULL COMMENT '出厂时间',
  `organization` bigint(20) DEFAULT NULL,
  `sourceCertifyResource` int(11) DEFAULT NULL,
  `growEnvironmentResource` int(11) DEFAULT NULL,
  `buyLink` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of trace_data
-- ----------------------------
