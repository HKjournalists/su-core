/*
Navicat MySQL Data Transfer

Source Server         : 本机数据库
Source Server Version : 50534
Source Host           : localhost:3306
Source Database       : fsn

Target Server Type    : MYSQL
Target Server Version : 50534
File Encoding         : 65001

Date: 2016-05-31 17:08:15
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
  `sourceArea` varchar(255) DEFAULT NULL,
  `sourceCertifyResource` int(255) unsigned DEFAULT NULL,
  `sourceDate` date DEFAULT NULL,
  `processor` varchar(255) DEFAULT NULL,
  `packagePlant` varchar(255) DEFAULT NULL,
  `warehouseDate` date DEFAULT NULL,
  `leaveDate` date DEFAULT NULL,
  `fsnCode` varchar(32) DEFAULT NULL,
  `organization` bigint(20) unsigned NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=32 DEFAULT CHARSET=utf8;
