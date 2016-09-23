/*
Navicat MySQL Data Transfer

Source Server         : Web
Source Server Version : 50134
Source Host           : localhost:3306
Source Database       : fsn2.0_160224

Target Server Type    : MYSQL
Target Server Version : 50134
File Encoding         : 65001

Date: 2016-05-17 17:06:53
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
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of trace_data
-- ----------------------------
INSERT INTO `trace_data` VALUES ('21', '12303', '贵州省黔南布依族苗族自治州三都水族自治县建设西路林场宿舍楼下', '三都水族自治县', '20160320000001', '', '', '', null, '茶叶约为3-5克，冲以75℃-85℃开水，浸泡约2分钟后饮用', '18', '', '500g', '91522732MA6DK9PW4F', '500g', '特级', '', '', '[\"http://qc110.com/ytbase/GetImg.ashx?opt=BASEDICT_DICTIMG&blodfiled=PicThing&[KEY]=848&key=Productid&tab=ProductPic&PICNO=1&DbConn=NYYDB\"]', '2016-03-20', '都匀毛尖茶', '[\"2016-03-20@茶青初加工\",\"2016-03-21@干茶精加工\",\"2016-03-23@干茶包装\"]', '', null, 'http://qc110.com?q=94GDC94000NQH', '00000000000123032016032000000003');
