/*
Navicat MySQL Data Transfer

Source Server         : GETTEC
Source Server Version : 50534
Source Host           : 127.0.0.1:3306
Source Database       : fsn_core

Target Server Type    : MYSQL
Target Server Version : 50534
File Encoding         : 65001

Date: 2014-09-29 09:33:11
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `fields`
-- ----------------------------
DROP TABLE IF EXISTS `fields`;
CREATE TABLE `fields` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `field_name` varchar(30) DEFAULT NULL,
  `display` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=38 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of fields
-- ----------------------------
INSERT INTO `fields` VALUES ('1', 'directorPhone', '主任电话');
INSERT INTO `fields` VALUES ('2', 'coversArea', '企业占地面积（平方米）');
INSERT INTO `fields` VALUES ('3', 'overallFloorage', '总建筑面积（平方米）');
INSERT INTO `fields` VALUES ('4', 'wineShopNumber', '制酒车间数（个）');
INSERT INTO `fields` VALUES ('5', 'wineWorkShopArea', '制酒车间建筑面积（平方米）');
INSERT INTO `fields` VALUES ('6', 'kojiShopNumber', '制曲车间数（个）');
INSERT INTO `fields` VALUES ('7', 'kojiWorkShopArea', '制曲车间建筑面积（平方米）');
INSERT INTO `fields` VALUES ('8', 'packageShopNumber', '包装车间数（个）');
INSERT INTO `fields` VALUES ('9', 'packageWorkShopArea', '包装车间建筑面积（平方米）');
INSERT INTO `fields` VALUES ('10', 'wineLibraryNumber', '酒库数（栋）');
INSERT INTO `fields` VALUES ('11', 'wineLibraryArea', '酒库建筑面积（平方米）');
INSERT INTO `fields` VALUES ('12', 'officeBuildArea', '办公楼建筑面积（平方米）');
INSERT INTO `fields` VALUES ('13', 'dormitoryBuildArea', '职工宿舍建筑面积（平方米）');
INSERT INTO `fields` VALUES ('14', 'otherBuildArea', '其他用房建筑面积（平方米）');
INSERT INTO `fields` VALUES ('15', 'actualTotalAssets', '实际资产总额（万元）');
INSERT INTO `fields` VALUES ('16', 'boiler', '锅炉（台）');
INSERT INTO `fields` VALUES ('17', 'travelCrane', '行车（台）');
INSERT INTO `fields` VALUES ('18', 'employeeNumber', '企业总人数（人）');
INSERT INTO `fields` VALUES ('19', 'managementNumber', '管理人员总数量（人）');
INSERT INTO `fields` VALUES ('20', 'winemakersNumber', '酿酒师人数');
INSERT INTO `fields` VALUES ('21', 'blendDivisionNumber', '勾调师人数');
INSERT INTO `fields` VALUES ('22', 'salesPersonNumber', '营销人员数');
INSERT INTO `fields` VALUES ('23', 'researchersNumber', '科研人员数');
INSERT INTO `fields` VALUES ('24', 'oterPersonNumber', '其他人员数');
INSERT INTO `fields` VALUES ('25', 'daquProductCapacityNum', '生产能力（吨）大曲酱香酒');
INSERT INTO `fields` VALUES ('26', 'suishaProductCapacityNum', '生产能力（吨）碎沙酱香酒');
INSERT INTO `fields` VALUES ('27', 'fanshaProductCapacityNum', '生产能力（吨）翻沙酱香酒');
INSERT INTO `fields` VALUES ('28', 'nongxiangProductCapacityNum', '生产能力（吨）浓香酒');
INSERT INTO `fields` VALUES ('29', 'daquStockNum', '库存白酒量（吨）大曲酱香酒');
INSERT INTO `fields` VALUES ('30', 'suisshfanshaStockNum', '库存白酒量（吨）碎沙、翻沙酒');
INSERT INTO `fields` VALUES ('31', 'otherStockNum', '库存白酒量（吨）其他酒');
INSERT INTO `fields` VALUES ('32', 'majorShareholder', '主要股东');
INSERT INTO `fields` VALUES ('33', '2013hasBuidNumber', '窖池（口）2013建成数');
INSERT INTO `fields` VALUES ('34', '2013canUseNumber', '窖池（口）2013年底投产数');
INSERT INTO `fields` VALUES ('35', '2014isBuidingNumber', '窖池（口）2014在建窖池数');
INSERT INTO `fields` VALUES ('36', '2014planUseNumber', '窖池（口）2014年计划投产数');
INSERT INTO `fields` VALUES ('37', 'officeTelephone', '办公电话');
