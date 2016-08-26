/*
Navicat MySQL Data Transfer

Source Server         : local
Source Server Version : 50534
Source Host           : localhost:3306
Source Database       : fsn_core

Target Server Type    : MYSQL
Target Server Version : 50534
File Encoding         : 65001

Date: 2014-11-27 17:47:14
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `sys_dict`
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict`;
CREATE TABLE `sys_dict` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `label` varchar(100) COLLATE utf8_unicode_ci NOT NULL COMMENT '标签名',
  `value` varchar(100) COLLATE utf8_unicode_ci NOT NULL COMMENT '数据值',
  `type` varchar(100) COLLATE utf8_unicode_ci NOT NULL COMMENT '类型',
  `description` varchar(100) COLLATE utf8_unicode_ci NOT NULL COMMENT '描述',
  `sort` int(11) NOT NULL COMMENT '排序（升序）',
  PRIMARY KEY (`id`),
  KEY `sys_dict_value` (`value`),
  KEY `sys_dict_label` (`label`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='字典表';

-- ----------------------------
-- Records of sys_dict
-- ----------------------------
INSERT INTO `sys_dict` VALUES ('1', '综合农贸市场', '1', 'location_type', '场所类型', '10');
INSERT INTO `sys_dict` VALUES ('2', '批发市场', '2', 'location_type', '场所类型', '20');
INSERT INTO `sys_dict` VALUES ('3', '商场', '3', 'location_type', '场所类型', '30');
INSERT INTO `sys_dict` VALUES ('4', '超市', '4', 'location_type', '场所类型', '40');
INSERT INTO `sys_dict` VALUES ('5', '城市', '1', 'region_type', '地域类型', '10');
INSERT INTO `sys_dict` VALUES ('6', '农村', '2', 'region_type', '地域类型', '20');
INSERT INTO `sys_dict` VALUES ('7', '城乡结合', '3', 'region_type', '地域类型', '30');
INSERT INTO `sys_dict` VALUES ('8', '省会城市', '1', 'area_type', '地区类型', '10');
INSERT INTO `sys_dict` VALUES ('9', '地市', '2', 'area_type', '地区类型', '20');
INSERT INTO `sys_dict` VALUES ('10', '区县', '3', 'area_type', '地区类型', '30');
INSERT INTO `sys_dict` VALUES ('11', '正常营业', '1', 'business_status', '营业状态', '10');
INSERT INTO `sys_dict` VALUES ('12', '未正常营业', '2', 'business_status', '营业状态', '20');
INSERT INTO `sys_dict` VALUES ('13', '校园周边', '1', 'other_property', '其他性质', '10');
INSERT INTO `sys_dict` VALUES ('14', '旅游景区', '2', 'other_property', '其他性质', '20');
INSERT INTO `sys_dict` VALUES ('15', '民族地区', '3', 'other_property', '其他性质', '30');
INSERT INTO `sys_dict` VALUES ('16', '是否公路沿线', '4', 'other_property', '其他性质', '40');
INSERT INTO `sys_dict` VALUES ('17', '是否火车站周边', '5', 'other_property', '其他性质', '50');
INSERT INTO `sys_dict` VALUES ('18', '是否机场周边', '6', 'other_property', '其他性质', '60');
INSERT INTO `sys_dict` VALUES ('19', '是否汽车客运站周边', '7', 'other_property', '其他性质', '70');
INSERT INTO `sys_dict` VALUES ('20', '其他性质', '8', 'other_property', '其他性质', '80');
INSERT INTO `sys_dict` VALUES ('21', '是否食品安全示范店（省级）', '1', 'business_unit_case', '经营主体情况', '10');
INSERT INTO `sys_dict` VALUES ('22', '是否食品安全示范店（市级）', '2', 'business_unit_case', '经营主体情况', '20');
INSERT INTO `sys_dict` VALUES ('23', '是否食品安全示范店（县级）', '3', 'business_unit_case', '经营主体情况', '30');
INSERT INTO `sys_dict` VALUES ('24', '是否食品流通行业协会会员）', '4', 'business_unit_case', '经营主体情况', '40');
INSERT INTO `sys_dict` VALUES ('25', '是否执行\'易票通\'', '5', 'business_unit_case', '经营主体情况', '50');
INSERT INTO `sys_dict` VALUES ('26', '是否执行索票索证', '6', 'business_unit_case', '经营主体情况', '60');
INSERT INTO `sys_dict` VALUES ('27', '是否简历电子台账', '7', 'business_unit_case', '经营主体情况', '70');
INSERT INTO `sys_dict` VALUES ('28', '是否直接从生产企业进货（省内）', '8', 'business_unit_case', '经营主体情况', '80');
INSERT INTO `sys_dict` VALUES ('29', '是否直接从生产企业进货（省外）', '9', 'business_unit_case', '经营主体情况', '90');
INSERT INTO `sys_dict` VALUES ('30', '是否直接从省外流通环节食品经营主体进货', '10', 'business_unit_case', '经营主体情况', '93');
INSERT INTO `sys_dict` VALUES ('31', '其他情况', '11', 'business_unit_case', '经营主体情况', '96');
INSERT INTO `sys_dict` VALUES ('32', '是否经营乳制品（含因幼儿配方乳粉）', '1', 'business_focus_food', '经营重点食品情况', '10');
INSERT INTO `sys_dict` VALUES ('33', '是否经营乳制品（不含因幼儿配方乳粉）', '2', 'business_focus_food', '经营重点食品情况', '20');
INSERT INTO `sys_dict` VALUES ('34', '是否经营儿童食品', '3', 'business_focus_food', '经营重点食品情况', '30');
INSERT INTO `sys_dict` VALUES ('35', '是否经营专供特定人群主辅食品(中老年食品)', '4', 'business_focus_food', '经营重点食品情况', '40');
INSERT INTO `sys_dict` VALUES ('36', '是否经营专供特定人群主辅食品(婴幼儿食品)', '5', 'business_focus_food', '经营重点食品情况', '50');
INSERT INTO `sys_dict` VALUES ('37', '是否经营专供特定人群主辅食品(其他食品)', '6', 'business_focus_food', '经营重点食品情况', '60');
INSERT INTO `sys_dict` VALUES ('38', '是否经营食品添加剂', '7', 'business_focus_food', '经营重点食品情况', '70');
INSERT INTO `sys_dict` VALUES ('39', '是否经营食用油（预包装）', '8', 'business_focus_food', '经营重点食品情况', '80');
INSERT INTO `sys_dict` VALUES ('40', '是否经营食用油（散装）', '9', 'business_focus_food', '经营重点食品情况', '90');
INSERT INTO `sys_dict` VALUES ('41', '是否经营白酒（预包装）', '10', 'business_focus_food', '经营重点食品情况', '92');
INSERT INTO `sys_dict` VALUES ('42', '是否经营白酒（散装）', '11', 'business_focus_food', '经营重点食品情况', '94');
INSERT INTO `sys_dict` VALUES ('43', '是否经营清蒸食品', '12', 'business_focus_food', '经营重点食品情况', '95');
INSERT INTO `sys_dict` VALUES ('44', '其他食品', '12', 'business_focus_food', '经营重点食品情况', '96');
INSERT INTO `sys_dict` VALUES ('50', '新成立', '1', 'permit_license_change', '换证情况', '10');
INSERT INTO `sys_dict` VALUES ('51', '到期更换', '2', 'permit_license_change', '换证情况', '20');
INSERT INTO `sys_dict` VALUES ('53', '未到期自愿更换', '3', 'permit_license_change', '换证情况', '30');
INSERT INTO `sys_dict` VALUES ('60', '综合农贸市场入场经营户', '1', 'mechant_type', '主体性质', '10');
INSERT INTO `sys_dict` VALUES ('61', '批发市场入场经营户', '2', 'mechant_type', '主体性质', '20');
INSERT INTO `sys_dict` VALUES ('62', '商场', '3', 'mechant_type', '主体性质', '30');
INSERT INTO `sys_dict` VALUES ('63', '超市', '4', 'mechant_type', '主体性质', '40');
INSERT INTO `sys_dict` VALUES ('64', '批发经营户', '5', 'mechant_type', '主体性质', '50');
INSERT INTO `sys_dict` VALUES ('65', '食杂店（专卖店）', '6', 'mechant_type', '主体性质', '60');
INSERT INTO `sys_dict` VALUES ('66', '食杂店（非专卖店）', '7', 'mechant_type', '主体性质', '70');
INSERT INTO `sys_dict` VALUES ('67', '餐饮', '8', 'mechant_type', '主体性质', '80');
INSERT INTO `sys_dict` VALUES ('68', '网吧', '9', 'mechant_type', '主体性质', '90');
INSERT INTO `sys_dict` VALUES ('69', '其他', '10', 'mechant_type', '主体性质', '95');
INSERT INTO `sys_dict` VALUES ('70', '组织机构（主体性质）', '1', 'statistics_type', '统计类型', '10');
INSERT INTO `sys_dict` VALUES ('71', '组织机构（主体性质）', '2', 'statistics_type', '统计类型', '0');
INSERT INTO `sys_dict` VALUES ('72', '流通许可证', '1', 'license_type', '许可证类型', '0');
INSERT INTO `sys_dict` VALUES ('73', '卫生许可证', '2', 'license_type', '许可证类型', '20');
INSERT INTO `sys_dict` VALUES ('74', '其他', '3', 'license_type', '许可证类型', '30');
INSERT INTO `sys_dict` VALUES ('75', '省本级', '3', 'area_type', '地区类型', '40');
