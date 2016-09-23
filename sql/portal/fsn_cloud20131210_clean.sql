/*
Navicat MySQL Data Transfer

Source Server         : 本地
Source Server Version : 50530
Source Host           : localhost:3306
Source Database       : fsn_cloud

Target Server Type    : MYSQL
Target Server Version : 50530
File Encoding         : 65001

Date: 2013-12-10 16:24:50
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `business_brand`
-- ----------------------------
DROP TABLE IF EXISTS `business_brand`;
CREATE TABLE `business_brand` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `identity` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `symbol` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `logo` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `trademark` varchar(10) COLLATE utf8_unicode_ci DEFAULT NULL,
  `cobrand` bit(1) DEFAULT NULL,
  `registration_date` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL,
  `business_unit_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_name` (`name`),
  UNIQUE KEY `idx_identity` (`identity`),
  KEY `fk_business_brand_business_unit` (`business_unit_id`),
  CONSTRAINT `fk_business_brand_business_unit` FOREIGN KEY (`business_unit_id`) REFERENCES `business_unit` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10093 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Records of business_brand
-- ----------------------------
INSERT INTO `business_brand` VALUES ('1', '未知商标', '未知商标', null, null, null, '', null, '1');

-- ----------------------------
-- Table structure for `business_category`
-- ----------------------------
DROP TABLE IF EXISTS `business_category`;
CREATE TABLE `business_category` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `code` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL,
  `name` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_code` (`code`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Records of business_category
-- ----------------------------

-- ----------------------------
-- Table structure for `business_type`
-- ----------------------------
DROP TABLE IF EXISTS `business_type`;
CREATE TABLE `business_type` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `code` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL,
  `name` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_code` (`code`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Records of business_type
-- ----------------------------

-- ----------------------------
-- Table structure for `business_unit`
-- ----------------------------
DROP TABLE IF EXISTS `business_unit`;
CREATE TABLE `business_unit` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `address` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `address2` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `logo` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `website` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `type` bigint(20) DEFAULT NULL,
  `category` bigint(20) DEFAULT NULL,
  `license_no` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `contact` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `note` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `qs_no` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_name` (`name`),
  KEY `fk_business_unit_business_category` (`category`),
  KEY `fk_business_unit_business_type` (`type`),
  CONSTRAINT `fk_business_unit_business_category` FOREIGN KEY (`category`) REFERENCES `product_category` (`id`),
  CONSTRAINT `fk_business_unit_business_type` FOREIGN KEY (`type`) REFERENCES `business_type` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10101 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Records of business_unit
-- ----------------------------
INSERT INTO `business_unit` VALUES ('1', '未知企业', null, null, null, null, null, null, null, null, null, null);

-- ----------------------------
-- Table structure for `business_unit_test`
-- ----------------------------
DROP TABLE IF EXISTS `business_unit_test`;
CREATE TABLE `business_unit_test` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `business_unit_id` bigint(20) DEFAULT NULL,
  `test_cycle_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_business_unit_test_business_unit` (`business_unit_id`),
  KEY `fk_business_unit_test_test_cycle` (`test_cycle_id`),
  CONSTRAINT `fk_business_unit_test_business_unit` FOREIGN KEY (`business_unit_id`) REFERENCES `business_unit` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Records of business_unit_test
-- ----------------------------

-- ----------------------------
-- Table structure for `certification`
-- ----------------------------
DROP TABLE IF EXISTS `certification`;
CREATE TABLE `certification` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `imgurl` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '认证的图片url',
  `message` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '认证信息简介',
  `name` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '认证名称',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=33 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Records of certification
-- ----------------------------
INSERT INTO `certification` VALUES ('1', 'http://211.151.134.74:8080/portal/img/certification/128/3C.jpg', null, '3C认证');
INSERT INTO `certification` VALUES ('2', 'http://211.151.134.74:8080/portal/img/certification/128/3environment.png', null, 'Ⅲ型环境标志');
INSERT INTO `certification` VALUES ('3', 'http://211.151.134.74:8080/portal/img/certification/128/CE.jpg', null, 'CE');
INSERT INTO `certification` VALUES ('4', 'http://211.151.134.74:8080/portal/img/certification/128/FCC.jpg', null, 'FCC');
INSERT INTO `certification` VALUES ('5', 'http://211.151.134.74:8080/portal/img/certification/128/ISO90012000.jpg', null, 'ISO9001 2000');
INSERT INTO `certification` VALUES ('6', 'http://211.151.134.74:8080/portal/img/certification/128/ISO9001.png', null, 'ISO9001');
INSERT INTO `certification` VALUES ('7', 'http://211.151.134.74:8080/portal/img/certification/128/ISO14000.png', null, 'ISO14000体系认证');
INSERT INTO `certification` VALUES ('8', 'http://211.151.134.74:8080/portal/img/certification/128/ISO22000.png', null, 'ISO22000食品安全管理体系');
INSERT INTO `certification` VALUES ('9', 'http://211.151.134.74:8080/portal/img/certification/128/baojianshipin.jpg', null, '保健食品');
INSERT INTO `certification` VALUES ('10', 'http://211.151.134.74:8080/portal/img/certification/128/guojiamianjianchanpin.jpg', null, '国家免检产品');
INSERT INTO `certification` VALUES ('11', 'http://211.151.134.74:8080/portal/img/certification/128/huaxiarenzheng.jpg', null, '华夏认证');
INSERT INTO `certification` VALUES ('12', 'http://211.151.134.74:8080/portal/img/certification/128/lvseshipin.jpg', null, '绿色食品');
INSERT INTO `certification` VALUES ('13', 'http://211.151.134.74:8080/portal/img/certification/128/QS.jpg', null, '生产许可');
INSERT INTO `certification` VALUES ('14', 'http://211.151.134.74:8080/portal/img/certification/128/sudongshipin.jpg', null, '速冻食品');
INSERT INTO `certification` VALUES ('15', 'http://211.151.134.74:8080/portal/img/certification/128/wugonghainongchanpin.png', null, '无公害农产品');
INSERT INTO `certification` VALUES ('16', 'http://211.151.134.74:8080/portal/img/certification/128/wugonghainongchanpinjiu.png', null, '无公害农产品 旧');
INSERT INTO `certification` VALUES ('17', 'http://211.151.134.74:8080/portal/img/certification/128/xunhuanzaishengbiaozhi.jpg', null, '循环再生标志');
INSERT INTO `certification` VALUES ('18', 'http://211.151.134.74:8080/portal/img/certification/128/youjishipin.jpg', null, '有机食品');
INSERT INTO `certification` VALUES ('19', 'http://211.151.134.74:8080/portal/img/certification/128/zhongguohuanjingbiaozhi.png', null, '中国环境标志');
INSERT INTO `certification` VALUES ('20', 'http://211.151.134.74:8080/portal/img/certification/128/zhongguohuanjingguanlitixirenzhengjigouguojiarenzheng.png', null, '中国环境管理体系认证机构国家认可');
INSERT INTO `certification` VALUES ('21', 'http://211.151.134.74:8080/portal/img/certification/128/zhongguomingpai.jpg', null, '中国名牌');
INSERT INTO `certification` VALUES ('22', 'http://211.151.134.74:8080/portal/img/certification/128/zhonghuarenmingongheguoyuanchandiyuchanpin.jpg', null, '中华人民共和国原产地域产品');
INSERT INTO `certification` VALUES ('23', 'http://211.151.134.74:8080/portal/img/certification/128/unknow.jpg', null, 'unknow');
INSERT INTO `certification` VALUES ('24', 'http://211.151.134.74:8080/portal/img/certification/128/ISO9001CQC.jpg', null, 'ISO9001 2008');
INSERT INTO `certification` VALUES ('25', 'http://211.151.134.74:8080/portal/img/certification/128/ISO14001SGS.jpg', null, 'ISO14001 2004环境管理体系');
INSERT INTO `certification` VALUES ('26', 'http://211.151.134.74:8080/portal/img/certification/128/OHSAS18001SGS.jpg', null, 'OHSAS18001 2007');
INSERT INTO `certification` VALUES ('27', null, null, '国家标准化良好行为AAA级企业认证');
INSERT INTO `certification` VALUES ('28', 'http://211.151.134.74:8080/portal/img/certification/128/ISO9001CQC.jpg', null, 'ISO9001 2008质量管理体系');
INSERT INTO `certification` VALUES ('29', null, null, 'ISO 22000 2005食品安全管理体系认证');
INSERT INTO `certification` VALUES ('30', null, null, '安全生产标准化三级企业认证');
INSERT INTO `certification` VALUES ('31', 'http://211.151.134.74:8080/portal/img/certification/128/ISO10012.jpg', null, 'ISO10012');
INSERT INTO `certification` VALUES ('32', 'http://211.151.134.74:8080/portal/img/certification/128/OHSAS18001SGS.jpg', null, 'ISO18001');

-- ----------------------------
-- Table structure for `fda`
-- ----------------------------
DROP TABLE IF EXISTS `fda`;
CREATE TABLE `fda` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `address` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `code` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `parent_fda_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_fda_code` (`code`),
  KEY `fk_parent_fda` (`parent_fda_id`),
  CONSTRAINT `fk_parent_fda` FOREIGN KEY (`parent_fda_id`) REFERENCES `fda` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=36 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Records of fda
-- ----------------------------

-- ----------------------------
-- Table structure for `fda_dependency`
-- ----------------------------
DROP TABLE IF EXISTS `fda_dependency`;
CREATE TABLE `fda_dependency` (
  `FDA_id` bigint(20) NOT NULL,
  `DEPENDENCY_FDA_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`FDA_id`,`DEPENDENCY_FDA_ID`),
  KEY `FK__fda_dependency` (`DEPENDENCY_FDA_ID`),
  CONSTRAINT `FK_ssd__fda_dependency` FOREIGN KEY (`FDA_id`) REFERENCES `fda` (`id`),
  CONSTRAINT `FK__fda_dependency` FOREIGN KEY (`DEPENDENCY_FDA_ID`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Records of fda_dependency
-- ----------------------------

-- ----------------------------
-- Table structure for `fda_product_group`
-- ----------------------------
DROP TABLE IF EXISTS `fda_product_group`;
CREATE TABLE `fda_product_group` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `code` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL,
  `name` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_code` (`code`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Records of fda_product_group
-- ----------------------------

-- ----------------------------
-- Table structure for `fda_statement`
-- ----------------------------
DROP TABLE IF EXISTS `fda_statement`;
CREATE TABLE `fda_statement` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `publish_date` datetime DEFAULT NULL,
  `content` varchar(2000) COLLATE utf8_unicode_ci DEFAULT NULL,
  `product_instance_id` bigint(20) DEFAULT NULL,
  `fda_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_fda_statement_product_instance` (`product_instance_id`),
  KEY `fk_fda_statement_fda` (`fda_id`),
  CONSTRAINT `fk_fda_statement_fda` FOREIGN KEY (`fda_id`) REFERENCES `fda` (`id`),
  CONSTRAINT `fk_fda_statement_product_instance` FOREIGN KEY (`product_instance_id`) REFERENCES `product_instance` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Records of fda_statement
-- ----------------------------

-- ----------------------------
-- Table structure for `fda_test`
-- ----------------------------
DROP TABLE IF EXISTS `fda_test`;
CREATE TABLE `fda_test` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `fda_id` bigint(20) DEFAULT NULL,
  `test_cycle_id` bigint(20) DEFAULT NULL,
  `comment` varchar(500) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_fda_test_fda` (`fda_id`),
  KEY `fk_fda_test_test_cycle` (`test_cycle_id`),
  CONSTRAINT `fk_fda_test_fda` FOREIGN KEY (`fda_id`) REFERENCES `fda` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Records of fda_test
-- ----------------------------

-- ----------------------------
-- Table structure for `fda_test_plan`
-- ----------------------------
DROP TABLE IF EXISTS `fda_test_plan`;
CREATE TABLE `fda_test_plan` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `comment` varchar(2000) COLLATE utf8_unicode_ci DEFAULT NULL,
  `start_date` date DEFAULT NULL,
  `end_date` date DEFAULT NULL,
  `fda_id` bigint(20) DEFAULT NULL,
  `plan_no` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `sampling_district` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `sampling_quantity` int(11) DEFAULT NULL,
  `quarter` varchar(10) COLLATE utf8_unicode_ci DEFAULT NULL,
  `create_date` datetime DEFAULT NULL,
  `testee_id` bigint(20) DEFAULT NULL,
  `producer_id` bigint(20) DEFAULT NULL,
  `sample_id` bigint(20) DEFAULT NULL,
  `product_group_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `plan_no` (`plan_no`),
  KEY `fk_fda_test_plan_fda` (`fda_id`),
  KEY `fk_fda_test_plan_business_unit_testee` (`testee_id`),
  KEY `fk_fda_test_plan_business_unit_producer` (`producer_id`),
  KEY `fk_fda_test_plan_product_instnace` (`sample_id`),
  KEY `fk_fda_test_plan_product_group` (`product_group_id`),
  CONSTRAINT `fk_fda_test_plan_business_unit_producer` FOREIGN KEY (`producer_id`) REFERENCES `business_unit` (`id`),
  CONSTRAINT `fk_fda_test_plan_business_unit_testee` FOREIGN KEY (`testee_id`) REFERENCES `business_unit` (`id`),
  CONSTRAINT `fk_fda_test_plan_fda` FOREIGN KEY (`fda_id`) REFERENCES `fda` (`id`),
  CONSTRAINT `fk_fda_test_plan_product_group` FOREIGN KEY (`product_group_id`) REFERENCES `fda_product_group` (`id`),
  CONSTRAINT `fk_fda_test_plan_product_instnace` FOREIGN KEY (`sample_id`) REFERENCES `product_instance` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Records of fda_test_plan
-- ----------------------------

-- ----------------------------
-- Table structure for `foodcat`
-- ----------------------------
DROP TABLE IF EXISTS `foodcat`;
CREATE TABLE `foodcat` (
  `FdCatID` varchar(10) COLLATE utf8_unicode_ci NOT NULL,
  `FdUpCatID` varchar(10) COLLATE utf8_unicode_ci DEFAULT NULL,
  `FdCatName` varchar(30) COLLATE utf8_unicode_ci NOT NULL,
  `FdCatMth` varchar(15) COLLATE utf8_unicode_ci NOT NULL,
  `FdTopCatID` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `FdTopCatName` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `FdUpCatName` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`FdCatID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Records of foodcat
-- ----------------------------
INSERT INTO `foodcat` VALUES ('01', '', '乳及乳制品', 'GB2760-2011', '01', '乳及乳制品', '');
INSERT INTO `foodcat` VALUES ('0101', '01', '巴氏杀菌乳、灭菌乳和调制乳', 'GB2760-2011', '01', '乳及乳制品', '乳及乳制品');
INSERT INTO `foodcat` VALUES ('010101', '0101', '巴氏杀菌乳', 'GB2760-2011', '01', '乳及乳制品', '巴氏杀菌乳、灭菌乳和调制乳');
INSERT INTO `foodcat` VALUES ('010102', '0101', '灭菌乳', 'GB2760-2011', '01', '乳及乳制品', '巴氏杀菌乳、灭菌乳和调制乳');
INSERT INTO `foodcat` VALUES ('010103', '0101', '调制乳', 'GB2760-2011', '01', '乳及乳制品', '巴氏杀菌乳、灭菌乳和调制乳');
INSERT INTO `foodcat` VALUES ('0102', '01', '发酵乳和风味发酵乳', 'GB2760-2011', '01', '乳及乳制品', '乳及乳制品');
INSERT INTO `foodcat` VALUES ('010201', '0102', '发酵乳', 'GB2760-2011', '01', '乳及乳制品', '发酵乳和风味发酵乳');
INSERT INTO `foodcat` VALUES ('010202', '0102', '风味发酵乳', 'GB2760-2011', '01', '乳及乳制品', '发酵乳和风味发酵乳');
INSERT INTO `foodcat` VALUES ('0103', '01', '乳粉和奶油粉及其调制产品', 'GB2760-2011', '01', '乳及乳制品', '乳及乳制品');
INSERT INTO `foodcat` VALUES ('010301', '0103', '乳粉和奶油粉', 'GB2760-2011', '01', '乳及乳制品', '乳粉和奶油粉及其调制产品');
INSERT INTO `foodcat` VALUES ('010302', '0103', '调制乳粉和调制奶油粉', 'GB2760-2011', '01', '乳及乳制品', '乳粉和奶油粉及其调制产品');
INSERT INTO `foodcat` VALUES ('0104', '01', '炼乳及其调制产品', 'GB2760-2011', '01', '乳及乳制品', '乳及乳制品');
INSERT INTO `foodcat` VALUES ('010401', '0104', '淡炼乳', 'GB2760-2011', '01', '乳及乳制品', '炼乳及其调制产品');
INSERT INTO `foodcat` VALUES ('010402', '0104', '调制炼乳', 'GB2760-2011', '01', '乳及乳制品', '炼乳及其调制产品');
INSERT INTO `foodcat` VALUES ('0105', '01', '稀奶油及其类似品', 'GB2760-2011', '01', '乳及乳制品', '乳及乳制品');
INSERT INTO `foodcat` VALUES ('010501', '0105', '稀奶油', 'GB2760-2011', '01', '乳及乳制品', '稀奶油及其类似品');
INSERT INTO `foodcat` VALUES ('010502', '0105', '凝固稀奶油', 'GB2760-2011', '01', '乳及乳制品', '稀奶油及其类似品');
INSERT INTO `foodcat` VALUES ('010503', '0105', '调味稀奶油', 'GB2760-2011', '01', '乳及乳制品', '稀奶油及其类似品');
INSERT INTO `foodcat` VALUES ('010504', '0105', '稀奶油类似品', 'GB2760-2011', '01', '乳及乳制品', '稀奶油及其类似品');
INSERT INTO `foodcat` VALUES ('0106', '01', '干酪', 'GB2760-2011', '01', '乳及乳制品', '乳及乳制品');
INSERT INTO `foodcat` VALUES ('010601', '0106', '非熟化干酪', 'GB2760-2011', '01', '乳及乳制品', '干酪');
INSERT INTO `foodcat` VALUES ('010602', '0106', '熟化干酪', 'GB2760-2011', '01', '乳及乳制品', '干酪');
INSERT INTO `foodcat` VALUES ('010603', '0106', '乳清干酪', 'GB2760-2011', '01', '乳及乳制品', '干酪');
INSERT INTO `foodcat` VALUES ('010604', '0106', '再制干酪', 'GB2760-2011', '01', '乳及乳制品', '干酪');
INSERT INTO `foodcat` VALUES ('01060401', '010604', '普通再制干酪', 'GB2760-2011', '01', '乳及乳制品', '再制干酪');
INSERT INTO `foodcat` VALUES ('01060402', '010604', '调味再制干酪', 'GB2760-2011', '01', '乳及乳制品', '再制干酪');
INSERT INTO `foodcat` VALUES ('010605', '0106', '干酪类似品', 'GB2760-2011', '01', '乳及乳制品', '干酪');
INSERT INTO `foodcat` VALUES ('010606', '0106', '乳清蛋白干酪', 'GB2760-2011', '01', '乳及乳制品', '干酪');
INSERT INTO `foodcat` VALUES ('0107', '01', '以乳为主要配料的即食风味甜点或其预制产品', 'GB2760-2011', '01', '乳及乳制品', '乳及乳制品');
INSERT INTO `foodcat` VALUES ('0108', '01', '其他乳制品', 'GB2760-2011', '01', '乳及乳制品', '乳及乳制品');
INSERT INTO `foodcat` VALUES ('02', '', '脂肪，油和乳化脂肪制品', 'GB2760-2011', '02', '脂肪，油和乳化脂肪制品', '');
INSERT INTO `foodcat` VALUES ('0201', '02', '基本不含水的脂肪和油', 'GB2760-2011', '02', '脂肪，油和乳化脂肪制品', '脂肪，油和乳化脂肪制品');
INSERT INTO `foodcat` VALUES ('020101', '0201', '植物油脂', 'GB2760-2011', '02', '脂肪，油和乳化脂肪制品', '基本不含水的脂肪和油');
INSERT INTO `foodcat` VALUES ('02010101', '020101', '植物油', 'GB2760-2011', '02', '脂肪，油和乳化脂肪制品', '植物油脂');
INSERT INTO `foodcat` VALUES ('02010102', '020101', '氢化植物油', 'GB2760-2011', '02', '脂肪，油和乳化脂肪制品', '植物油脂');
INSERT INTO `foodcat` VALUES ('020102', '0201', '动物油脂', 'GB2760-2011', '02', '脂肪，油和乳化脂肪制品', '基本不含水的脂肪和油');
INSERT INTO `foodcat` VALUES ('020103', '0201', '无水黄油，无水乳脂', 'GB2760-2011', '02', '脂肪，油和乳化脂肪制品', '基本不含水的脂肪和油');
INSERT INTO `foodcat` VALUES ('0202', '02', '水油状脂肪乳化制品', 'GB2760-2011', '02', '脂肪，油和乳化脂肪制品', '脂肪，油和乳化脂肪制品');
INSERT INTO `foodcat` VALUES ('020201', '0202', '脂肪含量80%以上的乳化制品', 'GB2760-2011', '02', '脂肪，油和乳化脂肪制品', '水油状脂肪乳化制品');
INSERT INTO `foodcat` VALUES ('02020101', '020201', '黄油和浓缩黄油', 'GB2760-2011', '02', '脂肪，油和乳化脂肪制品', '水油状脂肪乳化制品');
INSERT INTO `foodcat` VALUES ('02020102', '020201', '人造黄油及其类似制品', 'GB2760-2011', '02', '脂肪，油和乳化脂肪制品', '水油状脂肪乳化制品');
INSERT INTO `foodcat` VALUES ('020202', '0202', '脂肪含量80%以下的乳化制品', 'GB2760-2011', '02', '脂肪，油和乳化脂肪制品', '水油状脂肪乳化制品');
INSERT INTO `foodcat` VALUES ('0203', '02', '其他脂肪乳化制品', 'GB2760-2011', '02', '脂肪，油和乳化脂肪制品', '脂肪，油和乳化脂肪制品');
INSERT INTO `foodcat` VALUES ('0204', '02', '脂肪类甜品', 'GB2760-2011', '02', '脂肪，油和乳化脂肪制品', '脂肪，油和乳化脂肪制品');
INSERT INTO `foodcat` VALUES ('0205', '02', '其他油脂或油脂制品', 'GB2760-2011', '02', '脂肪，油和乳化脂肪制品', '脂肪，油和乳化脂肪制品');
INSERT INTO `foodcat` VALUES ('03', '', '冷冻饮品', 'GB2760-2011', '03', '冷冻饮品', '');
INSERT INTO `foodcat` VALUES ('0301', '03', '冰淇淋类、雪糕类', 'GB2760-2011', '03', '冷冻饮品', '冷冻饮品');
INSERT INTO `foodcat` VALUES ('0302', '03', '____', 'GB2760-2011', '03', '冷冻饮品', '冷冻饮品');
INSERT INTO `foodcat` VALUES ('0303', '03', '风味冰、冰棍类', 'GB2760-2011', '03', '冷冻饮品', '冷冻饮品');
INSERT INTO `foodcat` VALUES ('0304', '03', '食用冰', 'GB2760-2011', '03', '冷冻饮品', '冷冻饮品');
INSERT INTO `foodcat` VALUES ('0305', '03', '其他冷冻饮品', 'GB2760-2011', '03', '冷冻饮品', '冷冻饮品');
INSERT INTO `foodcat` VALUES ('04', '', '水果、蔬菜、豆类、食用菌、藻类、坚果以及籽类等', 'GB2760-2011', '04', '水果、蔬菜、豆类、食用菌、藻类、坚果以及籽类等', '');
INSERT INTO `foodcat` VALUES ('0401', '04', '水果', 'GB2760-2011', '04', '水果、蔬菜、豆类、食用菌、藻类、坚果以及籽类等', '水果、蔬菜、豆类、食用菌、藻类、坚果以及籽类等');
INSERT INTO `foodcat` VALUES ('040101', '0401', '新鲜水果', 'GB2760-2011', '04', '水果、蔬菜、豆类、食用菌、藻类、坚果以及籽类等', '水果');
INSERT INTO `foodcat` VALUES ('04010101', '040101', '未经加工的鲜果', 'GB2760-2011', '04', '水果、蔬菜、豆类、食用菌、藻类、坚果以及籽类等', '新鲜水果');
INSERT INTO `foodcat` VALUES ('04010102', '040101', '经表面处理的鲜水果', 'GB2760-2011', '04', '水果、蔬菜、豆类、食用菌、藻类、坚果以及籽类等', '新鲜水果');
INSERT INTO `foodcat` VALUES ('04010103', '040101', '去皮或预切的鲜水果', 'GB2760-2011', '04', '水果、蔬菜、豆类、食用菌、藻类、坚果以及籽类等', '新鲜水果');
INSERT INTO `foodcat` VALUES ('040102', '0401', '加工水果', 'GB2760-2011', '04', '水果、蔬菜、豆类、食用菌、藻类、坚果以及籽类等', '水果');
INSERT INTO `foodcat` VALUES ('04010201', '040102', '冷冻水果', 'GB2760-2011', '04', '水果、蔬菜、豆类、食用菌、藻类、坚果以及籽类等', '加工水果');
INSERT INTO `foodcat` VALUES ('04010202', '040102', '水果干类', 'GB2760-2011', '04', '水果、蔬菜、豆类、食用菌、藻类、坚果以及籽类等', '加工水果');
INSERT INTO `foodcat` VALUES ('04010203', '040102', '醋、油或盐渍水果', 'GB2760-2011', '04', '水果、蔬菜、豆类、食用菌、藻类、坚果以及籽类等', '加工水果');
INSERT INTO `foodcat` VALUES ('04010204', '040102', '水果罐头', 'GB2760-2011', '04', '水果、蔬菜、豆类、食用菌、藻类、坚果以及籽类等', '加工水果');
INSERT INTO `foodcat` VALUES ('04010205', '040102', '果酱', 'GB2760-2011', '04', '水果、蔬菜、豆类、食用菌、藻类、坚果以及籽类等', '加工水果');
INSERT INTO `foodcat` VALUES ('04010206', '040102', '果泥', 'GB2760-2011', '04', '水果、蔬菜、豆类、食用菌、藻类、坚果以及籽类等', '加工水果');
INSERT INTO `foodcat` VALUES ('04010207', '040102', '其他果酱', 'GB2760-2011', '04', '水果、蔬菜、豆类、食用菌、藻类、坚果以及籽类等', '加工水果');
INSERT INTO `foodcat` VALUES ('04010208', '040102', '蜜饯凉果', 'GB2760-2011', '04', '水果、蔬菜、豆类、食用菌、藻类、坚果以及籽类等', '加工水果');
INSERT INTO `foodcat` VALUES ('0401020801', '04010208', '蜜饯类', 'GB2760-2011', '04', '水果、蔬菜、豆类、食用菌、藻类、坚果以及籽类等', '蜜饯凉果');
INSERT INTO `foodcat` VALUES ('0401020802', '04010208', '凉果类', 'GB2760-2011', '04', '水果、蔬菜、豆类、食用菌、藻类、坚果以及籽类等', '蜜饯凉果');
INSERT INTO `foodcat` VALUES ('0401020803', '04010208', '果脯类', 'GB2760-2011', '04', '水果、蔬菜、豆类、食用菌、藻类、坚果以及籽类等', '蜜饯凉果');
INSERT INTO `foodcat` VALUES ('0401020804', '04010208', '话化类', 'GB2760-2011', '04', '水果、蔬菜、豆类、食用菌、藻类、坚果以及籽类等', '蜜饯凉果');
INSERT INTO `foodcat` VALUES ('0401020805', '04010208', '果丹（饼）类', 'GB2760-2011', '04', '水果、蔬菜、豆类、食用菌、藻类、坚果以及籽类等', '蜜饯凉果');
INSERT INTO `foodcat` VALUES ('0401020806', '04010208', '果糕类', 'GB2760-2011', '04', '水果、蔬菜、豆类、食用菌、藻类、坚果以及籽类等', '蜜饯凉果');
INSERT INTO `foodcat` VALUES ('04010209', '040102', '装饰性果蔬', 'GB2760-2011', '04', '水果、蔬菜、豆类、食用菌、藻类、坚果以及籽类等', '加工水果');
INSERT INTO `foodcat` VALUES ('04010210', '040102', '水果甜品，包括果味液体甜品', 'GB2760-2011', '04', '水果、蔬菜、豆类、食用菌、藻类、坚果以及籽类等', '加工水果');
INSERT INTO `foodcat` VALUES ('04010211', '040102', '发酵的水果制品', 'GB2760-2011', '04', '水果、蔬菜、豆类、食用菌、藻类、坚果以及籽类等', '加工水果');
INSERT INTO `foodcat` VALUES ('04010212', '040102', '煮熟的或油炸的水果', 'GB2760-2011', '04', '水果、蔬菜、豆类、食用菌、藻类、坚果以及籽类等', '加工水果');
INSERT INTO `foodcat` VALUES ('04010213', '040102', '其他加工水果', 'GB2760-2011', '04', '水果、蔬菜、豆类、食用菌、藻类、坚果以及籽类等', '加工水果');
INSERT INTO `foodcat` VALUES ('0402', '04', '蔬菜', 'GB2760-2011', '04', '水果、蔬菜、豆类、食用菌、藻类、坚果以及籽类等', '水果、蔬菜、豆类、食用菌、藻类、坚果以及籽类等');
INSERT INTO `foodcat` VALUES ('040201', '0402', '新鲜蔬菜', 'GB2760-2011', '04', '水果、蔬菜、豆类、食用菌、藻类、坚果以及籽类等', '蔬菜');
INSERT INTO `foodcat` VALUES ('04020101', '040201', '未经加工鲜蔬菜', 'GB2760-2011', '04', '水果、蔬菜、豆类、食用菌、藻类、坚果以及籽类等', '新鲜蔬菜');
INSERT INTO `foodcat` VALUES ('04020102', '040201', '经表面处理的新鲜蔬菜', 'GB2760-2011', '04', '水果、蔬菜、豆类、食用菌、藻类、坚果以及籽类等', '新鲜蔬菜');
INSERT INTO `foodcat` VALUES ('04020103', '040201', '去皮、切块或切丝的蔬菜', 'GB2760-2011', '04', '水果、蔬菜、豆类、食用菌、藻类、坚果以及籽类等', '新鲜蔬菜');
INSERT INTO `foodcat` VALUES ('04020104', '040201', '豆芽菜', 'GB2760-2011', '04', '水果、蔬菜、豆类、食用菌、藻类、坚果以及籽类等', '新鲜蔬菜');
INSERT INTO `foodcat` VALUES ('040202', '0402', '加工蔬菜', 'GB2760-2011', '04', '水果、蔬菜、豆类、食用菌、藻类、坚果以及籽类等', '蔬菜');
INSERT INTO `foodcat` VALUES ('04020201', '040202', '冷冻蔬菜', 'GB2760-2011', '04', '水果、蔬菜、豆类、食用菌、藻类、坚果以及籽类等', '加工蔬菜');
INSERT INTO `foodcat` VALUES ('04020202', '040202', '干制蔬菜', 'GB2760-2011', '04', '水果、蔬菜、豆类、食用菌、藻类、坚果以及籽类等', '加工蔬菜');
INSERT INTO `foodcat` VALUES ('04020203', '040202', '腌渍的蔬菜', 'GB2760-2011', '04', '水果、蔬菜、豆类、食用菌、藻类、坚果以及籽类等', '加工蔬菜');
INSERT INTO `foodcat` VALUES ('04020204', '040202', '蔬菜罐头', 'GB2760-2011', '04', '水果、蔬菜、豆类、食用菌、藻类、坚果以及籽类等', '加工蔬菜');
INSERT INTO `foodcat` VALUES ('04020205', '040202', '蔬菜泥，番茄沙司除外', 'GB2760-2011', '04', '水果、蔬菜、豆类、食用菌、藻类、坚果以及籽类等', '加工蔬菜');
INSERT INTO `foodcat` VALUES ('04020206', '040202', '发酵蔬菜制品', 'GB2760-2011', '04', '水果、蔬菜、豆类、食用菌、藻类、坚果以及籽类等', '加工蔬菜');
INSERT INTO `foodcat` VALUES ('04020207', '040202', '经水煮或油炸的蔬菜', 'GB2760-2011', '04', '水果、蔬菜、豆类、食用菌、藻类、坚果以及籽类等', '加工蔬菜');
INSERT INTO `foodcat` VALUES ('04020208', '040202', '其他加工蔬菜', 'GB2760-2011', '04', '水果、蔬菜、豆类、食用菌、藻类、坚果以及籽类等', '加工蔬菜');
INSERT INTO `foodcat` VALUES ('040203', '0402', '根茎类', 'GB2760-2011', '04', '水果、蔬菜、豆类、食用菌、藻类、坚果以及籽类等', '蔬菜');
INSERT INTO `foodcat` VALUES ('040204', '0402', '叶菜类', 'GB2760-2011', '04', '水果、蔬菜、豆类、食用菌、藻类、坚果以及籽类等', '蔬菜');
INSERT INTO `foodcat` VALUES ('0403', '04', '食用菌和藻类', 'GB2760-2011', '04', '水果、蔬菜、豆类、食用菌、藻类、坚果以及籽类等', '水果、蔬菜、豆类、食用菌、藻类、坚果以及籽类等');
INSERT INTO `foodcat` VALUES ('040301', '0403', '新鲜食用菌和藻类', 'GB2760-2011', '04', '水果、蔬菜、豆类、食用菌、藻类、坚果以及籽类等', '食用菌和藻类');
INSERT INTO `foodcat` VALUES ('04030101', '040301', '未经加工鲜食用菌和藻类', 'GB2760-2011', '04', '水果、蔬菜、豆类、食用菌、藻类、坚果以及籽类等', '新鲜食用菌和藻类');
INSERT INTO `foodcat` VALUES ('04030102', '040301', '经表面处理的鲜食用菌和藻类', 'GB2760-2011', '04', '水果、蔬菜、豆类、食用菌、藻类、坚果以及籽类等', '新鲜食用菌和藻类');
INSERT INTO `foodcat` VALUES ('04030103', '040301', '去皮、切块或切丝的食用菌和藻类', 'GB2760-2011', '04', '水果、蔬菜、豆类、食用菌、藻类、坚果以及籽类等', '新鲜食用菌和藻类');
INSERT INTO `foodcat` VALUES ('040302', '0403', '加工食用菌和藻类', 'GB2760-2011', '04', '水果、蔬菜、豆类、食用菌、藻类、坚果以及籽类等', '食用菌和藻类');
INSERT INTO `foodcat` VALUES ('04030201', '040302', '冷冻食用菌和藻类', 'GB2760-2011', '04', '水果、蔬菜、豆类、食用菌、藻类、坚果以及籽类等', '加工食用菌和藻类');
INSERT INTO `foodcat` VALUES ('04030202', '040302', '干制食用菌和藻类', 'GB2760-2011', '04', '水果、蔬菜、豆类、食用菌、藻类、坚果以及籽类等', '加工食用菌和藻类');
INSERT INTO `foodcat` VALUES ('04030203', '040302', '腌渍的食用菌和藻类', 'GB2760-2011', '04', '水果、蔬菜、豆类、食用菌、藻类、坚果以及籽类等', '加工食用菌和藻类');
INSERT INTO `foodcat` VALUES ('04030204', '040302', '食用菌和藻类罐头', 'GB2760-2011', '04', '水果、蔬菜、豆类、食用菌、藻类、坚果以及籽类等', '加工食用菌和藻类');
INSERT INTO `foodcat` VALUES ('04030205', '040302', '经水煮或油炸的藻类', 'GB2760-2011', '04', '水果、蔬菜、豆类、食用菌、藻类、坚果以及籽类等', '加工食用菌和藻类');
INSERT INTO `foodcat` VALUES ('04030206', '040302', '其他加工食用菌和藻类', 'GB2760-2011', '04', '水果、蔬菜、豆类、食用菌、藻类、坚果以及籽类等', '加工食用菌和藻类');
INSERT INTO `foodcat` VALUES ('0404', '04', '豆类制品', 'GB2760-2011', '04', '水果、蔬菜、豆类、食用菌、藻类、坚果以及籽类等', '水果、蔬菜、豆类、食用菌、藻类、坚果以及籽类等');
INSERT INTO `foodcat` VALUES ('040401', '0404', '非发酵豆制品', 'GB2760-2011', '04', '水果、蔬菜、豆类、食用菌、藻类、坚果以及籽类等', '豆类制品');
INSERT INTO `foodcat` VALUES ('04040101', '040401', '豆腐类', 'GB2760-2011', '04', '水果、蔬菜、豆类、食用菌、藻类、坚果以及籽类等', '非发酵豆制品');
INSERT INTO `foodcat` VALUES ('04040102', '040401', '豆干类', 'GB2760-2011', '04', '水果、蔬菜、豆类、食用菌、藻类、坚果以及籽类等', '非发酵豆制品');
INSERT INTO `foodcat` VALUES ('04040103', '040401', '豆干再制品', 'GB2760-2011', '04', '水果、蔬菜、豆类、食用菌、藻类、坚果以及籽类等', '非发酵豆制品');
INSERT INTO `foodcat` VALUES ('0404010301', '04040103', '炸制半干豆腐', 'GB2760-2011', '04', '水果、蔬菜、豆类、食用菌、藻类、坚果以及籽类等', '豆干再制品');
INSERT INTO `foodcat` VALUES ('0404010302', '04040103', '卤制半干豆腐', 'GB2760-2011', '04', '水果、蔬菜、豆类、食用菌、藻类、坚果以及籽类等', '豆干再制品');
INSERT INTO `foodcat` VALUES ('0404010303', '04040103', '熏制半干豆腐', 'GB2760-2011', '04', '水果、蔬菜、豆类、食用菌、藻类、坚果以及籽类等', '豆干再制品');
INSERT INTO `foodcat` VALUES ('0404010304', '04040103', '其他半干豆腐', 'GB2760-2011', '04', '水果、蔬菜、豆类、食用菌、藻类、坚果以及籽类等', '豆干再制品');
INSERT INTO `foodcat` VALUES ('04040104', '040401', '腐竹类', 'GB2760-2011', '04', '水果、蔬菜、豆类、食用菌、藻类、坚果以及籽类等', '非发酵豆制品');
INSERT INTO `foodcat` VALUES ('04040105', '040401', '新型豆制品', 'GB2760-2011', '04', '水果、蔬菜、豆类、食用菌、藻类、坚果以及籽类等', '非发酵豆制品');
INSERT INTO `foodcat` VALUES ('04040106', '040401', '熟制豆类', 'GB2760-2011', '04', '水果、蔬菜、豆类、食用菌、藻类、坚果以及籽类等', '非发酵豆制品');
INSERT INTO `foodcat` VALUES ('040402', '0404', '发酵豆制品', 'GB2760-2011', '04', '水果、蔬菜、豆类、食用菌、藻类、坚果以及籽类等', '豆类制品');
INSERT INTO `foodcat` VALUES ('04040201', '040402', '腐乳类', 'GB2760-2011', '04', '水果、蔬菜、豆类、食用菌、藻类、坚果以及籽类等', '发酵豆制品');
INSERT INTO `foodcat` VALUES ('04040202', '040402', '豆豉及其制品', 'GB2760-2011', '04', '水果、蔬菜、豆类、食用菌、藻类、坚果以及籽类等', '发酵豆制品');
INSERT INTO `foodcat` VALUES ('040403', '0404', '其他豆制品', 'GB2760-2011', '04', '水果、蔬菜、豆类、食用菌、藻类、坚果以及籽类等', '豆类制品');
INSERT INTO `foodcat` VALUES ('0405', '04', '坚果和籽类', 'GB2760-2011', '04', '水果、蔬菜、豆类、食用菌、藻类、坚果以及籽类等', '水果、蔬菜、豆类、食用菌、藻类、坚果以及籽类等');
INSERT INTO `foodcat` VALUES ('040501', '0405', '新鲜坚果与籽类', 'GB2760-2011', '04', '水果、蔬菜、豆类、食用菌、藻类、坚果以及籽类等', '坚果和籽类');
INSERT INTO `foodcat` VALUES ('040502', '0405', '加工坚果与籽类', 'GB2760-2011', '04', '水果、蔬菜、豆类、食用菌、藻类、坚果以及籽类等', '坚果和籽类');
INSERT INTO `foodcat` VALUES ('04050201', '040502', '熟制坚果与籽类', 'GB2760-2011', '04', '水果、蔬菜、豆类、食用菌、藻类、坚果以及籽类等', '加工坚果与籽类');
INSERT INTO `foodcat` VALUES ('0405020101', '04050201', '带壳熟制坚果与籽类', 'GB2760-2011', '04', '水果、蔬菜、豆类、食用菌、藻类、坚果以及籽类等', '熟制坚果与籽类');
INSERT INTO `foodcat` VALUES ('0405020102', '04050201', '脱壳熟制坚果与籽类', 'GB2760-2011', '04', '水果、蔬菜、豆类、食用菌、藻类、坚果以及籽类等', '熟制坚果与籽类');
INSERT INTO `foodcat` VALUES ('04050202', '040502', '包衣的坚果和籽类', 'GB2760-2011', '04', '水果、蔬菜、豆类、食用菌、藻类、坚果以及籽类等', '加工坚果与籽类');
INSERT INTO `foodcat` VALUES ('04050203', '040502', '坚果与籽类罐头', 'GB2760-2011', '04', '水果、蔬菜、豆类、食用菌、藻类、坚果以及籽类等', '加工坚果与籽类');
INSERT INTO `foodcat` VALUES ('04050204', '040502', '坚果与籽类的泥（酱）', 'GB2760-2011', '04', '水果、蔬菜、豆类、食用菌、藻类、坚果以及籽类等', '加工坚果与籽类');
INSERT INTO `foodcat` VALUES ('04050205', '040502', '其他加工的坚果与籽类', 'GB2760-2011', '04', '水果、蔬菜、豆类、食用菌、藻类、坚果以及籽类等', '加工坚果与籽类');
INSERT INTO `foodcat` VALUES ('05', '', '可可制品、巧克力和巧克力制品以及糖果', 'GB2760-2011', '05', '可可制品、巧克力和巧克力制品以及糖果', '');
INSERT INTO `foodcat` VALUES ('0501', '05', '可可制品、巧克力和巧克力制品', 'GB2760-2011', '05', '可可制品、巧克力和巧克力制品以及糖果', '可可制品、巧克力和巧克力制品以及糖果');
INSERT INTO `foodcat` VALUES ('050101', '0501', '可可制品', 'GB2760-2011', '05', '可可制品、巧克力和巧克力制品以及糖果', '可可制品、巧克力和巧克力制品');
INSERT INTO `foodcat` VALUES ('050102', '0501', '巧克力和巧克力制品、其他可可制品', 'GB2760-2011', '05', '可可制品、巧克力和巧克力制品以及糖果', '可可制品、巧克力和巧克力制品');
INSERT INTO `foodcat` VALUES ('050103', '0501', '代可可脂巧克力及使用可可脂代用品的巧克力类似产品', 'GB2760-2011', '05', '可可制品、巧克力和巧克力制品以及糖果', '可可制品、巧克力和巧克力制品');
INSERT INTO `foodcat` VALUES ('0502', '05', '糖果', 'GB2760-2011', '05', '可可制品、巧克力和巧克力制品以及糖果', '可可制品、巧克力和巧克力制品以及糖果');
INSERT INTO `foodcat` VALUES ('050201', '0502', '胶基糖果', 'GB2760-2011', '05', '可可制品、巧克力和巧克力制品以及糖果', '糖果');
INSERT INTO `foodcat` VALUES ('050202', '0502', '除胶基糖果以外的其他糖果', 'GB2760-2011', '05', '可可制品、巧克力和巧克力制品以及糖果', '糖果');
INSERT INTO `foodcat` VALUES ('0503', '05', '糖果和巧克力制品包衣', 'GB2760-2011', '05', '可可制品、巧克力和巧克力制品以及糖果', '可可制品、巧克力和巧克力制品以及糖果');
INSERT INTO `foodcat` VALUES ('0504', '05', '装饰糖果、顶饰', 'GB2760-2011', '05', '可可制品、巧克力和巧克力制品以及糖果', '可可制品、巧克力和巧克力制品以及糖果');
INSERT INTO `foodcat` VALUES ('06', '', '粮食和粮食制品', 'GB2760-2011', '06', '粮食和粮食制品', '');
INSERT INTO `foodcat` VALUES ('0601', '06', '原粮', 'GB2760-2011', '06', '粮食和粮食制品', '粮食和粮食制品');
INSERT INTO `foodcat` VALUES ('0602', '06', '大米及其制品', 'GB2760-2011', '06', '粮食和粮食制品', '粮食和粮食制品');
INSERT INTO `foodcat` VALUES ('060201', '0602', '大米', 'GB2760-2011', '06', '粮食和粮食制品', '大米及其制品');
INSERT INTO `foodcat` VALUES ('060202', '0602', '大米制品', 'GB2760-2011', '06', '粮食和粮食制品', '大米及其制品');
INSERT INTO `foodcat` VALUES ('060203', '0602', '米粉', 'GB2760-2011', '06', '粮食和粮食制品', '大米及其制品');
INSERT INTO `foodcat` VALUES ('060204', '0602', '米粉制品', 'GB2760-2011', '06', '粮食和粮食制品', '大米及其制品');
INSERT INTO `foodcat` VALUES ('0603', '06', '小麦粉及其制品', 'GB2760-2011', '06', '粮食和粮食制品', '粮食和粮食制品');
INSERT INTO `foodcat` VALUES ('060301', '0603', '小麦粉', 'GB2760-2011', '06', '粮食和粮食制品', '小麦粉及其制品');
INSERT INTO `foodcat` VALUES ('06030101', '060301', '通用小麦粉', 'GB2760-2011', '06', '粮食和粮食制品', '小麦粉');
INSERT INTO `foodcat` VALUES ('06030102', '060301', '专用小麦粉', 'GB2760-2011', '06', '粮食和粮食制品', '小麦粉');
INSERT INTO `foodcat` VALUES ('060302', '0603', '小麦粉制品', 'GB2760-2011', '06', '粮食和粮食制品', '小麦粉及其制品');
INSERT INTO `foodcat` VALUES ('06030201', '060302', '生湿面制品', 'GB2760-2011', '06', '粮食和粮食制品', '小麦粉制品');
INSERT INTO `foodcat` VALUES ('06030202', '060302', '生干面制品', 'GB2760-2011', '06', '粮食和粮食制品', '小麦粉制品');
INSERT INTO `foodcat` VALUES ('06030203', '060302', '发酵面制品', 'GB2760-2011', '06', '粮食和粮食制品', '小麦粉制品');
INSERT INTO `foodcat` VALUES ('06030204', '060302', '面糊、裹粉、煎炸粉', 'GB2760-2011', '06', '粮食和粮食制品', '小麦粉制品');
INSERT INTO `foodcat` VALUES ('06030205', '060302', '油炸面制品', 'GB2760-2011', '06', '粮食和粮食制品', '小麦粉制品');
INSERT INTO `foodcat` VALUES ('0604', '06', '杂粮粉及其制品', 'GB2760-2011', '06', '粮食和粮食制品', '粮食和粮食制品');
INSERT INTO `foodcat` VALUES ('060401', '0604', '杂粮粉', 'GB2760-2011', '06', '粮食和粮食制品', '杂粮粉及其制品');
INSERT INTO `foodcat` VALUES ('060402', '0604', '杂粮制品', 'GB2760-2011', '06', '粮食和粮食制品', '杂粮粉及其制品');
INSERT INTO `foodcat` VALUES ('06040201', '060402', '八宝粥罐头', 'GB2760-2011', '06', '粮食和粮食制品', '杂粮制品');
INSERT INTO `foodcat` VALUES ('06040202', '060402', '其他杂粮制品', 'GB2760-2011', '06', '粮食和粮食制品', '杂粮制品');
INSERT INTO `foodcat` VALUES ('0605', '06', '淀粉及淀粉类制品', 'GB2760-2011', '06', '粮食和粮食制品', '粮食和粮食制品');
INSERT INTO `foodcat` VALUES ('060501', '0605', '食用淀粉', 'GB2760-2011', '06', '粮食和粮食制品', '淀粉及淀粉类制品');
INSERT INTO `foodcat` VALUES ('060502', '0605', '淀粉制品', 'GB2760-2011', '06', '粮食和粮食制品', '淀粉及淀粉类制品');
INSERT INTO `foodcat` VALUES ('06050201', '060502', '粉丝、粉条', 'GB2760-2011', '06', '粮食和粮食制品', '淀粉制品');
INSERT INTO `foodcat` VALUES ('06050202', '060502', '虾味片', 'GB2760-2011', '06', '粮食和粮食制品', '淀粉制品');
INSERT INTO `foodcat` VALUES ('06050203', '060502', '藕粉', 'GB2760-2011', '06', '粮食和粮食制品', '淀粉制品');
INSERT INTO `foodcat` VALUES ('06050204', '060502', '粉圆', 'GB2760-2011', '06', '粮食和粮食制品', '淀粉制品');
INSERT INTO `foodcat` VALUES ('0606', '06', '即食谷物', 'GB2760-2011', '06', '粮食和粮食制品', '粮食和粮食制品');
INSERT INTO `foodcat` VALUES ('0607', '06', '方便米面制品', 'GB2760-2011', '06', '粮食和粮食制品', '粮食和粮食制品');
INSERT INTO `foodcat` VALUES ('0608', '06', '冷冻米面制品', 'GB2760-2011', '06', '粮食和粮食制品', '粮食和粮食制品');
INSERT INTO `foodcat` VALUES ('0609', '06', '谷类和淀粉类甜品', 'GB2760-2011', '06', '粮食和粮食制品', '粮食和粮食制品');
INSERT INTO `foodcat` VALUES ('0610', '06', '粮食制品馅料', 'GB2760-2011', '06', '粮食和粮食制品', '粮食和粮食制品');
INSERT INTO `foodcat` VALUES ('07', '', '焙烤食品', 'GB2760-2011', '07', '焙烤食品', '');
INSERT INTO `foodcat` VALUES ('0701', '07', '面包', 'GB2760-2011', '07', '焙烤食品', '焙烤食品');
INSERT INTO `foodcat` VALUES ('0702', '07', '糕点', 'GB2760-2011', '07', '焙烤食品', '焙烤食品');
INSERT INTO `foodcat` VALUES ('070201', '0702', '中式糕点', 'GB2760-2011', '07', '焙烤食品', '糕点');
INSERT INTO `foodcat` VALUES ('070202', '0702', '西式糕点', 'GB2760-2011', '07', '焙烤食品', '糕点');
INSERT INTO `foodcat` VALUES ('070203', '0702', '月饼', 'GB2760-2011', '07', '焙烤食品', '糕点');
INSERT INTO `foodcat` VALUES ('070204', '0702', '糕点上彩装', 'GB2760-2011', '07', '焙烤食品', '糕点');
INSERT INTO `foodcat` VALUES ('0703', '07', '饼干', 'GB2760-2011', '07', '焙烤食品', '焙烤食品');
INSERT INTO `foodcat` VALUES ('070301', '0703', '夹心及装饰类饼干', 'GB2760-2011', '07', '焙烤食品', '饼干');
INSERT INTO `foodcat` VALUES ('070302', '0703', '威化饼干', 'GB2760-2011', '07', '焙烤食品', '饼干');
INSERT INTO `foodcat` VALUES ('070303', '0703', '蛋卷', 'GB2760-2011', '07', '焙烤食品', '饼干');
INSERT INTO `foodcat` VALUES ('070304', '0703', '其他饼干', 'GB2760-2011', '07', '焙烤食品', '饼干');
INSERT INTO `foodcat` VALUES ('0704', '07', '焙烤食品馅料及表面用挂浆', 'GB2760-2011', '07', '焙烤食品', '焙烤食品');
INSERT INTO `foodcat` VALUES ('0705', '07', '其他焙烤食品', 'GB2760-2011', '07', '焙烤食品', '焙烤食品');
INSERT INTO `foodcat` VALUES ('08', '', '肉及肉制品', 'GB2760-2011', '08', '肉及肉制品', '');
INSERT INTO `foodcat` VALUES ('0801', '08', '生、鲜肉', 'GB2760-2011', '08', '肉及肉制品', '肉及肉制品');
INSERT INTO `foodcat` VALUES ('080101', '0801', '生鲜肉', 'GB2760-2011', '08', '肉及肉制品', '生、鲜肉');
INSERT INTO `foodcat` VALUES ('080102', '0801', '冷却肉', 'GB2760-2011', '08', '肉及肉制品', '生、鲜肉');
INSERT INTO `foodcat` VALUES ('080103', '0801', '冻肉', 'GB2760-2011', '08', '肉及肉制品', '生、鲜肉');
INSERT INTO `foodcat` VALUES ('0802', '08', '预制肉制品', 'GB2760-2011', '08', '肉及肉制品', '肉及肉制品');
INSERT INTO `foodcat` VALUES ('080201', '0802', '调理肉制品', 'GB2760-2011', '08', '肉及肉制品', '预制肉制品');
INSERT INTO `foodcat` VALUES ('080202', '0802', '腌腊肉制品类', 'GB2760-2011', '08', '肉及肉制品', '预制肉制品');
INSERT INTO `foodcat` VALUES ('0803', '08', '熟肉制品', 'GB2760-2011', '08', '肉及肉制品', '肉及肉制品');
INSERT INTO `foodcat` VALUES ('080301', '0803', '酱卤肉制品类', 'GB2760-2011', '08', '肉及肉制品', '熟肉制品');
INSERT INTO `foodcat` VALUES ('08030101', '080301', '白煮肉类', 'GB2760-2011', '08', '肉及肉制品', '酱卤肉制品类');
INSERT INTO `foodcat` VALUES ('08030102', '080301', '酱卤肉类', 'GB2760-2011', '08', '肉及肉制品', '酱卤肉制品类');
INSERT INTO `foodcat` VALUES ('08030103', '080301', '糟肉类', 'GB2760-2011', '08', '肉及肉制品', '酱卤肉制品类');
INSERT INTO `foodcat` VALUES ('080302', '0803', '熏、烧、烤肉类', 'GB2760-2011', '08', '肉及肉制品', '熟肉制品');
INSERT INTO `foodcat` VALUES ('080303', '0803', '油炸肉类', 'GB2760-2011', '08', '肉及肉制品', '熟肉制品');
INSERT INTO `foodcat` VALUES ('080304', '0803', '西式火腿类', 'GB2760-2011', '08', '肉及肉制品', '熟肉制品');
INSERT INTO `foodcat` VALUES ('080305', '0803', '肉灌肠类', 'GB2760-2011', '08', '肉及肉制品', '熟肉制品');
INSERT INTO `foodcat` VALUES ('080306', '0803', '发酵肉制品类', 'GB2760-2011', '08', '肉及肉制品', '熟肉制品');
INSERT INTO `foodcat` VALUES ('080307', '0803', '熟肉干制品', 'GB2760-2011', '08', '肉及肉制品', '熟肉制品');
INSERT INTO `foodcat` VALUES ('08030701', '080307', '肉松类', 'GB2760-2011', '08', '肉及肉制品', '熟肉干制品');
INSERT INTO `foodcat` VALUES ('08030702', '080307', '肉干类', 'GB2760-2011', '08', '肉及肉制品', '熟肉干制品');
INSERT INTO `foodcat` VALUES ('08030703', '080307', '肉脯类', 'GB2760-2011', '08', '肉及肉制品', '熟肉干制品');
INSERT INTO `foodcat` VALUES ('080308', '0803', '肉罐头类', 'GB2760-2011', '08', '肉及肉制品', '熟肉制品');
INSERT INTO `foodcat` VALUES ('080309', '0803', '可食用动物肠衣类', 'GB2760-2011', '08', '肉及肉制品', '熟肉制品');
INSERT INTO `foodcat` VALUES ('080310', '0803', '其他肉及肉制品', 'GB2760-2011', '08', '肉及肉制品', '熟肉制品');
INSERT INTO `foodcat` VALUES ('09', '', '水产及其制品', 'GB2760-2011', '09', '水产及其制品', '');
INSERT INTO `foodcat` VALUES ('0901', '09', '鲜水产', 'GB2760-2011', '09', '水产及其制品', '水产及其制品');
INSERT INTO `foodcat` VALUES ('090101', '0901', '鲐鱼', 'GB2760-2011', '09', '水产及其制品', '鲜水产');
INSERT INTO `foodcat` VALUES ('090102', '0901', '其他', 'GB2760-2011', '09', '水产及其制品', '鲜水产');
INSERT INTO `foodcat` VALUES ('0902', '09', '冷冻水产品及其制品', 'GB2760-2011', '09', '水产及其制品', '水产及其制品');
INSERT INTO `foodcat` VALUES ('090201', '0902', '冷冻制品', 'GB2760-2011', '09', '水产及其制品', '冷冻水产品及其制品');
INSERT INTO `foodcat` VALUES ('090202', '0902', '冷冻挂桨制品', 'GB2760-2011', '09', '水产及其制品', '冷冻水产品及其制品');
INSERT INTO `foodcat` VALUES ('090203', '0902', '冷冻鱼糜制品', 'GB2760-2011', '09', '水产及其制品', '冷冻水产品及其制品');
INSERT INTO `foodcat` VALUES ('0903', '09', '预制水产品', 'GB2760-2011', '09', '水产及其制品', '水产及其制品');
INSERT INTO `foodcat` VALUES ('090301', '0903', '醋渍或肉冻状水产品', 'GB2760-2011', '09', '水产及其制品', '预制水产品');
INSERT INTO `foodcat` VALUES ('090302', '0903', '腌制水产品', 'GB2760-2011', '09', '水产及其制品', '预制水产品');
INSERT INTO `foodcat` VALUES ('090303', '0903', '鱼子制品', 'GB2760-2011', '09', '水产及其制品', '预制水产品');
INSERT INTO `foodcat` VALUES ('090304', '0903', '风干、烘干、压干等水产品', 'GB2760-2011', '09', '水产及其制品', '预制水产品');
INSERT INTO `foodcat` VALUES ('090305', '0903', '水发水产品(鱿鱼、海参等)', '', '09', '水产及其制品', '预制水产品');
INSERT INTO `foodcat` VALUES ('090306', '0903', '其他预制水产品', 'GB2760-2011', '09', '水产及其制品', '预制水产品');
INSERT INTO `foodcat` VALUES ('0904', '09', '熟制水产品', 'GB2760-2011', '09', '水产及其制品', '水产及其制品');
INSERT INTO `foodcat` VALUES ('090401', '0904', '熟干水产品', 'GB2760-2011', '09', '水产及其制品', '熟制水产品');
INSERT INTO `foodcat` VALUES ('090402', '0904', '经烹调或油炸的水产品', 'GB2760-2011', '09', '水产及其制品', '熟制水产品');
INSERT INTO `foodcat` VALUES ('090403', '0904', '熏、烤水产品', 'GB2760-2011', '09', '水产及其制品', '熟制水产品');
INSERT INTO `foodcat` VALUES ('090404', '0904', '发酵水产品', 'GB2760-2011', '09', '水产及其制品', '熟制水产品');
INSERT INTO `foodcat` VALUES ('0905', '09', '水产品罐头', 'GB2760-2011', '09', '水产及其制品', '水产及其制品');
INSERT INTO `foodcat` VALUES ('0906', '09', '其他水产品及其制品', 'GB2760-2011', '09', '水产及其制品', '水产及其制品');
INSERT INTO `foodcat` VALUES ('10', '', '蛋及蛋制品', 'GB2760-2011', '10', '蛋及蛋制品', '');
INSERT INTO `foodcat` VALUES ('1001', '10', '鲜蛋', 'GB2760-2011', '10', '蛋及蛋制品', '蛋及蛋制品');
INSERT INTO `foodcat` VALUES ('1002', '10', '再制蛋', 'GB2760-2011', '10', '蛋及蛋制品', '蛋及蛋制品');
INSERT INTO `foodcat` VALUES ('100201', '1002', '卤蛋', 'GB2760-2011', '10', '蛋及蛋制品', '再制蛋');
INSERT INTO `foodcat` VALUES ('100202', '1002', '糟蛋', 'GB2760-2011', '10', '蛋及蛋制品', '再制蛋');
INSERT INTO `foodcat` VALUES ('100203', '1002', '皮蛋', 'GB2760-2011', '10', '蛋及蛋制品', '再制蛋');
INSERT INTO `foodcat` VALUES ('100204', '1002', '咸蛋', 'GB2760-2011', '10', '蛋及蛋制品', '再制蛋');
INSERT INTO `foodcat` VALUES ('100205', '1002', '其他再制蛋', 'GB2760-2011', '10', '蛋及蛋制品', '再制蛋');
INSERT INTO `foodcat` VALUES ('1003', '10', '蛋制品', 'GB2760-2011', '10', '蛋及蛋制品', '蛋及蛋制品');
INSERT INTO `foodcat` VALUES ('100301', '1003', '脱水蛋制品', 'GB2760-2011', '10', '蛋及蛋制品', '蛋制品');
INSERT INTO `foodcat` VALUES ('100302', '1003', '热凝固蛋制品', 'GB2760-2011', '10', '蛋及蛋制品', '蛋制品');
INSERT INTO `foodcat` VALUES ('100303', '1003', '冷冻蛋制品', 'GB2760-2011', '10', '蛋及蛋制品', '蛋制品');
INSERT INTO `foodcat` VALUES ('100304', '1003', '液体蛋', 'GB2760-2011', '10', '蛋及蛋制品', '蛋制品');
INSERT INTO `foodcat` VALUES ('1004', '10', '其他蛋制品', 'GB2760-2011', '10', '蛋及蛋制品', '蛋及蛋制品');
INSERT INTO `foodcat` VALUES ('11', '', '甜味料，包括蜂蜜', 'GB2760-2011', '11', '甜味料，包括蜂蜜', '');
INSERT INTO `foodcat` VALUES ('1101', '11', '食糖', 'GB2760-2011', '11', '甜味料，包括蜂蜜', '甜味料，包括蜂蜜');
INSERT INTO `foodcat` VALUES ('110101', '1101', '白糖及白糖制品', 'GB2760-2011', '11', '甜味料，包括蜂蜜', '食糖');
INSERT INTO `foodcat` VALUES ('110102', '1101', '其他糖和糖浆', 'GB2760-2011', '11', '甜味料，包括蜂蜜', '食糖');
INSERT INTO `foodcat` VALUES ('1102', '11', '淀粉糖', 'GB2760-2011', '11', '甜味料，包括蜂蜜', '甜味料，包括蜂蜜');
INSERT INTO `foodcat` VALUES ('1103', '11', '蜂蜜及花粉', 'GB2760-2011', '11', '甜味料，包括蜂蜜', '甜味料，包括蜂蜜');
INSERT INTO `foodcat` VALUES ('110301', '1103', '蜂蜜', 'GB2760-2011', '11', '甜味料，包括蜂蜜', '蜂蜜及花粉');
INSERT INTO `foodcat` VALUES ('11030101', '110301', '桉树蜂蜜', 'GB2760-2011', '11', '甜味料，包括蜂蜜', '蜂蜜');
INSERT INTO `foodcat` VALUES ('11030102', '110301', '柑橘蜂蜜', 'GB2760-2011', '11', '甜味料，包括蜂蜜', '蜂蜜');
INSERT INTO `foodcat` VALUES ('11030103', '110301', '紫苜蓿蜂蜜', 'GB2760-2011', '11', '甜味料，包括蜂蜜', '蜂蜜');
INSERT INTO `foodcat` VALUES ('11030104', '110301', '荔枝蜂蜜', 'GB2760-2011', '11', '甜味料，包括蜂蜜', '蜂蜜');
INSERT INTO `foodcat` VALUES ('11030105', '110301', '野桂花蜂蜜', 'GB2760-2011', '11', '甜味料，包括蜂蜜', '蜂蜜');
INSERT INTO `foodcat` VALUES ('11030106', '110301', '龙眼蜂蜜', 'GB2760-2011', '11', '甜味料，包括蜂蜜', '蜂蜜');
INSERT INTO `foodcat` VALUES ('11030107', '110301', '鹅掌柴蜂蜜', 'GB2760-2011', '11', '甜味料，包括蜂蜜', '蜂蜜');
INSERT INTO `foodcat` VALUES ('11030108', '110301', '一级蜂蜜', 'GB2760-2011', '11', '甜味料，包括蜂蜜', '蜂蜜');
INSERT INTO `foodcat` VALUES ('11030109', '110301', '二级蜂蜜', 'GB2760-2011', '11', '甜味料，包括蜂蜜', '蜂蜜');
INSERT INTO `foodcat` VALUES ('11030110', '110301', '其他蜂蜜', 'GB2760-2011', '11', '甜味料，包括蜂蜜', '蜂蜜');
INSERT INTO `foodcat` VALUES ('110302', '1103', '花粉', 'GB2760-2011', '11', '甜味料，包括蜂蜜', '蜂蜜及花粉');
INSERT INTO `foodcat` VALUES ('1104', '11', '餐桌甜味料', 'GB2760-2011', '11', '甜味料，包括蜂蜜', '甜味料，包括蜂蜜');
INSERT INTO `foodcat` VALUES ('1105', '11', '调味糖浆', 'GB2760-2011', '11', '甜味料，包括蜂蜜', '甜味料，包括蜂蜜');
INSERT INTO `foodcat` VALUES ('110501', '1105', '水果调味糖浆', 'GB2760-2011', '11', '甜味料，包括蜂蜜', '调味糖浆');
INSERT INTO `foodcat` VALUES ('110502', '1105', '其他调味糖浆', 'GB2760-2011', '11', '甜味料，包括蜂蜜', '调味糖浆');
INSERT INTO `foodcat` VALUES ('1106', '11', '其他甜味料', 'GB2760-2011', '11', '甜味料，包括蜂蜜', '甜味料，包括蜂蜜');
INSERT INTO `foodcat` VALUES ('12', '', '调味品', 'GB2760-2011', '12', '调味品', '');
INSERT INTO `foodcat` VALUES ('1201', '12', '盐及代盐制品', 'GB2760-2011', '12', '调味品', '调味品');
INSERT INTO `foodcat` VALUES ('1202', '12', '鲜味剂和助鲜剂', 'GB2760-2011', '12', '调味品', '调味品');
INSERT INTO `foodcat` VALUES ('1203', '12', '醋', 'GB2760-2011', '12', '调味品', '调味品');
INSERT INTO `foodcat` VALUES ('120301', '1203', '酿造食醋', 'GB2760-2011', '12', '调味品', '醋');
INSERT INTO `foodcat` VALUES ('120302', '1203', '配制食醋', 'GB2760-2011', '12', '调味品', '醋');
INSERT INTO `foodcat` VALUES ('1204', '12', '酱油', 'GB2760-2011', '12', '调味品', '调味品');
INSERT INTO `foodcat` VALUES ('120401', '1204', '酿造酱油', 'GB2760-2011', '12', '调味品', '酱油');
INSERT INTO `foodcat` VALUES ('120402', '1204', '配制酱油', 'GB2760-2011', '12', '调味品', '酱油');
INSERT INTO `foodcat` VALUES ('1205', '12', '酱及酱制品', 'GB2760-2011', '12', '调味品', '调味品');
INSERT INTO `foodcat` VALUES ('120501', '1205', '酿造酱', 'GB2760-2011', '12', '调味品', '酱及酱制品');
INSERT INTO `foodcat` VALUES ('120502', '1205', '配制酱', 'GB2760-2011', '12', '调味品', '酱及酱制品');
INSERT INTO `foodcat` VALUES ('1206', '12', '_____', 'GB2760-2011', '12', '调味品', '调味品');
INSERT INTO `foodcat` VALUES ('1207', '12', '料酒及制品', 'GB2760-2011', '12', '调味品', '调味品');
INSERT INTO `foodcat` VALUES ('1208', '12', '______', 'GB2760-2011', '12', '调味品', '调味品');
INSERT INTO `foodcat` VALUES ('1209', '12', '香辛料类', 'GB2760-2011', '12', '调味品', '调味品');
INSERT INTO `foodcat` VALUES ('120901', '1209', '香辛料及粉', 'GB2760-2011', '12', '调味品', '香辛料类');
INSERT INTO `foodcat` VALUES ('120902', '1209', '香辛料油', 'GB2760-2011', '12', '调味品', '香辛料类');
INSERT INTO `foodcat` VALUES ('120903', '1209', '香辛料酱', 'GB2760-2011', '12', '调味品', '香辛料类');
INSERT INTO `foodcat` VALUES ('120904', '1209', '其他香辛料加工品', 'GB2760-2011', '12', '调味品', '香辛料类');
INSERT INTO `foodcat` VALUES ('1210', '12', '复合调味料', 'GB2760-2011', '12', '调味品', '调味品');
INSERT INTO `foodcat` VALUES ('121001', '1210', '固体复合调味料', 'GB2760-2011', '12', '调味品', '复合调味料');
INSERT INTO `foodcat` VALUES ('12100101', '121001', '固体汤料', 'GB2760-2011', '12', '调味品', '固体复合调味料');
INSERT INTO `foodcat` VALUES ('12100102', '121001', '鸡精、鸡粉', 'GB2760-2011', '12', '调味品', '固体复合调味料');
INSERT INTO `foodcat` VALUES ('12100103', '121001', '其他固体复合调味料', 'GB2760-2011', '12', '调味品', '固体复合调味料');
INSERT INTO `foodcat` VALUES ('121002', '1210', '半固体复合调味料', 'GB2760-2011', '12', '调味品', '复合调味料');
INSERT INTO `foodcat` VALUES ('12100201', '121002', '蛋黄酱、沙拉酱', 'GB2760-2011', '12', '调味品', '半固体复合调味料');
INSERT INTO `foodcat` VALUES ('12100202', '121002', '以动物性原料为基料的调味酱', 'GB2760-2011', '12', '调味品', '半固体复合调味料');
INSERT INTO `foodcat` VALUES ('12100203', '121002', '以蔬菜为基料的调味酱', 'GB2760-2011', '12', '调味品', '半固体复合调味料');
INSERT INTO `foodcat` VALUES ('12100204', '121002', '其他', 'GB2760-2011', '12', '调味品', '半固体复合调味料');
INSERT INTO `foodcat` VALUES ('121003', '1210', '液体复合调味料', 'GB2760-2011', '12', '调味品', '复合调味料');
INSERT INTO `foodcat` VALUES ('12100301', '121003', '浓缩汤', 'GB2760-2011', '12', '调味品', '液体复合调味料');
INSERT INTO `foodcat` VALUES ('12100302', '121003', '肉汤、骨汤', 'GB2760-2011', '12', '调味品', '液体复合调味料');
INSERT INTO `foodcat` VALUES ('12100303', '121003', '调味清汁', 'GB2760-2011', '12', '调味品', '液体复合调味料');
INSERT INTO `foodcat` VALUES ('12100304', '121003', '蚝油、虾油、鱼露等', 'GB2760-2011', '12', '调味品', '液体复合调味料');
INSERT INTO `foodcat` VALUES ('1211', '12', '其他调味料', 'GB2760-2011', '12', '调味品', '调味品');
INSERT INTO `foodcat` VALUES ('13', '', '特殊膳食用食品', 'GB2760-2011', '13', '特殊膳食用食品', '');
INSERT INTO `foodcat` VALUES ('1301', '13', '婴幼儿配方食品', 'GB2760-2011', '13', '特殊膳食用食品', '特殊膳食用食品');
INSERT INTO `foodcat` VALUES ('130101', '1301', '婴儿配方食品', 'GB2760-2011', '13', '特殊膳食用食品', '婴幼儿配方食品');
INSERT INTO `foodcat` VALUES ('130102', '1301', '较大婴儿和幼儿配方食品', 'GB2760-2011', '13', '特殊膳食用食品', '婴幼儿配方食品');
INSERT INTO `foodcat` VALUES ('130103', '1301', '特殊医学用途婴儿配方食品', 'GB2760-2011', '13', '特殊膳食用食品', '婴幼儿配方食品');
INSERT INTO `foodcat` VALUES ('1302', '13', '婴幼儿辅助食品', 'GB2760-2011', '13', '特殊膳食用食品', '特殊膳食用食品');
INSERT INTO `foodcat` VALUES ('130201', '1302', '婴幼儿谷类辅助食品', 'GB2760-2011', '13', '特殊膳食用食品', '婴幼儿辅助食品');
INSERT INTO `foodcat` VALUES ('130202', '1302', '婴幼儿罐装辅助食品', 'GB2760-2011', '13', '特殊膳食用食品', '婴幼儿辅助食品');
INSERT INTO `foodcat` VALUES ('1303', '13', '特殊医学用途配方食品', 'GB2760-2011', '13', '特殊膳食用食品', '特殊膳食用食品');
INSERT INTO `foodcat` VALUES ('1304', '13', '低能量配方食品', 'GB2760-2011', '13', '特殊膳食用食品', '特殊膳食用食品');
INSERT INTO `foodcat` VALUES ('1305', '13', '其他特殊膳食用食品', 'GB2760-2011', '13', '特殊膳食用食品', '特殊膳食用食品');
INSERT INTO `foodcat` VALUES ('14', '', '饮料类', 'GB2760-2011', '14', '饮料类', '');
INSERT INTO `foodcat` VALUES ('1401', '14', '包装饮用水类', 'GB2760-2011', '14', '饮料类', '饮料类');
INSERT INTO `foodcat` VALUES ('140101', '1401', '饮用天然矿泉水', 'GB2760-2011', '14', '饮料类', '1401');
INSERT INTO `foodcat` VALUES ('140102', '1401', '饮用纯净水', 'GB2760-2011', '14', '饮料类', '1401');
INSERT INTO `foodcat` VALUES ('140103', '1401', '其他饮用水', 'GB2760-2011', '14', '饮料类', '1401');
INSERT INTO `foodcat` VALUES ('1402', '14', '果蔬汁类', 'GB2760-2011', '14', '饮料类', '饮料类');
INSERT INTO `foodcat` VALUES ('140201', '1402', '果蔬汁（浆）', 'GB2760-2011', '14', '饮料类', '果蔬汁类');
INSERT INTO `foodcat` VALUES ('140202', '1402', '浓缩果蔬汁（浆）', 'GB2760-2011', '14', '饮料类', '果蔬汁类');
INSERT INTO `foodcat` VALUES ('140203', '1402', '果蔬汁（肉）', 'GB2760-2011', '14', '饮料类', '果蔬汁类');
INSERT INTO `foodcat` VALUES ('1403', '14', '蛋白饮料类', 'GB2760-2011', '14', '饮料类', '饮料类');
INSERT INTO `foodcat` VALUES ('140301', '1403', '含乳饮料', 'GB2760-2011', '14', '饮料类', '蛋白饮料类');
INSERT INTO `foodcat` VALUES ('14030101', '140301', '发酵型含乳饮料', 'GB2760-2011', '14', '饮料类', '含乳饮料');
INSERT INTO `foodcat` VALUES ('14030102', '140301', '配制型含乳饮料', 'GB2760-2011', '14', '饮料类', '含乳饮料');
INSERT INTO `foodcat` VALUES ('14030103', '140301', '乳酸菌饮料', 'GB2760-2011', '14', '饮料类', '含乳饮料');
INSERT INTO `foodcat` VALUES ('140302', '1403', '植物蛋白饮料', 'GB2760-2011', '14', '饮料类', '蛋白饮料类');
INSERT INTO `foodcat` VALUES ('140303', '1403', '复合蛋白饮料', 'GB2760-2011', '14', '饮料类', '蛋白饮料类');
INSERT INTO `foodcat` VALUES ('1404', '14', '水基调味饮料类', 'GB2760-2011', '14', '饮料类', '饮料类');
INSERT INTO `foodcat` VALUES ('140401', '1404', '碳酸饮料', 'GB2760-2011', '14', '饮料类', '水基调味饮料类');
INSERT INTO `foodcat` VALUES ('14040101', '140401', '可乐型碳酸饮料', 'GB2760-2011', '14', '饮料类', '碳酸饮料');
INSERT INTO `foodcat` VALUES ('14040102', '140401', '其他型碳酸饮料', 'GB2760-2011', '14', '饮料类', '碳酸饮料');
INSERT INTO `foodcat` VALUES ('140402', '1404', '非碳酸饮料', 'GB2760-2011', '14', '饮料类', '水基调味饮料类');
INSERT INTO `foodcat` VALUES ('14040201', '140402', '特殊用途饮料', 'GB2760-2011', '14', '饮料类', '非碳酸饮料');
INSERT INTO `foodcat` VALUES ('14040202', '140402', '风味饮料', 'GB2760-2011', '14', '饮料类', '非碳酸饮料');
INSERT INTO `foodcat` VALUES ('1405', '14', '茶、咖啡、植物饮料类', 'GB2760-2011', '14', '饮料类', '饮料类');
INSERT INTO `foodcat` VALUES ('140501', '1405', '茶饮料类', 'GB2760-2011', '14', '饮料类', '茶、咖啡、植物饮料类');
INSERT INTO `foodcat` VALUES ('140502', '1405', '咖啡饮料类', 'GB2760-2011', '14', '饮料类', '茶、咖啡、植物饮料类');
INSERT INTO `foodcat` VALUES ('140503', '1405', '植物饮料类', 'GB2760-2011', '14', '饮料类', '茶、咖啡、植物饮料类');
INSERT INTO `foodcat` VALUES ('1406', '14', '固体饮料类', 'GB2760-2011', '14', '饮料类', '饮料类');
INSERT INTO `foodcat` VALUES ('140601', '1406', '果香型固体饮料', 'GB2760-2011', '14', '饮料类', '固体饮料类');
INSERT INTO `foodcat` VALUES ('140602', '1406', '蛋白型固体饮料', 'GB2760-2011', '14', '饮料类', '固体饮料类');
INSERT INTO `foodcat` VALUES ('140603', '1406', '速溶咖啡', 'GB2760-2011', '14', '饮料类', '固体饮料类');
INSERT INTO `foodcat` VALUES ('140604', '1406', '其他固体饮料', 'GB2760-2011', '14', '饮料类', '固体饮料类');
INSERT INTO `foodcat` VALUES ('1407', '14', '生活饮用水', 'GB2760-2011', '14', '饮料类', '饮料类');
INSERT INTO `foodcat` VALUES ('1408', '14', '其他饮料类', 'GB2760-2011', '14', '饮料类', '饮料类');
INSERT INTO `foodcat` VALUES ('15', '', '酒类', 'GB2760-2011', '15', '酒类', '');
INSERT INTO `foodcat` VALUES ('1501', '15', '蒸馏酒', 'GB2760-2011', '15', '酒类', '酒类');
INSERT INTO `foodcat` VALUES ('150101', '1501', '白酒', 'GB2760-2011', '15', '酒类', '蒸馏酒');
INSERT INTO `foodcat` VALUES ('150102', '1501', '调香蒸馏酒', 'GB2760-2011', '15', '酒类', '蒸馏酒');
INSERT INTO `foodcat` VALUES ('150103', '1501', '白兰地', 'GB2760-2011', '15', '酒类', '蒸馏酒');
INSERT INTO `foodcat` VALUES ('150104', '1501', '威士忌', 'GB2760-2011', '15', '酒类', '蒸馏酒');
INSERT INTO `foodcat` VALUES ('150105', '1501', '伏特加', 'GB2760-2011', '15', '酒类', '蒸馏酒');
INSERT INTO `foodcat` VALUES ('150106', '1501', '朗姆酒', 'GB2760-2011', '15', '酒类', '蒸馏酒');
INSERT INTO `foodcat` VALUES ('150107', '1501', '其他蒸馏酒', 'GB2760-2011', '15', '酒类', '蒸馏酒');
INSERT INTO `foodcat` VALUES ('1502', '15', '配制酒', 'GB2760-2011', '15', '酒类', '酒类');
INSERT INTO `foodcat` VALUES ('1503', '15', '发酵酒', 'GB2760-2011', '15', '酒类', '酒类');
INSERT INTO `foodcat` VALUES ('150301', '1503', '葡萄酒', 'GB2760-2011', '15', '酒类', '发酵酒');
INSERT INTO `foodcat` VALUES ('15030101', '150301', '无汽葡萄酒', 'GB2760-2011', '15', '酒类', '葡萄酒');
INSERT INTO `foodcat` VALUES ('15030102', '150301', '起泡和半起泡葡萄酒', 'GB2760-2011', '15', '酒类', '葡萄酒');
INSERT INTO `foodcat` VALUES ('15030103', '150301', '调香葡萄酒', 'GB2760-2011', '15', '酒类', '葡萄酒');
INSERT INTO `foodcat` VALUES ('15030104', '150301', '特种葡萄酒', 'GB2760-2011', '15', '酒类', '葡萄酒');
INSERT INTO `foodcat` VALUES ('150302', '1503', '黄酒', 'GB2760-2011', '15', '酒类', '发酵酒');
INSERT INTO `foodcat` VALUES ('150303', '1503', '果酒', 'GB2760-2011', '15', '酒类', '发酵酒');
INSERT INTO `foodcat` VALUES ('150304', '1503', '蜂蜜酒', 'GB2760-2011', '15', '酒类', '发酵酒');
INSERT INTO `foodcat` VALUES ('150305', '1503', '啤酒和麦芽饮料', 'GB2760-2011', '15', '酒类', '发酵酒');
INSERT INTO `foodcat` VALUES ('150306', '1503', '其他发酵酒类', 'GB2760-2011', '15', '酒类', '发酵酒');
INSERT INTO `foodcat` VALUES ('16', '', '其他类', 'GB2760-2011', '16', '其他类', '');
INSERT INTO `foodcat` VALUES ('1601', '16', '果冻', 'GB2760-2011', '16', '其他类', '其他类');
INSERT INTO `foodcat` VALUES ('1602', '16', '茶叶、咖啡', 'GB2760-2011', '16', '其他类', '其他类');
INSERT INTO `foodcat` VALUES ('1603', '16', '胶原蛋白肠衣', 'GB2760-2011', '16', '其他类', '其他类');
INSERT INTO `foodcat` VALUES ('1604', '16', '酵母及酵母类制品', 'GB2760-2011', '16', '其他类', '其他类');
INSERT INTO `foodcat` VALUES ('160401', '1604', '干酵母', 'GB2760-2011', '16', '其他类', '酵母及酵母类制品');
INSERT INTO `foodcat` VALUES ('160402', '1604', '其他酵母及酵母类制品', 'GB2760-2011', '16', '其他类', '酵母及酵母类制品');
INSERT INTO `foodcat` VALUES ('1605', '16', '________', 'GB2760-2011', '16', '其他类', '其他类');
INSERT INTO `foodcat` VALUES ('1606', '16', '膨化食品', 'GB2760-2011', '16', '其他类', '其他类');
INSERT INTO `foodcat` VALUES ('1607', '16', '小吃', '', '16', '其他类', '其他类');
INSERT INTO `foodcat` VALUES ('160701', '1607', '味精', 'GB2760-2011', '16', '其他类', '其他');
INSERT INTO `foodcat` VALUES ('16070102', '160701', '增鲜味精', 'GB2760-2011', '16', '其他类', '味精');
INSERT INTO `foodcat` VALUES ('16070103', '160701', '加盐味精', 'GB2760-2011', '16', '其他类', '味精');
INSERT INTO `foodcat` VALUES ('16070104', '160701', '其他味精', 'GB2760-2011', '16', '其他类', '味精');
INSERT INTO `foodcat` VALUES ('160702', '1607', '自来水', 'GB2760-2011', '16', '其他类', '其他');
INSERT INTO `foodcat` VALUES ('160703', '1607', '其他', 'GB2760-2011', '16', '其他类', '其他');
INSERT INTO `foodcat` VALUES ('1608', '16', '油炸食品', '', '16', '其他类', '其他类');
INSERT INTO `foodcat` VALUES ('1609', '16', '其他', 'GB2760-2011', '16', '其他类', '其他类');

-- ----------------------------
-- Table structure for `fsn_comment`
-- ----------------------------
DROP TABLE IF EXISTS `fsn_comment`;
CREATE TABLE `fsn_comment` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `product_id` bigint(20) NOT NULL DEFAULT '0',
  `user_id` bigint(20) NOT NULL DEFAULT '0',
  `star` int(1) NOT NULL DEFAULT '0',
  `content` varchar(255) COLLATE utf8_unicode_ci NOT NULL DEFAULT '',
  `method` varchar(20) COLLATE utf8_unicode_ci NOT NULL DEFAULT '',
  `method_detail` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `time` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  `product_detail` varchar(50) CHARACTER SET utf8 DEFAULT '',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Records of fsn_comment
-- ----------------------------

-- ----------------------------
-- Table structure for `fsn_display`
-- ----------------------------
DROP TABLE IF EXISTS `fsn_display`;
CREATE TABLE `fsn_display` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `usrType` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '用户类型：匿名用户、cookie用户，实名用户',
  `uriId` bigint(20) DEFAULT NULL COMMENT 'URI编号',
  `container` int(3) DEFAULT NULL COMMENT '显示槽位',
  `active` int(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=73 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='根据用户类型在页面显示对应的信息，包含推荐栏、广告栏、楼';

-- ----------------------------
-- Records of fsn_display
-- ----------------------------
INSERT INTO `fsn_display` VALUES ('1', 'default', '1', '1', '1');
INSERT INTO `fsn_display` VALUES ('2', 'default', '2', '2', '1');
INSERT INTO `fsn_display` VALUES ('3', 'default', '3', '3', '1');
INSERT INTO `fsn_display` VALUES ('4', 'default', '4', '4', '1');
INSERT INTO `fsn_display` VALUES ('5', 'default', '5', '5', '1');
INSERT INTO `fsn_display` VALUES ('6', 'default', '6', '6', '1');
INSERT INTO `fsn_display` VALUES ('7', 'default', '7', '7', '1');
INSERT INTO `fsn_display` VALUES ('8', 'default', '8', '8', '1');
INSERT INTO `fsn_display` VALUES ('9', 'default', '9', '9', '1');
INSERT INTO `fsn_display` VALUES ('10', 'default', '10', '10', '1');
INSERT INTO `fsn_display` VALUES ('11', 'default', '11', '1', '1');
INSERT INTO `fsn_display` VALUES ('12', 'default', '12', '3', '1');
INSERT INTO `fsn_display` VALUES ('13', 'default', '13', '4', '1');
INSERT INTO `fsn_display` VALUES ('14', 'default', '14', '5', '1');
INSERT INTO `fsn_display` VALUES ('15', 'default', '15', '6', '1');
INSERT INTO `fsn_display` VALUES ('16', 'default', '16', '1', '1');
INSERT INTO `fsn_display` VALUES ('17', 'default', '17', '1', '1');
INSERT INTO `fsn_display` VALUES ('18', 'default', '18', '2', '1');
INSERT INTO `fsn_display` VALUES ('19', 'default', '19', '3', '1');
INSERT INTO `fsn_display` VALUES ('20', 'default', '20', '4', '1');
INSERT INTO `fsn_display` VALUES ('21', 'default', '21', '5', '1');
INSERT INTO `fsn_display` VALUES ('22', 'default', '22', '6', '1');
INSERT INTO `fsn_display` VALUES ('23', 'default', '23', '7', '1');
INSERT INTO `fsn_display` VALUES ('24', 'default', '24', '8', '1');
INSERT INTO `fsn_display` VALUES ('25', 'default', '25', '9', '1');
INSERT INTO `fsn_display` VALUES ('26', 'default', '26', '10', '1');
INSERT INTO `fsn_display` VALUES ('27', 'default', '27', '2', '1');
INSERT INTO `fsn_display` VALUES ('28', 'default', '28', '1', '1');
INSERT INTO `fsn_display` VALUES ('29', 'default', '29', '2', '1');
INSERT INTO `fsn_display` VALUES ('30', 'default', '30', '3', '1');
INSERT INTO `fsn_display` VALUES ('31', 'default', '31', '4', '1');
INSERT INTO `fsn_display` VALUES ('32', 'default', '32', '5', '1');
INSERT INTO `fsn_display` VALUES ('33', 'default', '33', '6', '1');
INSERT INTO `fsn_display` VALUES ('34', 'default', '34', '7', '1');
INSERT INTO `fsn_display` VALUES ('35', 'default', '35', '8', '1');
INSERT INTO `fsn_display` VALUES ('36', 'default', '36', '4', '1');
INSERT INTO `fsn_display` VALUES ('37', 'default', '37', '1', '1');
INSERT INTO `fsn_display` VALUES ('38', 'default', '38', '2', '1');
INSERT INTO `fsn_display` VALUES ('39', 'default', '39', '3', '1');
INSERT INTO `fsn_display` VALUES ('40', 'default', '40', '4', '1');
INSERT INTO `fsn_display` VALUES ('41', 'default', '41', '5', '1');
INSERT INTO `fsn_display` VALUES ('42', 'default', '42', '6', '1');
INSERT INTO `fsn_display` VALUES ('43', 'default', '43', '7', '1');
INSERT INTO `fsn_display` VALUES ('44', 'default', '44', '8', '1');
INSERT INTO `fsn_display` VALUES ('45', 'default', '45', '3', '1');
INSERT INTO `fsn_display` VALUES ('46', 'default', '46', '1', '1');
INSERT INTO `fsn_display` VALUES ('47', 'default', '47', '2', '1');
INSERT INTO `fsn_display` VALUES ('48', 'default', '48', '3', '1');
INSERT INTO `fsn_display` VALUES ('49', 'default', '49', '4', '1');
INSERT INTO `fsn_display` VALUES ('50', 'default', '50', '5', '1');
INSERT INTO `fsn_display` VALUES ('51', 'default', '51', '6', '1');
INSERT INTO `fsn_display` VALUES ('52', 'default', '52', '7', '1');
INSERT INTO `fsn_display` VALUES ('53', 'default', '53', '8', '1');
INSERT INTO `fsn_display` VALUES ('54', 'default', '54', '2', '1');
INSERT INTO `fsn_display` VALUES ('55', 'default', '55', '9', '1');
INSERT INTO `fsn_display` VALUES ('56', 'default', '56', '9', '1');
INSERT INTO `fsn_display` VALUES ('57', 'default', '57', '10', '1');
INSERT INTO `fsn_display` VALUES ('58', 'default', '58', '10', '1');

-- ----------------------------
-- Table structure for `fsn_display_ad`
-- ----------------------------
DROP TABLE IF EXISTS `fsn_display_ad`;
CREATE TABLE `fsn_display_ad` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `ad_pic` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '广告图片路径',
  `ad_url` varchar(255) CHARACTER SET utf8 DEFAULT NULL COMMENT '广告图片点击跳转链接',
  `col_code` varchar(10) CHARACTER SET utf8 DEFAULT NULL COMMENT '所属栏目code，没有则为通用广告',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='显示的广告信息';

-- ----------------------------
-- Records of fsn_display_ad
-- ----------------------------
INSERT INTO `fsn_display_ad` VALUES ('1', '/fsn/resource/img/test_ad.jpg', '#', null);

-- ----------------------------
-- Table structure for `fsn_display_col`
-- ----------------------------
DROP TABLE IF EXISTS `fsn_display_col`;
CREATE TABLE `fsn_display_col` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `col_name` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '栏目名称',
  `col_code` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '栏目编码',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Records of fsn_display_col
-- ----------------------------

-- ----------------------------
-- Table structure for `fsn_display_prod`
-- ----------------------------
DROP TABLE IF EXISTS `fsn_display_prod`;
CREATE TABLE `fsn_display_prod` (
  `id` bigint(20) NOT NULL DEFAULT '0',
  `prod_id` bigint(20) DEFAULT NULL COMMENT '产品id',
  `prod_name` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '产品名称',
  `prod_desc` text COLLATE utf8_unicode_ci COMMENT '产品描述',
  `prod_pic` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '产品图片资源路径，一份',
  `prod_url` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '产品链接',
  `col_code` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='楼的显示信息';

-- ----------------------------
-- Records of fsn_display_prod
-- ----------------------------
INSERT INTO `fsn_display_prod` VALUES ('0', '1', '雀巢咖啡', '雀巢公司陆续在中国的天津、青岛和东莞等地建起了自己的工厂，1999年秋天，雀巢成功地收购了太太乐集团80%的股份。之后，雀巢又于2001年与中国第二大鸡精生产厂家豪吉公司合作，成立了合资四川豪吉食品有限公司。\r\n雀巢在市场上咄咄逼人的态势，让它的竞争对手感到巨大的压力，国内一个乳制品企业的管理人员将雀巢形容为一个食品领域的“巨人”，随时都可以在相关的竞争领域发起攻势。', '/fsn/resource/img/nescafe1-3.jpg', '/fsn/ui/product_detail?proid=1', null);

-- ----------------------------
-- Table structure for `fsn_display_re`
-- ----------------------------
DROP TABLE IF EXISTS `fsn_display_re`;
CREATE TABLE `fsn_display_re` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `re_pic` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '推荐信息图片资源路径',
  `re_url` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '推荐跳转链接',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='推荐栏的显示信息';

-- ----------------------------
-- Records of fsn_display_re
-- ----------------------------

-- ----------------------------
-- Table structure for `fsn_display_uri`
-- ----------------------------
DROP TABLE IF EXISTS `fsn_display_uri`;
CREATE TABLE `fsn_display_uri` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `type` int(1) NOT NULL DEFAULT '0' COMMENT '资源类型 1-产品 2-广告 3-栏目 4-推荐 5-新闻',
  `content` text COLLATE utf8_unicode_ci COMMENT '资源内容',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=59 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='显示资源存储表';

-- ----------------------------
-- Records of fsn_display_uri
-- ----------------------------
INSERT INTO `fsn_display_uri` VALUES ('1', '1', '<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><prod><prodId>216</prodId><prodName>纯牛奶利乐</prodName><prodDesc>prodDesc:0</prodDesc><prodPic>http://211.151.134.74:8080/portal/img/product/mengniuchunniunailile/mncnnll.jpg</prodPic><prodUrl>resource/img/portal/qwf.png</prodUrl><colCode>0001</colCode><active>1</active><container>0</container></prod>');
INSERT INTO `fsn_display_uri` VALUES ('2', '1', '<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><prod><prodId>217</prodId><prodName>低脂高钙牛奶利乐砖</prodName><prodDesc>prodDesc:1</prodDesc><prodPic>http://211.151.134.74:8080/portal/img/product/mengniudizhigaogainiunaililezhuan/mndzggnnllz.jpg</prodPic><prodUrl>prodUrl:1</prodUrl><colCode>0001</colCode><active>1</active><container>1</container></prod>');
INSERT INTO `fsn_display_uri` VALUES ('3', '1', '<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><prod><prodId>218</prodId><prodName>低乳糖牛奶</prodName><prodDesc>prodDesc:2</prodDesc><prodPic>http://211.151.134.74:8080/portal/img/product/mengniuxinyangdaoditangniunai/mnxyddtnn.jpg</prodPic><prodUrl>prodUrl:2</prodUrl><colCode>0001</colCode><active>1</active><container>2</container></prod>');
INSERT INTO `fsn_display_uri` VALUES ('4', '1', '<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><prod><prodId>219</prodId><prodName>优益乳</prodName><prodDesc>prodDesc:3</prodDesc><prodPic>http://211.151.134.74:8080/portal/img/product/mengniuyouyiru/mnyyr.jpg</prodPic><prodUrl>prodUrl:3</prodUrl><colCode>0001</colCode><active>1</active><container>3</container></prod>');
INSERT INTO `fsn_display_uri` VALUES ('5', '1', '<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><prod><prodId>220</prodId><prodName>优菌乳</prodName><prodDesc>prodDesc:4</prodDesc><prodPic>http://211.151.134.74:8080/portal/img/product/mengniuyijunru/mnyjr.jpg</prodPic><prodUrl>prodUrl:4</prodUrl><colCode>0001</colCode><active>1</active><container>4</container></prod>');
INSERT INTO `fsn_display_uri` VALUES ('6', '1', '<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><prod><prodId>221</prodId><prodName>联杯菠萝</prodName><prodDesc>prodDesc:5</prodDesc><prodPic>http://211.151.134.74:8080/portal/img/product/liangbeiboluo/lianbeiboluo.jpg</prodPic><prodUrl>prodUrl:5</prodUrl><colCode>0001</colCode><active>1</active><container>5</container></prod>');
INSERT INTO `fsn_display_uri` VALUES ('7', '1', '<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><prod><prodId>222</prodId><prodName>1953黑加仑</prodName><prodDesc>prodDesc:6</prodDesc><prodPic>http://211.151.134.74:8080/portal/img/product/1953heijialun/1953heijialun.jpg</prodPic><prodUrl>prodUrl:6</prodUrl><colCode>0001</colCode><active>1</active><container>6</container></prod>');
INSERT INTO `fsn_display_uri` VALUES ('8', '1', '<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><prod><prodId>223</prodId><prodName>壶装酸奶</prodName><prodDesc>prodDesc:7</prodDesc><prodPic>http://211.151.134.74:8080/portal/img/product/huzhuangshuannai/huzhuangshuannai.jpg</prodPic><prodUrl>prodUrl:7</prodUrl><colCode>0001</colCode><active>1</active><container>7</container></prod>');
INSERT INTO `fsn_display_uri` VALUES ('11', '2', '<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><ad><adPic>adPic:0</adPic><adUrl>adUrl:0</adUrl><colCode>0001</colCode><active>1</active><container>0</container></ad>');
INSERT INTO `fsn_display_uri` VALUES ('12', '2', '<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><ad><adPic>adPic:2</adPic><adUrl>adUrl:2</adUrl><colCode></colCode><active>1</active><container>2</container></ad>');
INSERT INTO `fsn_display_uri` VALUES ('13', '2', '<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><ad><adPic>adPic:3</adPic><adUrl>adUrl:3</adUrl><colCode></colCode><active>1</active><container>3</container></ad>');
INSERT INTO `fsn_display_uri` VALUES ('14', '2', '<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><ad><adPic>adPic:4</adPic><adUrl>adUrl:4</adUrl><colCode></colCode><active>1</active><container>4</container></ad>');
INSERT INTO `fsn_display_uri` VALUES ('15', '2', '<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><ad><adPic>adPic:5</adPic><adUrl>adUrl:5</adUrl><colCode></colCode><active>1</active><container>5</container></ad>');
INSERT INTO `fsn_display_uri` VALUES ('16', '3', '<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><col><colName>牛奶</colName><colCode>0001</colCode><active>1</active><container>1</container></col>');
INSERT INTO `fsn_display_uri` VALUES ('17', '4', '<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><re><rePic>http://211.151.134.74:8080/portal/img/recommendAd/qwfu1.jpg</rePic><reUrl>/fsn-core/ui/portal/product-detail?proid=163</reUrl><active>1</active><container>0</container></re>');
INSERT INTO `fsn_display_uri` VALUES ('18', '4', '<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><re><rePic>http://211.151.134.74:8080/portal/img/recommendAd/lgd1.jpg</rePic><reUrl>/fsn-core/ui/portal/error404</reUrl><active>1</active><container>1</container></re>');
INSERT INTO `fsn_display_uri` VALUES ('19', '4', '<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><re><rePic>http://211.151.134.74:8080/portal/img/recommendAd/lxc1.jpg</rePic><reUrl>/fsn-core/ui/portal/error404</reUrl><active>1</active><container>2</container></re>');
INSERT INTO `fsn_display_uri` VALUES ('20', '4', '<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><re><rePic>http://211.151.134.74:8080/portal/img/recommendAd/visual1.jpg</rePic><reUrl>/fsn-core/ui/portal/error404</reUrl><active>1</active><container>3</container></re>');
INSERT INTO `fsn_display_uri` VALUES ('21', '4', '<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><re><rePic>http://211.151.134.74:8080/portal/img/recommendAd/slry.jpg</rePic><reUrl>/fsn-core/ui/portal/error404</reUrl><active>1</active><container>4</container></re>');
INSERT INTO `fsn_display_uri` VALUES ('22', '4', '<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><re><rePic>http://211.151.134.74:8080/portal/img/recommendAd/gzgt.jpg</rePic><reUrl>/fsn-core/ui/portal/error404</reUrl><active>1</active><container>5</container></re>');
INSERT INTO `fsn_display_uri` VALUES ('23', '4', '<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><re><rePic>http://211.151.134.74:8080/portal/img/recommendAd/lgd2.jpg</rePic><reUrl>/fsn-core/ui/portal/error404</reUrl><active>1</active><container>6</container></re>');
INSERT INTO `fsn_display_uri` VALUES ('24', '4', '<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><re><rePic>http://211.151.134.74:8080/portal/img/recommendAd/visual2.jpg</rePic><reUrl>/fsn-core/ui/portal/error404</reUrl><active>1</active><container>7</container></re>');
INSERT INTO `fsn_display_uri` VALUES ('25', '4', '<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><re><rePic>http://211.151.134.74:8080/portal/img/recommendAd/qwfu2.jpg</rePic><reUrl>/fsn-core/ui/portal/product-detail?proid=165</reUrl><active>1</active><container>8</container></re>');
INSERT INTO `fsn_display_uri` VALUES ('26', '4', '<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><re><rePic>http://211.151.134.74:8080/portal/img/recommendAd/lxc2.png</rePic><reUrl>/fsn-core/ui/portal/error404</reUrl><active>1</active><container>9</container></re>');
INSERT INTO `fsn_display_uri` VALUES ('27', '2', '<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><ad><adPic>adPic:1</adPic><adUrl>adUrl:1</adUrl><colCode></colCode><active>1</active><container>1</container></ad>');
INSERT INTO `fsn_display_uri` VALUES ('28', '1', '<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><prod><prodId>238</prodId><prodName>都匀毛尖</prodName><prodDesc>prodDesc:0</prodDesc><prodPic>http://211.151.134.74:8080/portal/img/product/duyunmaojian/duyunmaojian1.jpg</prodPic><prodUrl>resource/img/portal/qwf.png</prodUrl><colCode>0002</colCode><active>1</active><container>0</container></prod>');
INSERT INTO `fsn_display_uri` VALUES ('29', '1', '<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><prod><prodId>238</prodId><prodName>都匀毛尖</prodName><prodDesc>prodDesc:1</prodDesc><prodPic>http://211.151.134.74:8080/portal/img/product/duyunmaojian/duyunmaojian2.jpg</prodPic><prodUrl>prodUrl:1</prodUrl><colCode>0002</colCode><active>1</active><container>1</container></prod>');
INSERT INTO `fsn_display_uri` VALUES ('30', '1', '<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><prod><prodId>238</prodId><prodName>都匀毛尖</prodName><prodDesc>prodDesc:2</prodDesc><prodPic>http://211.151.134.74:8080/portal/img/product/duyunmaojian/duyunmaojian3.jpg</prodPic><prodUrl>prodUrl:2</prodUrl><colCode>0002</colCode><active>1</active><container>2</container></prod>');
INSERT INTO `fsn_display_uri` VALUES ('31', '1', '<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><prod><prodId>238</prodId><prodName>都匀毛尖</prodName><prodDesc>prodDesc:3</prodDesc><prodPic>http://211.151.134.74:8080/portal/img/product/duyunmaojian/duyunmaojian4.jpg</prodPic><prodUrl>prodUrl:3</prodUrl><colCode>0002</colCode><active>1</active><container>3</container></prod>');
INSERT INTO `fsn_display_uri` VALUES ('32', '1', '<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><prod><prodId>238</prodId><prodName>都匀毛尖</prodName><prodDesc>prodDesc:4</prodDesc><prodPic>http://211.151.134.74:8080/portal/img/product/duyunmaojian/duyunmaojian5.jpg</prodPic><prodUrl>prodUrl:4</prodUrl><colCode>0002</colCode><active>1</active><container>4</container></prod>');
INSERT INTO `fsn_display_uri` VALUES ('33', '1', '<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><prod><prodId>168</prodId><prodName>贵州辣子鸡</prodName><prodDesc>prodDesc:5</prodDesc><prodPic>http://211.151.134.74:8080/portal/img/product/guizhoulaziji/guizhoulaziji1.jpg</prodPic><prodUrl>prodUrl:5</prodUrl><colCode>0002</colCode><active>1</active><container>5</container></prod>');
INSERT INTO `fsn_display_uri` VALUES ('34', '1', '<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><prod><prodId>169</prodId><prodName>蔬菜猪肉干（醇香味）</prodName><prodDesc>prodDesc:6</prodDesc><prodPic>http://211.151.134.74:8080/portal/img/product/shucaizhurouganchunxiangwei/shucaizhurougan(chunxiangwei)1.jpg</prodPic><prodUrl>prodUrl:6</prodUrl><colCode>0002</colCode><active>1</active><container>6</container></prod>');
INSERT INTO `fsn_display_uri` VALUES ('35', '1', '<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><prod><prodId>170</prodId><prodName>精制猪肉脯（醇香味）</prodName><prodDesc>prodDesc:7</prodDesc><prodPic>http://211.151.134.74:8080/portal/img/product/jingzhizhuroupuchunxiangwei/jingzhizhuroupu(chunxiangwei)1.jpg</prodPic><prodUrl>prodUrl:7</prodUrl><colCode>0002</colCode><active>1</active><container>7</container></prod>');
INSERT INTO `fsn_display_uri` VALUES ('36', '3', '<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><col><colName>茶</colName><colCode>0002</colCode><active>1</active><container>4</container></col>');
INSERT INTO `fsn_display_uri` VALUES ('37', '1', '<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><prod><prodId>225</prodId><prodName>红油腐乳</prodName><prodDesc>prodDesc:0</prodDesc><prodPic>http://211.151.134.74:8080/portal/img/product/hongyoufuru/hongyoufuru.jpg</prodPic><prodUrl>resource/img/portal/qwf.png</prodUrl><colCode>0003</colCode><active>1</active><container>0</container></prod>');
INSERT INTO `fsn_display_uri` VALUES ('38', '1', '<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><prod><prodId>226</prodId><prodName>黄豆酱油</prodName><prodDesc>prodDesc:1</prodDesc><prodPic>http://211.151.134.74:8080/portal/img/product/huangdoujiangyou500ml/huangdoujiangyou500ml.jpg</prodPic><prodUrl>prodUrl:1</prodUrl><colCode>0003</colCode><active>1</active><container>1</container></prod>');
INSERT INTO `fsn_display_uri` VALUES ('39', '1', '<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><prod><prodId>227</prodId><prodName>生抽王</prodName><prodDesc>prodDesc:2</prodDesc><prodPic>http://211.151.134.74:8080/portal/img/product/shengchouwang/shengchouwang.jpg</prodPic><prodUrl>prodUrl:2</prodUrl><colCode>0003</colCode><active>1</active><container>2</container></prod>');
INSERT INTO `fsn_display_uri` VALUES ('40', '1', '<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><prod><prodId>228</prodId><prodName>蒜汁酱油</prodName><prodDesc>prodDesc:3</prodDesc><prodPic>http://211.151.134.74:8080/portal/img/product/suanzhijiangyou/suanzhijiangyou.jpg</prodPic><prodUrl>prodUrl:3</prodUrl><colCode>0003</colCode><active>1</active><container>3</container></prod>');
INSERT INTO `fsn_display_uri` VALUES ('41', '1', '<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><prod><prodId>229</prodId><prodName>特鲜酱油</prodName><prodDesc>prodDesc:4</prodDesc><prodPic>http://211.151.134.74:8080/portal/img/product/texianjiangyou/texianjiangyou.jpg</prodPic><prodUrl>prodUrl:4</prodUrl><colCode>0003</colCode><active>1</active><container>4</container></prod>');
INSERT INTO `fsn_display_uri` VALUES ('42', '1', '<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><prod><prodId>230</prodId><prodName>特制酱油</prodName><prodDesc>prodDesc:5</prodDesc><prodPic>http://211.151.134.74:8080/portal/img/product/tezhijiangyou/tezhijiangyou.jpg</prodPic><prodUrl>prodUrl:5</prodUrl><colCode>0003</colCode><active>1</active><container>5</container></prod>');
INSERT INTO `fsn_display_uri` VALUES ('43', '1', '<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><prod><prodId>231</prodId><prodName>铁强化营养酱油</prodName><prodDesc>prodDesc:6</prodDesc><prodPic>http://211.151.134.74:8080/portal/img/product/tieqianghuayinyangjiangyou/tieqianghuayinyangjiangyou.jpg</prodPic><prodUrl>prodUrl:6</prodUrl><colCode>0003</colCode><active>1</active><container>6</container></prod>');
INSERT INTO `fsn_display_uri` VALUES ('44', '1', '<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><prod><prodId>232</prodId><prodName>鲜味酱油</prodName><prodDesc>prodDesc:7</prodDesc><prodPic>http://211.151.134.74:8080/portal/img/product/xianweijiangyou/xianweijiangyou.jpg</prodPic><prodUrl>prodUrl:7</prodUrl><colCode>0003</colCode><active>1</active><container>7</container></prod>');
INSERT INTO `fsn_display_uri` VALUES ('45', '3', '<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><col><colName>调味</colName><colCode>0003</colCode><active>1</active><container>3</container></col>');
INSERT INTO `fsn_display_uri` VALUES ('46', '1', '<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><prod><prodId>163</prodId><prodName>贵州腊肠</prodName><prodDesc>prodDesc:0</prodDesc><prodPic>http://211.151.134.74:8080/portal/img/product/guizhoulachang/guizhoulachang2.jpg</prodPic><prodUrl>resource/img/portal/qwf.png</prodUrl><colCode>0004</colCode><active>1</active><container>0</container></prod>');
INSERT INTO `fsn_display_uri` VALUES ('47', '1', '<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><prod><prodId>164</prodId><prodName>广味香肠</prodName><prodDesc>prodDesc:1</prodDesc><prodPic>http://211.151.134.74:8080/portal/img/product/guangweixiangchang/guangweixiangchang1.jpg</prodPic><prodUrl>prodUrl:1</prodUrl><colCode>0004</colCode><active>1</active><container>1</container></prod>');
INSERT INTO `fsn_display_uri` VALUES ('48', '1', '<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><prod><prodId>165</prodId><prodName>老腊肉</prodName><prodDesc>prodDesc:2</prodDesc><prodPic>http://211.151.134.74:8080/portal/img/product/laolarou/laolarou1.jpg</prodPic><prodUrl>prodUrl:2</prodUrl><colCode>0004</colCode><active>1</active><container>2</container></prod>');
INSERT INTO `fsn_display_uri` VALUES ('49', '1', '<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><prod><prodId>166</prodId><prodName>猪肉小米鲊（甜味）</prodName><prodDesc>prodDesc:3</prodDesc><prodPic>http://211.151.134.74:8080/portal/img/product/zhurouxiaomizhatianwei/zhurouxiaomizha(tianwei)1.jpg</prodPic><prodUrl>prodUrl:3</prodUrl><colCode>0004</colCode><active>1</active><container>3</container></prod>');
INSERT INTO `fsn_display_uri` VALUES ('50', '1', '<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><prod><prodId>167</prodId><prodName>八宝小米鲊（甜味）</prodName><prodDesc>prodDesc:4</prodDesc><prodPic>http://211.151.134.74:8080/portal/img/product/babaoxiaomizhatianwei/babaoxiaomizha(tianwei)1.jpg</prodPic><prodUrl>prodUrl:4</prodUrl><colCode>0004</colCode><active>1</active><container>4</container></prod>');
INSERT INTO `fsn_display_uri` VALUES ('51', '1', '<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><prod><prodId>168</prodId><prodName>贵州辣子鸡</prodName><prodDesc>prodDesc:5</prodDesc><prodPic>http://211.151.134.74:8080/portal/img/product/guizhoulaziji/guizhoulaziji1.jpg</prodPic><prodUrl>prodUrl:5</prodUrl><colCode>0004</colCode><active>1</active><container>5</container></prod>');
INSERT INTO `fsn_display_uri` VALUES ('52', '1', '<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><prod><prodId>169</prodId><prodName>蔬菜猪肉干（醇香味）</prodName><prodDesc>prodDesc:6</prodDesc><prodPic>http://211.151.134.74:8080/portal/img/product/shucaizhourouganchunxiangwei/shucaizhourougan(chunxiangwei)1.jpg</prodPic><prodUrl>prodUrl:6</prodUrl><colCode>0004</colCode><active>1</active><container>6</container></prod>');
INSERT INTO `fsn_display_uri` VALUES ('53', '1', '<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><prod><prodId>170</prodId><prodName>精制猪肉脯（醇香味）</prodName><prodDesc>prodDesc:7</prodDesc><prodPic>http://211.151.134.74:8080/portal/img/product/jingzhizhuroupuchunxiangwei/jingzhizhuroupu(chunxiangwei)1.jpg</prodPic><prodUrl>prodUrl:7</prodUrl><colCode>0004</colCode><active>1</active><container>7</container></prod>');
INSERT INTO `fsn_display_uri` VALUES ('54', '3', '<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><col><colName>肉</colName><colCode>0004</colCode><active>1</active><container>2</container></col>');
INSERT INTO `fsn_display_uri` VALUES ('55', '1', '<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><prod><prodId>224</prodId><prodName>黄牛奶</prodName><prodDesc>prodDesc:0</prodDesc><prodPic>http://211.151.134.74:8080/portal/img/product/huangniunai/huangniunai.jpg</prodPic><prodUrl>resource/img/portal/qwf.png</prodUrl><colCode>0001</colCode><active>1</active><container>9</container></prod>');
INSERT INTO `fsn_display_uri` VALUES ('56', '1', '<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><prod><prodId>233</prodId><prodName>香辣臭腐乳</prodName><prodDesc>prodDesc:0</prodDesc><prodPic>http://211.151.134.74:8080/portal/img/product/xianglachoufuru/xianglachoufuru.jpg</prodPic><prodUrl>resource/img/portal/qwf.png</prodUrl><colCode>0003</colCode><active>1</active><container>9</container></prod>');
INSERT INTO `fsn_display_uri` VALUES ('57', '1', '<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><prod><prodId>234</prodId><prodName>香辣腐乳</prodName><prodDesc>prodDesc:0</prodDesc><prodPic>http://211.151.134.74:8080/portal/img/product/xianglafuru/xianglafuru.jpg</prodPic><prodUrl>resource/img/portal/qwf.png</prodUrl><colCode>0003</colCode><active>1</active><container>10</container></prod>');
INSERT INTO `fsn_display_uri` VALUES ('58', '1', '<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><prod><prodId>159</prodId><prodName>维生素AD奶</prodName><prodDesc>prodDesc:0</prodDesc><prodPic>http://211.151.134.74:8080/portal/img/product/weishengsuADgainai/weishengsuADgainai1.jpg</prodPic><prodUrl>resource/img/portal/qwf.png</prodUrl><colCode>0001</colCode><active>1</active><container>10</container></prod>');

-- ----------------------------
-- Table structure for `fsn_user`
-- ----------------------------
DROP TABLE IF EXISTS `fsn_user`;
CREATE TABLE `fsn_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `usr_name` varchar(20) CHARACTER SET utf8 NOT NULL DEFAULT '' COMMENT '登录名',
  `usr_pwd` varchar(20) CHARACTER SET utf8 NOT NULL DEFAULT '' COMMENT '登录密码',
  `usr_type` varchar(20) CHARACTER SET utf8 NOT NULL DEFAULT 'default' COMMENT '用户类型：匿名用户、cookie用户，实名用户',
  `usr_email` varchar(100) CHARACTER SET utf8 NOT NULL DEFAULT '' COMMENT '电子邮件',
  `usr_gender` int(1) DEFAULT NULL COMMENT '0-男 1-女',
  `usr_birth` date DEFAULT NULL COMMENT '出生年',
  `usr_birthplace` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '出生地',
  `usr_occupation` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '职业',
  `usr_income` varchar(50) CHARACTER SET utf8 DEFAULT NULL COMMENT '收入',
  `usr_sta` int(1) NOT NULL DEFAULT '0' COMMENT '用户状态: 0-未激活,1-激活,2-高级用户审核被退回',
  `usr_key` varchar(15) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '验证码（账号激活码）',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Records of fsn_user
-- ----------------------------

-- ----------------------------
-- Table structure for `message`
-- ----------------------------
DROP TABLE IF EXISTS `message`;
CREATE TABLE `message` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `content` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `sender_id` bigint(20) DEFAULT NULL,
  `sender_type` int(11) DEFAULT NULL,
  `create_date` datetime DEFAULT NULL,
  `receiver_id` bigint(20) DEFAULT NULL,
  `receiver_type` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Records of message
-- ----------------------------

-- ----------------------------
-- Table structure for `nutri_rpt`
-- ----------------------------
DROP TABLE IF EXISTS `nutri_rpt`;
CREATE TABLE `nutri_rpt` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `product_id` int(11) DEFAULT '0' COMMENT 'product ID',
  `nutri_id` int(11) DEFAULT NULL COMMENT '营养名称',
  `unit` varchar(30) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '营养值单位',
  `value` varchar(30) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '每个计算单位中的营养值',
  `per` varchar(30) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '计算单位如每100ml,每份',
  `nrv` varchar(25) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '营养所占百分比',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='营养报告';

-- ----------------------------
-- Records of nutri_rpt
-- ----------------------------

-- ----------------------------
-- Table structure for `nutri_std`
-- ----------------------------
DROP TABLE IF EXISTS `nutri_std`;
CREATE TABLE `nutri_std` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(30) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '营养名称',
  `daily_intake` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '一天摄入量',
  `unit` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '单位',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='营养标准表';

-- ----------------------------
-- Records of nutri_std
-- ----------------------------
INSERT INTO `nutri_std` VALUES ('1', '能量', '10000', '千焦');
INSERT INTO `nutri_std` VALUES ('2', '蛋白质', '80', '克');
INSERT INTO `nutri_std` VALUES ('3', '脂肪', '50', '克');
INSERT INTO `nutri_std` VALUES ('4', '碳水化合物', '400', '克');
INSERT INTO `nutri_std` VALUES ('5', '钠', '600', '毫克');
INSERT INTO `nutri_std` VALUES ('6', '纤维素', '100', '克');

-- ----------------------------
-- Table structure for `product`
-- ----------------------------
DROP TABLE IF EXISTS `product`;
CREATE TABLE `product` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `status` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `format` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `regularity` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `barcode` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `note` text COLLATE utf8_unicode_ci,
  `business_brand_id` bigint(20) DEFAULT NULL,
  `fda_product_group` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL,
  `producer_id` bigint(20) DEFAULT NULL,
  `qscore_self` float(3,2) NOT NULL DEFAULT '5.00' COMMENT '自检评分',
  `qscore_censor` float(3,2) NOT NULL DEFAULT '5.00' COMMENT '送检评分',
  `qscore_sample` float(3,2) NOT NULL DEFAULT '5.00' COMMENT '抽检评分',
  `imgurl` varchar(500) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '产品图片url路径',
  `des` text COLLATE utf8_unicode_ci COMMENT '产品描述',
  `cstm` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '适宜人群',
  `ingredient` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '主要成分',
  `category` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL,
  `feature` text COLLATE utf8_unicode_ci,
  `characteristic` varchar(255) CHARACTER SET utf8 NOT NULL DEFAULT '' COMMENT '产品特色',
  `expiration_date` varchar(255) CHARACTER SET utf8 DEFAULT NULL COMMENT '产品保质期',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_barcode` (`barcode`),
  KEY `fk_product_business_brand` (`business_brand_id`),
  KEY `fk_product_fda_product_group` (`fda_product_group`),
  KEY `idx_name` (`name`),
  KEY `fk_product_business_unit` (`producer_id`),
  CONSTRAINT `fk_product_business_brand` FOREIGN KEY (`business_brand_id`) REFERENCES `business_brand` (`id`),
  CONSTRAINT `fk_product_business_unit` FOREIGN KEY (`producer_id`) REFERENCES `business_unit` (`id`),
  CONSTRAINT `fk_product_fda_product_group` FOREIGN KEY (`fda_product_group`) REFERENCES `fda_product_group` (`code`)
) ENGINE=InnoDB AUTO_INCREMENT=10099 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Records of product
-- ----------------------------
INSERT INTO `product` VALUES ('1', '未知产品', '未知状态', '未知规格', '未知标准', null, null, '1', null, '1', '0.00', '0.00', '0.00', 'http://211.151.134.74:8080/portal/img/product/nescafe/nescafe1-1.jpg', '未知产品简介', null, null, '010101', null, '', null);

-- ----------------------------
-- Table structure for `product_auth`
-- ----------------------------
DROP TABLE IF EXISTS `product_auth`;
CREATE TABLE `product_auth` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `product_id` bigint(20) DEFAULT NULL COMMENT '产品ID',
  `auth_id` int(11) DEFAULT NULL COMMENT '认证的ID，关联到auth表',
  `auth_imgurl` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `auth_message` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='产品认证信息表';

-- ----------------------------
-- Records of product_auth
-- ----------------------------

-- ----------------------------
-- Table structure for `product_category`
-- ----------------------------
DROP TABLE IF EXISTS `product_category`;
CREATE TABLE `product_category` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `code` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL,
  `name` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `display` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `imgUrl` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_code` (`code`)
) ENGINE=InnoDB AUTO_INCREMENT=401 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Records of product_category
-- ----------------------------
INSERT INTO `product_category` VALUES ('7', '01', '乳及乳制品', '乳及乳制品', 'http://211.151.134.74:8080/portal/img/leftNav/nav01.png');
INSERT INTO `product_category` VALUES ('8', '0101', '巴氏杀菌乳、灭菌乳和调制乳', '巴氏杀菌乳类', null);
INSERT INTO `product_category` VALUES ('9', '010101', '巴氏杀菌乳', '巴氏杀菌乳', null);
INSERT INTO `product_category` VALUES ('10', '010102', '灭菌乳', '灭菌乳', null);
INSERT INTO `product_category` VALUES ('11', '010103', '调制乳', '调制乳', null);
INSERT INTO `product_category` VALUES ('12', '0102', '发酵乳和风味发酵乳', '风味发酵乳类', null);
INSERT INTO `product_category` VALUES ('13', '010201', '发酵乳', '发酵乳', null);
INSERT INTO `product_category` VALUES ('14', '010202', '风味发酵乳', '风味发酵乳', null);
INSERT INTO `product_category` VALUES ('15', '0103', '乳粉和奶油粉及其调制产品', '乳粉和奶油粉类', null);
INSERT INTO `product_category` VALUES ('16', '010301', '乳粉和奶油粉', '乳粉和奶油粉', null);
INSERT INTO `product_category` VALUES ('17', '010302', '调制乳粉和调制奶油粉', '调制乳粉和调制奶油粉', null);
INSERT INTO `product_category` VALUES ('18', '0104', '炼乳及其调制产品', '炼乳类', null);
INSERT INTO `product_category` VALUES ('19', '010401', '淡炼乳', '淡炼乳', null);
INSERT INTO `product_category` VALUES ('20', '010402', '调制炼乳', '调制炼乳', null);
INSERT INTO `product_category` VALUES ('21', '0105', '稀奶油及其类似品', '稀奶油类', null);
INSERT INTO `product_category` VALUES ('22', '010501', '稀奶油', '稀奶油', null);
INSERT INTO `product_category` VALUES ('23', '010502', '凝固稀奶油', '凝固稀奶油', null);
INSERT INTO `product_category` VALUES ('24', '010503', '调味稀奶油', '调味稀奶油', null);
INSERT INTO `product_category` VALUES ('25', '010504', '稀奶油类似品', '稀奶油类似品', null);
INSERT INTO `product_category` VALUES ('26', '0106', '干酪', '干酪', null);
INSERT INTO `product_category` VALUES ('27', '010601', '非熟化干酪', '非熟化干酪', null);
INSERT INTO `product_category` VALUES ('28', '010602', '熟化干酪', '熟化干酪', null);
INSERT INTO `product_category` VALUES ('29', '010603', '乳清干酪', '乳清干酪', null);
INSERT INTO `product_category` VALUES ('30', '010604', '再制干酪', '再制干酪', null);
INSERT INTO `product_category` VALUES ('33', '010605', '干酪类似品', '类干酪', null);
INSERT INTO `product_category` VALUES ('34', '010606', '乳清蛋白干酪', '乳清蛋白干酪', null);
INSERT INTO `product_category` VALUES ('35', '0107', '以乳为主要配料的即食风味甜点或其预制产品', '即食风味甜点', null);
INSERT INTO `product_category` VALUES ('36', '0108', '其他乳制品', '其他乳制品', null);
INSERT INTO `product_category` VALUES ('37', '02', '脂肪，油和乳化脂肪制品', '脂肪及油类', 'http://211.151.134.74:8080/portal/img/leftNav/nav02.png');
INSERT INTO `product_category` VALUES ('38', '0201', '基本不含水的脂肪和油', '基本无水脂肪和油', null);
INSERT INTO `product_category` VALUES ('39', '020101', '植物油脂', '植物油脂', null);
INSERT INTO `product_category` VALUES ('42', '020102', '动物油脂', '动物油脂', null);
INSERT INTO `product_category` VALUES ('43', '020103', '无水黄油，无水乳脂', '无水黄油，无水乳脂', null);
INSERT INTO `product_category` VALUES ('44', '0202', '水油状脂肪乳化制品', '水油状脂肪乳化制品', null);
INSERT INTO `product_category` VALUES ('45', '020201', '脂肪含量80%以上的乳化制品', '脂肪含量80%以上的乳化制品', null);
INSERT INTO `product_category` VALUES ('48', '020202', '脂肪含量80%以下的乳化制品', '脂肪含量80%以下的乳化制品', null);
INSERT INTO `product_category` VALUES ('49', '0203', '其他脂肪乳化制品', '其他脂肪乳化制品', null);
INSERT INTO `product_category` VALUES ('50', '0204', '脂肪类甜品', '脂肪类甜品', null);
INSERT INTO `product_category` VALUES ('51', '0205', '其他油脂或油脂制品', '其他油脂或油脂制品', null);
INSERT INTO `product_category` VALUES ('52', '03', '冷冻饮品', '冷冻饮品', 'http://211.151.134.74:8080/portal/img/leftNav/nav03.png');
INSERT INTO `product_category` VALUES ('53', '0301', '冰淇淋类、雪糕类', '冰淇淋类、雪糕类', null);
INSERT INTO `product_category` VALUES ('55', '0303', '风味冰、冰棍类', '风味冰、冰棍类', null);
INSERT INTO `product_category` VALUES ('56', '0304', '食用冰', '食用冰', null);
INSERT INTO `product_category` VALUES ('57', '0305', '其他冷冻饮品', '其他冷冻饮品', null);
INSERT INTO `product_category` VALUES ('58', '04', '水果、蔬菜、豆类、食用菌、藻类、坚果以及籽类等', '果蔬豆菌藻坚果类', 'http://211.151.134.74:8080/portal/img/leftNav/nav04.png');
INSERT INTO `product_category` VALUES ('59', '0401', '水果', '水果', null);
INSERT INTO `product_category` VALUES ('60', '040101', '新鲜水果', '新鲜水果', null);
INSERT INTO `product_category` VALUES ('64', '040102', '加工水果', '加工水果', null);
INSERT INTO `product_category` VALUES ('84', '0402', '蔬菜', '蔬菜', null);
INSERT INTO `product_category` VALUES ('85', '040201', '新鲜蔬菜', '新鲜蔬菜', null);
INSERT INTO `product_category` VALUES ('90', '040202', '加工蔬菜', '加工蔬菜', null);
INSERT INTO `product_category` VALUES ('91', '040203', '根茎类', '根茎类', null);
INSERT INTO `product_category` VALUES ('92', '040204', '叶菜类', '叶菜类', null);
INSERT INTO `product_category` VALUES ('101', '0403', '食用菌和藻类', '食用菌类', null);
INSERT INTO `product_category` VALUES ('102', '040301', '新鲜食用菌和藻类', '新鲜食用菌和藻类', null);
INSERT INTO `product_category` VALUES ('106', '040302', '加工食用菌和藻类', '加工食用菌和藻类', null);
INSERT INTO `product_category` VALUES ('113', '0404', '豆类制品', '豆类制品', null);
INSERT INTO `product_category` VALUES ('114', '040401', '非发酵豆制品', '非发酵豆制品', null);
INSERT INTO `product_category` VALUES ('125', '040402', '发酵豆制品', '发酵豆制品', null);
INSERT INTO `product_category` VALUES ('128', '040403', '其他豆制品', '其他豆制品', null);
INSERT INTO `product_category` VALUES ('129', '0405', '坚果和籽类', '坚果和籽类', null);
INSERT INTO `product_category` VALUES ('130', '040501', '新鲜坚果与籽类', '新鲜坚果与籽类', null);
INSERT INTO `product_category` VALUES ('131', '040502', '加工坚果与籽类', '加工坚果与籽类', null);
INSERT INTO `product_category` VALUES ('139', '05', '可可制品、巧克力和巧克力制品以及糖果', '巧克力和糖果类', 'http://211.151.134.74:8080/portal/img/leftNav/nav05.png');
INSERT INTO `product_category` VALUES ('140', '0501', '巧克力', '巧克力', null);
INSERT INTO `product_category` VALUES ('141', '050101', '可可制品', '可可制品', null);
INSERT INTO `product_category` VALUES ('142', '050102', '巧克力和巧克力制品、其他可可制品', '巧克力类制品', null);
INSERT INTO `product_category` VALUES ('143', '050103', '代可可脂巧克力及使用可可脂代用品的巧克力类似产品', '代可可脂巧克力类', null);
INSERT INTO `product_category` VALUES ('144', '0502', '糖果', '糖果', null);
INSERT INTO `product_category` VALUES ('145', '050201', '胶基糖果', '口香糖类', null);
INSERT INTO `product_category` VALUES ('146', '050202', '除胶基糖果以外的其他糖果', '硬质夹心糖类', null);
INSERT INTO `product_category` VALUES ('147', '0503', '糖果和巧克力制品包衣', '糖果和巧克力制品包衣', null);
INSERT INTO `product_category` VALUES ('148', '0504', '装饰糖果、顶饰', '装饰糖果类', null);
INSERT INTO `product_category` VALUES ('149', '06', '粮食和粮食制品', '粮食和粮食制品', 'http://211.151.134.74:8080/portal/img/leftNav/nav06.png');
INSERT INTO `product_category` VALUES ('150', '0601', '原粮', '原粮', null);
INSERT INTO `product_category` VALUES ('151', '0602', '大米及其制品', '大米及其制品', null);
INSERT INTO `product_category` VALUES ('152', '060201', '大米', '大米', null);
INSERT INTO `product_category` VALUES ('153', '060202', '大米制品', '大米制品', null);
INSERT INTO `product_category` VALUES ('154', '060203', '米粉', '米粉', null);
INSERT INTO `product_category` VALUES ('155', '060204', '米粉制品', '米粉制品', null);
INSERT INTO `product_category` VALUES ('156', '0603', '小麦粉及其制品', '小麦粉及其制品', null);
INSERT INTO `product_category` VALUES ('157', '060301', '小麦粉', '小麦粉', null);
INSERT INTO `product_category` VALUES ('160', '060302', '小麦粉制品', '小麦粉制品', null);
INSERT INTO `product_category` VALUES ('166', '0604', '杂粮粉及其制品', '杂粮粉及其制品', null);
INSERT INTO `product_category` VALUES ('167', '060401', '杂粮粉', '杂粮粉', null);
INSERT INTO `product_category` VALUES ('168', '060402', '杂粮制品', '杂粮制品', null);
INSERT INTO `product_category` VALUES ('171', '0605', '淀粉及淀粉类制品', '淀粉及淀粉类制品', null);
INSERT INTO `product_category` VALUES ('172', '060501', '食用淀粉', '食用淀粉', null);
INSERT INTO `product_category` VALUES ('173', '060502', '淀粉制品', '淀粉制品', null);
INSERT INTO `product_category` VALUES ('178', '0606', '即食谷物', '即食谷物', null);
INSERT INTO `product_category` VALUES ('179', '0607', '方便米面制品', '方便米面制品', null);
INSERT INTO `product_category` VALUES ('180', '0608', '冷冻米面制品', '冷冻米面制品', null);
INSERT INTO `product_category` VALUES ('181', '0609', '谷类和淀粉类甜品', '谷类和淀粉类甜品', null);
INSERT INTO `product_category` VALUES ('182', '0610', '粮食制品馅料', '粮食制品馅料', null);
INSERT INTO `product_category` VALUES ('183', '07', '焙烤食品', '焙烤食品', 'http://211.151.134.74:8080/portal/img/leftNav/nav07.png');
INSERT INTO `product_category` VALUES ('184', '0701', '面包', '面包', null);
INSERT INTO `product_category` VALUES ('185', '0702', '糕点', '糕点', null);
INSERT INTO `product_category` VALUES ('186', '070201', '中式糕点', '中式糕点', null);
INSERT INTO `product_category` VALUES ('187', '070202', '西式糕点', '西式糕点', null);
INSERT INTO `product_category` VALUES ('188', '070203', '月饼', '月饼', null);
INSERT INTO `product_category` VALUES ('189', '070204', '糕点上彩装', '糕点彩装', null);
INSERT INTO `product_category` VALUES ('190', '0703', '饼干', '饼干', null);
INSERT INTO `product_category` VALUES ('191', '070301', '夹心及装饰类饼干', '夹心及装饰类饼干', null);
INSERT INTO `product_category` VALUES ('192', '070302', '威化饼干', '威化饼干', null);
INSERT INTO `product_category` VALUES ('193', '070303', '蛋卷', '蛋卷', null);
INSERT INTO `product_category` VALUES ('194', '070304', '其他饼干', '其他饼干', null);
INSERT INTO `product_category` VALUES ('195', '0704', '焙烤食品馅料及表面用挂浆', '焙烤食品馅料及表面用挂浆', null);
INSERT INTO `product_category` VALUES ('196', '0705', '其他焙烤食品', '其他焙烤食品', null);
INSERT INTO `product_category` VALUES ('197', '08', '肉及肉制品', '肉及肉制品', 'http://211.151.134.74:8080/portal/img/leftNav/nav08.png');
INSERT INTO `product_category` VALUES ('198', '0801', '生、鲜肉', '生、鲜肉', null);
INSERT INTO `product_category` VALUES ('199', '080101', '生鲜肉', '生鲜肉', null);
INSERT INTO `product_category` VALUES ('200', '080102', '冷却肉', '冷却肉', null);
INSERT INTO `product_category` VALUES ('201', '080103', '冻肉', '冻肉', null);
INSERT INTO `product_category` VALUES ('202', '0802', '预制肉制品', '预制肉制品', null);
INSERT INTO `product_category` VALUES ('203', '080201', '调理肉制品', '调理肉制品', null);
INSERT INTO `product_category` VALUES ('204', '080202', '腌腊肉制品类', '腌腊肉制品类', null);
INSERT INTO `product_category` VALUES ('205', '0803', '熟肉制品', '熟肉制品', null);
INSERT INTO `product_category` VALUES ('206', '080301', '酱卤肉制品类', '酱卤肉制品类', null);
INSERT INTO `product_category` VALUES ('210', '080302', '熏、烧、烤肉类', '熏、烧、烤肉类', null);
INSERT INTO `product_category` VALUES ('211', '080303', '油炸肉类', '油炸肉类', null);
INSERT INTO `product_category` VALUES ('212', '080304', '西式火腿类', '西式火腿类', null);
INSERT INTO `product_category` VALUES ('213', '080305', '肉灌肠类', '肉灌肠类', null);
INSERT INTO `product_category` VALUES ('214', '080306', '发酵肉制品类', '发酵肉制品类', null);
INSERT INTO `product_category` VALUES ('215', '080307', '熟肉干制品', '熟肉干制品', null);
INSERT INTO `product_category` VALUES ('219', '080308', '肉罐头类', '肉罐头类', null);
INSERT INTO `product_category` VALUES ('220', '080309', '可食用动物肠衣类', '可食用动物肠衣类', null);
INSERT INTO `product_category` VALUES ('221', '080310', '其他肉及肉制品', '其他肉及肉制品', null);
INSERT INTO `product_category` VALUES ('222', '09', '水产及其制品', '水产及其制品', 'http://211.151.134.74:8080/portal/img/leftNav/nav09.png');
INSERT INTO `product_category` VALUES ('223', '0901', '鲜水产', '鲜水产', null);
INSERT INTO `product_category` VALUES ('224', '090101', '鲐鱼', '鲐鱼', null);
INSERT INTO `product_category` VALUES ('225', '090102', '其他', '其他', null);
INSERT INTO `product_category` VALUES ('226', '0902', '冷冻水产品及其制品', '冷冻水产品及其制品', null);
INSERT INTO `product_category` VALUES ('227', '090201', '冷冻制品', '冷冻制品', null);
INSERT INTO `product_category` VALUES ('228', '090202', '冷冻挂桨制品', '冷冻挂桨制品', null);
INSERT INTO `product_category` VALUES ('229', '090203', '冷冻鱼糜制品', '冷冻鱼糜制品', null);
INSERT INTO `product_category` VALUES ('230', '0903', '预制水产品', '预制水产品', null);
INSERT INTO `product_category` VALUES ('231', '090301', '醋渍或肉冻状水产品', '醋渍水产品', null);
INSERT INTO `product_category` VALUES ('232', '090302', '腌制水产品', '腌制水产品', null);
INSERT INTO `product_category` VALUES ('233', '090303', '鱼子制品', '鱼子制品', null);
INSERT INTO `product_category` VALUES ('234', '090304', '风干、烘干、压干等水产品', '干制水产品', null);
INSERT INTO `product_category` VALUES ('235', '090305', '水发水产品(鱿鱼、海参等)', '水发水产品', null);
INSERT INTO `product_category` VALUES ('236', '090306', '其他预制水产品', '其他预制水产品', null);
INSERT INTO `product_category` VALUES ('237', '0904', '熟制水产品', '熟制水产品', null);
INSERT INTO `product_category` VALUES ('238', '090401', '熟干水产品', '熟干水产品', null);
INSERT INTO `product_category` VALUES ('239', '090402', '经烹调或油炸的水产品', '烹制水产品', null);
INSERT INTO `product_category` VALUES ('240', '090403', '熏、烤水产品', '熏、烤水产品', null);
INSERT INTO `product_category` VALUES ('241', '090404', '发酵水产品', '发酵水产品', null);
INSERT INTO `product_category` VALUES ('242', '0905', '水产品罐头', '水产品罐头', null);
INSERT INTO `product_category` VALUES ('243', '0906', '其他水产品及其制品', '其他水产品及其制品', null);
INSERT INTO `product_category` VALUES ('244', '10', '蛋及蛋制品', '蛋及蛋制品', 'http://211.151.134.74:8080/portal/img/leftNav/nav10.png');
INSERT INTO `product_category` VALUES ('245', '1001', '鲜蛋', '鲜蛋', null);
INSERT INTO `product_category` VALUES ('246', '1002', '再制蛋', '再制蛋', null);
INSERT INTO `product_category` VALUES ('247', '100201', '卤蛋', '卤蛋', null);
INSERT INTO `product_category` VALUES ('248', '100202', '糟蛋', '糟蛋', null);
INSERT INTO `product_category` VALUES ('249', '100203', '皮蛋', '皮蛋', null);
INSERT INTO `product_category` VALUES ('250', '100204', '咸蛋', '咸蛋', null);
INSERT INTO `product_category` VALUES ('251', '100205', '其他再制蛋', '其他再制蛋', null);
INSERT INTO `product_category` VALUES ('252', '1003', '蛋制品', '蛋制品', null);
INSERT INTO `product_category` VALUES ('253', '100301', '脱水蛋制品', '脱水蛋制品', null);
INSERT INTO `product_category` VALUES ('254', '100302', '热凝固蛋制品', '热凝固蛋制品', null);
INSERT INTO `product_category` VALUES ('255', '100303', '冷冻蛋制品', '冷冻蛋制品', null);
INSERT INTO `product_category` VALUES ('256', '100304', '液体蛋', '液体蛋', null);
INSERT INTO `product_category` VALUES ('257', '1004', '其他蛋制品', '其他蛋制品', null);
INSERT INTO `product_category` VALUES ('258', '11', '甜味料，包括蜂蜜', '甜味料', 'http://211.151.134.74:8080/portal/img/leftNav/nav11.png');
INSERT INTO `product_category` VALUES ('259', '1101', '食糖', '食糖', null);
INSERT INTO `product_category` VALUES ('260', '110101', '白糖及白糖制品', '白糖及白糖制品', null);
INSERT INTO `product_category` VALUES ('261', '110102', '其他糖和糖浆', '其他糖和糖浆', null);
INSERT INTO `product_category` VALUES ('262', '1102', '淀粉糖', '淀粉糖', null);
INSERT INTO `product_category` VALUES ('263', '1103', '蜂蜜及花粉', '蜂蜜及花粉', null);
INSERT INTO `product_category` VALUES ('264', '110301', '蜂蜜', '蜂蜜', null);
INSERT INTO `product_category` VALUES ('275', '110302', '花粉', '花粉', null);
INSERT INTO `product_category` VALUES ('276', '1104', '餐桌甜味料', '餐桌甜味料', null);
INSERT INTO `product_category` VALUES ('277', '1105', '调味糖浆', '调味糖浆', null);
INSERT INTO `product_category` VALUES ('278', '110501', '水果调味糖浆', '水果调味糖浆', null);
INSERT INTO `product_category` VALUES ('279', '110502', '其他调味糖浆', '其他调味糖浆', null);
INSERT INTO `product_category` VALUES ('280', '1106', '其他甜味料', '其他甜味料', null);
INSERT INTO `product_category` VALUES ('281', '12', '调味品', '调味品', 'http://211.151.134.74:8080/portal/img/leftNav/nav12.png');
INSERT INTO `product_category` VALUES ('282', '1201', '盐及代盐制品', '盐及代盐制品', null);
INSERT INTO `product_category` VALUES ('283', '1202', '鲜味剂和助鲜剂', '鲜味剂和助鲜剂', null);
INSERT INTO `product_category` VALUES ('284', '1203', '醋', '醋', null);
INSERT INTO `product_category` VALUES ('285', '120301', '酿造食醋', '酿造食醋', null);
INSERT INTO `product_category` VALUES ('286', '120302', '配制食醋', '配制食醋', null);
INSERT INTO `product_category` VALUES ('287', '1204', '酱油', '酱油', null);
INSERT INTO `product_category` VALUES ('288', '120401', '酿造酱油', '酿造酱油', null);
INSERT INTO `product_category` VALUES ('289', '120402', '配制酱油', '配制酱油', null);
INSERT INTO `product_category` VALUES ('290', '1205', '酱及酱制品', '酱及酱制品', null);
INSERT INTO `product_category` VALUES ('291', '120501', '酿造酱', '酿造酱', null);
INSERT INTO `product_category` VALUES ('292', '120502', '配制酱', '配制酱', null);
INSERT INTO `product_category` VALUES ('294', '1207', '料酒及制品', '料酒及制品', null);
INSERT INTO `product_category` VALUES ('296', '1209', '香辛料类', '香辛料类', null);
INSERT INTO `product_category` VALUES ('297', '120901', '香辛料及粉', '香辛料及粉', null);
INSERT INTO `product_category` VALUES ('298', '120902', '香辛料油', '香辛料油', null);
INSERT INTO `product_category` VALUES ('299', '120903', '香辛料酱', '香辛料酱', null);
INSERT INTO `product_category` VALUES ('300', '120904', '其他香辛料加工品', '其他香辛料加工品', null);
INSERT INTO `product_category` VALUES ('301', '1210', '复合调味料', '复合调味料', null);
INSERT INTO `product_category` VALUES ('302', '121001', '固体复合调味料', '固体复合调味料', null);
INSERT INTO `product_category` VALUES ('306', '121002', '半固体复合调味料', '半固体复合调味料', null);
INSERT INTO `product_category` VALUES ('311', '121003', '液体复合调味料', '液体复合调味料', null);
INSERT INTO `product_category` VALUES ('316', '1211', '其他调味料', '其他调味料', null);
INSERT INTO `product_category` VALUES ('317', '13', '特殊膳食用食品', '特殊膳食用食品', 'http://211.151.134.74:8080/portal/img/leftNav/nav13.png');
INSERT INTO `product_category` VALUES ('318', '1301', '婴幼儿配方食品', '婴幼儿配方食品', null);
INSERT INTO `product_category` VALUES ('319', '130101', '婴儿配方食品', '婴儿配方食品', null);
INSERT INTO `product_category` VALUES ('320', '130102', '较大婴儿和幼儿配方食品', '婴幼儿配方食品', null);
INSERT INTO `product_category` VALUES ('321', '130103', '特殊医学用途婴儿配方食品', '特殊医学用途类', null);
INSERT INTO `product_category` VALUES ('322', '1302', '婴幼儿辅助食品', '婴幼儿辅助食品', null);
INSERT INTO `product_category` VALUES ('323', '130201', '婴幼儿谷类辅助食品', '婴幼儿谷类辅助食品', null);
INSERT INTO `product_category` VALUES ('324', '130202', '婴幼儿罐装辅助食品', '婴幼儿罐装辅助食品', null);
INSERT INTO `product_category` VALUES ('325', '1303', '特殊医学用途配方食品', '特殊医学用途配方食品', null);
INSERT INTO `product_category` VALUES ('326', '1304', '低能量配方食品', '低能量配方食品', null);
INSERT INTO `product_category` VALUES ('327', '1305', '其他特殊膳食用食品', '其他食品', null);
INSERT INTO `product_category` VALUES ('328', '14', '饮料类', '饮料类', 'http://211.151.134.74:8080/portal/img/leftNav/nav14.png');
INSERT INTO `product_category` VALUES ('329', '1401', '包装饮用水类', '包装饮用水类', null);
INSERT INTO `product_category` VALUES ('330', '140101', '饮用天然矿泉水', '饮用天然矿泉水', null);
INSERT INTO `product_category` VALUES ('331', '140102', '饮用纯净水', '饮用纯净水', null);
INSERT INTO `product_category` VALUES ('332', '140103', '其他饮用水', '其他饮用水', null);
INSERT INTO `product_category` VALUES ('333', '1402', '果蔬汁类', '果蔬汁类', null);
INSERT INTO `product_category` VALUES ('334', '140201', '果蔬汁（浆）', '果蔬汁', null);
INSERT INTO `product_category` VALUES ('335', '140202', '浓缩果蔬汁（浆）', '浓缩果蔬汁', null);
INSERT INTO `product_category` VALUES ('336', '140203', '果蔬汁（肉）', '果蔬汁', null);
INSERT INTO `product_category` VALUES ('337', '1403', '蛋白饮料类', '蛋白饮料类', null);
INSERT INTO `product_category` VALUES ('338', '140301', '含乳饮料', '含乳饮料', null);
INSERT INTO `product_category` VALUES ('342', '140302', '植物蛋白饮料', '植物蛋白饮料', null);
INSERT INTO `product_category` VALUES ('343', '140303', '复合蛋白饮料', '复合蛋白饮料', null);
INSERT INTO `product_category` VALUES ('344', '1404', '水基调味饮料类', '水基调味饮料类', null);
INSERT INTO `product_category` VALUES ('345', '140401', '碳酸饮料', '碳酸饮料', null);
INSERT INTO `product_category` VALUES ('348', '140402', '非碳酸饮料', '非碳酸饮料', null);
INSERT INTO `product_category` VALUES ('351', '1405', '茶、咖啡、植物饮料类', '茶、咖啡、植物饮料类', null);
INSERT INTO `product_category` VALUES ('352', '140501', '茶饮料类', '茶饮料类', null);
INSERT INTO `product_category` VALUES ('353', '140502', '咖啡饮料类', '咖啡饮料类', null);
INSERT INTO `product_category` VALUES ('354', '140503', '植物饮料类', '植物饮料类', null);
INSERT INTO `product_category` VALUES ('355', '1406', '固体饮料类', '固体饮料类', null);
INSERT INTO `product_category` VALUES ('356', '140601', '果香型固体饮料', '果香型固体饮料', null);
INSERT INTO `product_category` VALUES ('357', '140602', '蛋白型固体饮料', '蛋白型固体饮料', null);
INSERT INTO `product_category` VALUES ('358', '140603', '速溶咖啡', '速溶咖啡', null);
INSERT INTO `product_category` VALUES ('359', '140604', '其他固体饮料', '其他固体饮料', null);
INSERT INTO `product_category` VALUES ('360', '1407', '生活饮用水', '生活饮用水', null);
INSERT INTO `product_category` VALUES ('361', '1408', '其他饮料类', '其他饮料类', null);
INSERT INTO `product_category` VALUES ('362', '15', '酒类', '酒类', 'http://211.151.134.74:8080/portal/img/leftNav/nav15.png');
INSERT INTO `product_category` VALUES ('363', '1501', '蒸馏酒', '蒸馏酒', null);
INSERT INTO `product_category` VALUES ('364', '150101', '白酒', '白酒', null);
INSERT INTO `product_category` VALUES ('365', '150102', '调香蒸馏酒', '调香蒸馏酒', null);
INSERT INTO `product_category` VALUES ('366', '150103', '白兰地', '白兰地', null);
INSERT INTO `product_category` VALUES ('367', '150104', '威士忌', '威士忌', null);
INSERT INTO `product_category` VALUES ('368', '150105', '伏特加', '伏特加', null);
INSERT INTO `product_category` VALUES ('369', '150106', '朗姆酒', '朗姆酒', null);
INSERT INTO `product_category` VALUES ('370', '150107', '其他蒸馏酒', '其他蒸馏酒', null);
INSERT INTO `product_category` VALUES ('371', '1502', '配制酒', '配制酒', null);
INSERT INTO `product_category` VALUES ('372', '1503', '发酵酒', '发酵酒', null);
INSERT INTO `product_category` VALUES ('373', '150301', '葡萄酒', '葡萄酒', null);
INSERT INTO `product_category` VALUES ('378', '150302', '黄酒', '黄酒', null);
INSERT INTO `product_category` VALUES ('379', '150303', '果酒', '果酒', null);
INSERT INTO `product_category` VALUES ('380', '150304', '蜂蜜酒', '蜂蜜酒', null);
INSERT INTO `product_category` VALUES ('381', '150305', '啤酒和麦芽饮料', '啤酒和麦芽饮料', null);
INSERT INTO `product_category` VALUES ('382', '150306', '其他发酵酒类', '其他发酵酒类', null);
INSERT INTO `product_category` VALUES ('383', '16', '其他类', '其他类', 'http://211.151.134.74:8080/portal/img/leftNav/nav16.png');
INSERT INTO `product_category` VALUES ('384', '1601', '果冻', '果冻', null);
INSERT INTO `product_category` VALUES ('385', '1602', '茶叶、咖啡', '茶叶、咖啡', null);
INSERT INTO `product_category` VALUES ('386', '1603', '胶原蛋白肠衣', '胶原蛋白肠衣', null);
INSERT INTO `product_category` VALUES ('387', '1604', '酵母及酵母类制品', '酵母及其制品', null);
INSERT INTO `product_category` VALUES ('388', '160401', '干酵母', '干酵母', null);
INSERT INTO `product_category` VALUES ('389', '160402', '其他酵母及酵母类制品', '其他酵母及其制品', null);
INSERT INTO `product_category` VALUES ('391', '1606', '膨化食品', '膨化食品', null);
INSERT INTO `product_category` VALUES ('392', '1607', '小吃', '小吃', null);
INSERT INTO `product_category` VALUES ('393', '160701', '味精', '味精', null);
INSERT INTO `product_category` VALUES ('397', '160702', '自来水', '自来水', null);
INSERT INTO `product_category` VALUES ('398', '160703', '其他', '其他', null);
INSERT INTO `product_category` VALUES ('399', '1608', '油炸食品', '油炸食品', null);
INSERT INTO `product_category` VALUES ('400', '1609', '其他', '其他', null);

-- ----------------------------
-- Table structure for `product_certification`
-- ----------------------------
DROP TABLE IF EXISTS `product_certification`;
CREATE TABLE `product_certification` (
  `Id` bigint(11) NOT NULL AUTO_INCREMENT,
  `product_id` bigint(20) DEFAULT NULL COMMENT '产品ID',
  `cert_id` bigint(20) DEFAULT NULL COMMENT '认证的ID，关联到certification',
  `document_url` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`Id`),
  KEY `fk_product_cert_certification` (`cert_id`),
  CONSTRAINT `fk_product_cert_cert` FOREIGN KEY (`cert_id`) REFERENCES `certification` (`id`) ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='产品认证信息表';

-- ----------------------------
-- Records of product_certification
-- ----------------------------

-- ----------------------------
-- Table structure for `product_instance`
-- ----------------------------
DROP TABLE IF EXISTS `product_instance`;
CREATE TABLE `product_instance` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `batch_serial_no` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `serial` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `production_date` datetime DEFAULT NULL,
  `expiration_date` datetime DEFAULT NULL,
  `product_id` bigint(20) DEFAULT NULL,
  `original_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_product_instance_product_instance` (`original_id`),
  KEY `fk_product_instance_product` (`product_id`),
  KEY `idx_batch_serial_no_serial_product_id` (`batch_serial_no`,`serial`,`product_id`) USING BTREE,
  CONSTRAINT `fk_product_instance_product` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`),
  CONSTRAINT `fk_product_instance_product_instance` FOREIGN KEY (`original_id`) REFERENCES `product_instance` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=312 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Records of product_instance
-- ----------------------------

-- ----------------------------
-- Table structure for `product_poll`
-- ----------------------------
DROP TABLE IF EXISTS `product_poll`;
CREATE TABLE `product_poll` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `product_instance_id` bigint(20) DEFAULT NULL,
  `user_id` bigint(20) DEFAULT NULL,
  `like_` tinyint(4) DEFAULT NULL,
  `rate` int(11) DEFAULT NULL,
  `poll_date` datetime DEFAULT NULL,
  `comment` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_product_poll_product_instance` (`product_instance_id`),
  CONSTRAINT `fk_product_poll_product_instance` FOREIGN KEY (`product_instance_id`) REFERENCES `product_instance` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Records of product_poll
-- ----------------------------

-- ----------------------------
-- Table structure for `testa`
-- ----------------------------
DROP TABLE IF EXISTS `testa`;
CREATE TABLE `testa` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `user` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `item` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `rank` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Records of testa
-- ----------------------------

-- ----------------------------
-- Table structure for `testb`
-- ----------------------------
DROP TABLE IF EXISTS `testb`;
CREATE TABLE `testb` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `usera` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `ranka` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `userb` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `rankb` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `count` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Records of testb
-- ----------------------------

-- ----------------------------
-- Table structure for `test_case`
-- ----------------------------
DROP TABLE IF EXISTS `test_case`;
CREATE TABLE `test_case` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `test_cycle_id` bigint(20) DEFAULT NULL,
  `product_instance_id` bigint(20) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `business_brand_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_test_case_product_instance` (`product_instance_id`),
  KEY `fk_test_case_test_cycle` (`test_cycle_id`),
  KEY `fk_test_case_business_brand` (`business_brand_id`),
  CONSTRAINT `fk_test_case_business_brand` FOREIGN KEY (`business_brand_id`) REFERENCES `business_brand` (`id`),
  CONSTRAINT `fk_test_case_product_instance` FOREIGN KEY (`product_instance_id`) REFERENCES `product_instance` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Records of test_case
-- ----------------------------

-- ----------------------------
-- Table structure for `test_cycle`
-- ----------------------------
DROP TABLE IF EXISTS `test_cycle`;
CREATE TABLE `test_cycle` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `start_date` datetime NOT NULL,
  `end_date` datetime NOT NULL,
  `create_date` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Records of test_cycle
-- ----------------------------

-- ----------------------------
-- Table structure for `test_lab`
-- ----------------------------
DROP TABLE IF EXISTS `test_lab`;
CREATE TABLE `test_lab` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `address` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `parent_lab_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_parent_lab` (`parent_lab_id`),
  CONSTRAINT `fk_parent_lab` FOREIGN KEY (`parent_lab_id`) REFERENCES `test_lab` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=40 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Records of test_lab
-- ----------------------------

-- ----------------------------
-- Table structure for `test_lab_test`
-- ----------------------------
DROP TABLE IF EXISTS `test_lab_test`;
CREATE TABLE `test_lab_test` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `test_lab_id` bigint(20) DEFAULT NULL,
  `test_cycle_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_test_lab_test_test_lab` (`test_lab_id`),
  KEY `fk_test_lab_test_test_cycle` (`test_cycle_id`),
  CONSTRAINT `fk_test_lab_test_test_lab` FOREIGN KEY (`test_lab_id`) REFERENCES `test_lab` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Records of test_lab_test
-- ----------------------------

-- ----------------------------
-- Table structure for `test_property`
-- ----------------------------
DROP TABLE IF EXISTS `test_property`;
CREATE TABLE `test_property` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `test_result_id` bigint(20) DEFAULT NULL,
  `category` bigint(20) DEFAULT NULL,
  `name` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `result` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `unit` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL,
  `tech_indicator` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `assessment` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `standard` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_test_property_test_result` (`test_result_id`),
  KEY `fk_test_property_test_property_category` (`category`),
  CONSTRAINT `fk_test_property_test_property_category` FOREIGN KEY (`category`) REFERENCES `test_property_category` (`id`),
  CONSTRAINT `fk_test_property_test_result` FOREIGN KEY (`test_result_id`) REFERENCES `test_result` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Records of test_property
-- ----------------------------

-- ----------------------------
-- Table structure for `test_property_category`
-- ----------------------------
DROP TABLE IF EXISTS `test_property_category`;
CREATE TABLE `test_property_category` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `code` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL,
  `name` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_code` (`code`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Records of test_property_category
-- ----------------------------
INSERT INTO `test_property_category` VALUES ('1', '1001', '营养成份');
INSERT INTO `test_property_category` VALUES ('2', '1002', '微量元素');

-- ----------------------------
-- Table structure for `test_result`
-- ----------------------------
DROP TABLE IF EXISTS `test_result`;
CREATE TABLE `test_result` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `comment` varchar(2000) COLLATE utf8_unicode_ci DEFAULT NULL,
  `pass` tinyint(1) DEFAULT NULL,
  `test_date` datetime DEFAULT NULL,
  `tester_id` bigint(20) DEFAULT NULL,
  `tester_type` int(11) DEFAULT NULL,
  `fda_test_plan_id` bigint(20) DEFAULT NULL,
  `brand_id` bigint(20) DEFAULT NULL,
  `testee_id` bigint(20) DEFAULT NULL,
  `sample_id` bigint(20) DEFAULT NULL,
  `sample_quantity` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL,
  `sampling_location` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `sampling_date` datetime DEFAULT NULL,
  `test_type` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `equipment` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `standard` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `result` varchar(2000) COLLATE utf8_unicode_ci DEFAULT NULL,
  `approve_by` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL,
  `audit_by` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL,
  `key_tester` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL,
  `pdf_report` varchar(1024) COLLATE utf8_unicode_ci DEFAULT NULL,
  `test_lab_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_test_result_fda_test_plan` (`fda_test_plan_id`),
  KEY `fk_test_result_business_unit` (`testee_id`),
  KEY `fk_test_result_product_instance` (`sample_id`),
  KEY `fk_test_result_business_brand` (`brand_id`),
  KEY `fk_test_result_test_lab` (`test_lab_id`),
  CONSTRAINT `fk_test_result_business_brand` FOREIGN KEY (`brand_id`) REFERENCES `business_brand` (`id`),
  CONSTRAINT `fk_test_result_business_unit` FOREIGN KEY (`testee_id`) REFERENCES `business_unit` (`id`),
  CONSTRAINT `fk_test_result_fda_test_plan` FOREIGN KEY (`fda_test_plan_id`) REFERENCES `fda_test_plan` (`id`),
  CONSTRAINT `fk_test_result_product_instance` FOREIGN KEY (`sample_id`) REFERENCES `product_instance` (`id`),
  CONSTRAINT `fk_test_result_test_lab` FOREIGN KEY (`test_lab_id`) REFERENCES `test_lab` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=214 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Records of test_result
-- ----------------------------

-- ----------------------------
-- Table structure for `user`
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `display_name` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `type` smallint(6) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=40 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Records of user
-- ----------------------------

-- ----------------------------
-- Table structure for `users`
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
  `USER_ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `PASSWORD` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `USER_NAME` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL,
  `ORGANIZATION_NAME` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `REGISTER_DATE` datetime DEFAULT NULL,
  `LAST_LOGIN_DATE` datetime DEFAULT NULL,
  PRIMARY KEY (`USER_ID`),
  UNIQUE KEY `USER_NAME` (`USER_NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Records of users
-- ----------------------------
