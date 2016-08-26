/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50534
Source Host           : localhost:3306
Source Database       : fsn_stg_20160810

Target Server Type    : MYSQL
Target Server Version : 50534
File Encoding         : 65001

Date: 2016-08-17 11:07:27
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `basedata`
-- ----------------------------
DROP TABLE IF EXISTS `basedata`;
CREATE TABLE `basedata` (
  `ID` varchar(50) NOT NULL COMMENT 'ID',
  `ZYMC` varchar(100) DEFAULT NULL COMMENT '值域名称',
  `ZYLX` varchar(20) DEFAULT NULL COMMENT '值域类型',
  `ZYLXMC` varchar(50) DEFAULT NULL COMMENT '值域类型名称',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='系统值域基础信息';

-- ----------------------------
-- Records of basedata
-- ----------------------------
INSERT INTO `basedata` VALUES ('10', '董事长', 'zw', '职务');
INSERT INTO `basedata` VALUES ('104', '蒙古族', 'mz', '民族');
INSERT INTO `basedata` VALUES ('105', '回族', 'mz', '民族');
INSERT INTO `basedata` VALUES ('106', '藏族', 'mz', '民族');
INSERT INTO `basedata` VALUES ('107', '苗族', 'mz', '民族');
INSERT INTO `basedata` VALUES ('108', '彝族', 'mz', '民族');
INSERT INTO `basedata` VALUES ('109', '壮族', 'mz', '民族');
INSERT INTO `basedata` VALUES ('11', '副董事长', 'zw', '职务');
INSERT INTO `basedata` VALUES ('110', '布依族', 'mz', '民族');
INSERT INTO `basedata` VALUES ('111', '朝鲜族', 'mz', '民族');
INSERT INTO `basedata` VALUES ('112', '满族', 'mz', '民族');
INSERT INTO `basedata` VALUES ('113', '侗族', 'mz', '民族');
INSERT INTO `basedata` VALUES ('114', '瑶族', 'mz', '民族');
INSERT INTO `basedata` VALUES ('115', '白族', 'mz', '民族');
INSERT INTO `basedata` VALUES ('116', '土家族', 'mz', '民族');
INSERT INTO `basedata` VALUES ('117', '哈尼族', 'mz', '民族');
INSERT INTO `basedata` VALUES ('118', '哈萨克族', 'mz', '民族');
INSERT INTO `basedata` VALUES ('119', '傣族', 'mz', '民族');
INSERT INTO `basedata` VALUES ('12', '董事', 'zw', '职务');
INSERT INTO `basedata` VALUES ('120', '黎族', 'mz', '民族');
INSERT INTO `basedata` VALUES ('121', '傈僳族', 'mz', '民族');
INSERT INTO `basedata` VALUES ('122', '佤族', 'mz', '民族');
INSERT INTO `basedata` VALUES ('123', '畲族', 'mz', '民族');
INSERT INTO `basedata` VALUES ('124', '高山族', 'mz', '民族');
INSERT INTO `basedata` VALUES ('125', '拉祜族', 'mz', '民族');
INSERT INTO `basedata` VALUES ('126', '水族', 'mz', '民族');
INSERT INTO `basedata` VALUES ('127', '东乡族', 'mz', '民族');
INSERT INTO `basedata` VALUES ('128', '纳西族', 'mz', '民族');
INSERT INTO `basedata` VALUES ('129', '景颇族', 'mz', '民族');
INSERT INTO `basedata` VALUES ('13', '执行董事', 'zw', '职务');
INSERT INTO `basedata` VALUES ('130', '柯尔克孜族', 'mz', '民族');
INSERT INTO `basedata` VALUES ('131', '土族', 'mz', '民族');
INSERT INTO `basedata` VALUES ('132', '达斡尔族', 'mz', '民族');
INSERT INTO `basedata` VALUES ('133', '仡佬族', 'mz', '民族');
INSERT INTO `basedata` VALUES ('134', '羌族', 'mz', '民族');
INSERT INTO `basedata` VALUES ('135', '布朗族', 'mz', '民族');
INSERT INTO `basedata` VALUES ('136', '撒拉族', 'mz', '民族');
INSERT INTO `basedata` VALUES ('137', '毛南族', 'mz', '民族');
INSERT INTO `basedata` VALUES ('138', '锡伯族', 'mz', '民族');
INSERT INTO `basedata` VALUES ('139', '阿昌族', 'mz', '民族');
INSERT INTO `basedata` VALUES ('14', '总经理', 'zw', '职务');
INSERT INTO `basedata` VALUES ('140', '普米族', 'mz', '民族');
INSERT INTO `basedata` VALUES ('141', '塔吉克族', 'mz', '民族');
INSERT INTO `basedata` VALUES ('142', '怒族', 'mz', '民族');
INSERT INTO `basedata` VALUES ('143', '乌孜别克族', 'mz', '民族');
INSERT INTO `basedata` VALUES ('144', '俄罗斯族', 'mz', '民族');
INSERT INTO `basedata` VALUES ('145', '鄂温克族', 'mz', '民族');
INSERT INTO `basedata` VALUES ('146', '德昂族', 'mz', '民族');
INSERT INTO `basedata` VALUES ('147', '保安族', 'mz', '民族');
INSERT INTO `basedata` VALUES ('148', '裕固族', 'mz', '民族');
INSERT INTO `basedata` VALUES ('149', '京族', 'mz', '民族');
INSERT INTO `basedata` VALUES ('15', '副总经理', 'zw', '职务');
INSERT INTO `basedata` VALUES ('150', '塔塔尔族', 'mz', '民族');
INSERT INTO `basedata` VALUES ('151', '独龙族', 'mz', '民族');
INSERT INTO `basedata` VALUES ('152', '鄂伦春族', 'mz', '民族');
INSERT INTO `basedata` VALUES ('153', '赫哲族', 'mz', '民族');
INSERT INTO `basedata` VALUES ('154', '门巴族', 'mz', '民族');
INSERT INTO `basedata` VALUES ('155', '珞巴族', 'mz', '民族');
INSERT INTO `basedata` VALUES ('156', '基诺族', 'mz', '民族');
INSERT INTO `basedata` VALUES ('157', '仫佬族', 'mz', '民族');
INSERT INTO `basedata` VALUES ('16', '经理', 'zw', '职务');
INSERT INTO `basedata` VALUES ('17', '负责人', 'zw', '职务');
INSERT INTO `basedata` VALUES ('18', '总务主任', 'zw', '职务');
INSERT INTO `basedata` VALUES ('19', '专职管理员', 'zw', '职务');
INSERT INTO `basedata` VALUES ('20', '其他人员', 'zw', '职务');
INSERT INTO `basedata` VALUES ('21', '中华人民共和国居民身份证', 'zjlx', '证件类型');
INSERT INTO `basedata` VALUES ('210', '外籍', 'mz', '民族');
INSERT INTO `basedata` VALUES ('211', '穿青人', 'mz', '民族');
INSERT INTO `basedata` VALUES ('22', '外国（地区）护照', 'zjlx', '证件类型');
INSERT INTO `basedata` VALUES ('23', '港澳台有效身份证件', 'zjlx', '证件类型');
INSERT INTO `basedata` VALUES ('24', '其他有效身份证件', 'zjlx', '证件类型');
INSERT INTO `basedata` VALUES ('25', '法定代表人（负责人）', 'rylx', '人员类型');
INSERT INTO `basedata` VALUES ('26', '食品安全专业技术人员', 'rylx', '人员类型');
INSERT INTO `basedata` VALUES ('27', '食品安全管理人员', 'rylx', '人员类型');
INSERT INTO `basedata` VALUES ('28', '从业人员', 'rylx', '人员类型');
INSERT INTO `basedata` VALUES ('29', '其他', 'rylx', '人员类型');
INSERT INTO `basedata` VALUES ('7', '董事长兼总经理', 'zw', '职务');
INSERT INTO `basedata` VALUES ('8', '董事兼总经理', 'zw', '职务');
INSERT INTO `basedata` VALUES ('84', '汉族', 'mz', '民族');
INSERT INTO `basedata` VALUES ('86', '维吾尔族', 'mz', '民族');
INSERT INTO `basedata` VALUES ('9', '执行董事兼总经理', 'zw', '职务');
