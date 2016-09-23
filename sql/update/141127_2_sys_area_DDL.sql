/*
Navicat MySQL Data Transfer

Source Server         : local
Source Server Version : 50534
Source Host           : localhost:3306
Source Database       : fsn_core

Target Server Type    : MYSQL
Target Server Version : 50534
File Encoding         : 65001

Date: 2014-11-27 17:47:06
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `sys_area`
-- ----------------------------
DROP TABLE IF EXISTS `sys_area`;
CREATE TABLE `sys_area` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `parent_id` bigint(20) DEFAULT '0' COMMENT '父级编号',
  `parent_ids` varchar(2000) CHARACTER SET utf8 DEFAULT '' COMMENT '所有父级编号',
  `code` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '区域编码',
  `name` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '区域名称',
  `type` varchar(1) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '区域类型',
  PRIMARY KEY (`id`),
  KEY `sys_area_parent_id` (`parent_id`)
) ENGINE=InnoDB AUTO_INCREMENT=104 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='区域表';

-- ----------------------------
-- Records of sys_area
-- ----------------------------
INSERT INTO `sys_area` VALUES ('1', '0', '0', '520000', '贵州省', '1');
INSERT INTO `sys_area` VALUES ('2', '1', '0,1', '520100', '贵阳市', '2');
INSERT INTO `sys_area` VALUES ('3', '2', '0,1,2', '520101', '市辖区', '3');
INSERT INTO `sys_area` VALUES ('4', '2', '0,1,2', '520102', '南明区', '3');
INSERT INTO `sys_area` VALUES ('5', '2', '0,1,2', '520103', '云岩区', '3');
INSERT INTO `sys_area` VALUES ('6', '2', '0,1,2', '520111', '花溪区', '3');
INSERT INTO `sys_area` VALUES ('7', '2', '0,1,2', '520112', '乌当区', '3');
INSERT INTO `sys_area` VALUES ('8', '2', '0,1,2', '520113', '白云区', '3');
INSERT INTO `sys_area` VALUES ('9', '2', '0,1,2', '520115', '观山湖区', '3');
INSERT INTO `sys_area` VALUES ('10', '2', '0,1,2', '520121', '开阳县', '3');
INSERT INTO `sys_area` VALUES ('11', '2', '0,1,2', '520122', '息烽县', '3');
INSERT INTO `sys_area` VALUES ('12', '2', '0,1,2', '520123', '修文县', '3');
INSERT INTO `sys_area` VALUES ('13', '2', '0,1,2', '520181', '清镇市', '3');
INSERT INTO `sys_area` VALUES ('14', '1', '0,1', '520200', '六盘水市', '2');
INSERT INTO `sys_area` VALUES ('15', '14', '0,1,14', '520201', '钟山区', '3');
INSERT INTO `sys_area` VALUES ('16', '14', '0,1,14', '520203', '六枝特区', '3');
INSERT INTO `sys_area` VALUES ('17', '14', '0,1,14', '520221', '水城县', '3');
INSERT INTO `sys_area` VALUES ('18', '14', '0,1,14', '520222', '盘县', '3');
INSERT INTO `sys_area` VALUES ('19', '1', '0,1', '520300', '遵义市', '2');
INSERT INTO `sys_area` VALUES ('20', '19', '0,1,19', '520301', '市辖区', '3');
INSERT INTO `sys_area` VALUES ('21', '19', '0,1,19', '520302', '红花岗区', '3');
INSERT INTO `sys_area` VALUES ('22', '19', '0,1,19', '520303', '汇川区', '3');
INSERT INTO `sys_area` VALUES ('23', '19', '0,1,19', '520321', '遵义县', '3');
INSERT INTO `sys_area` VALUES ('24', '19', '0,1,19', '520322', '桐梓县', '3');
INSERT INTO `sys_area` VALUES ('25', '19', '0,1,19', '520323', '绥阳县', '3');
INSERT INTO `sys_area` VALUES ('26', '19', '0,1,19', '520324', '正安县', '3');
INSERT INTO `sys_area` VALUES ('27', '19', '0,1,19', '520325', '道真仡佬族苗族自治县', '3');
INSERT INTO `sys_area` VALUES ('28', '19', '0,1,19', '520326', '务川仡佬族苗族自治县', '3');
INSERT INTO `sys_area` VALUES ('29', '19', '0,1,19', '520327', '凤冈县', '3');
INSERT INTO `sys_area` VALUES ('30', '19', '0,1,19', '520328', '湄潭县', '3');
INSERT INTO `sys_area` VALUES ('31', '19', '0,1,19', '520329', '余庆县', '3');
INSERT INTO `sys_area` VALUES ('32', '19', '0,1,19', '520330', '习水县', '3');
INSERT INTO `sys_area` VALUES ('33', '19', '0,1,19', '520381', '赤水市', '3');
INSERT INTO `sys_area` VALUES ('34', '19', '0,1,19', '520382', '仁怀市', '3');
INSERT INTO `sys_area` VALUES ('35', '1', '0,1', '520400', '安顺市', '2');
INSERT INTO `sys_area` VALUES ('36', '35', '0,1,35', '520401', '市辖区', '3');
INSERT INTO `sys_area` VALUES ('37', '35', '0,1,35', '520402', '西秀区', '3');
INSERT INTO `sys_area` VALUES ('38', '35', '0,1,35', '520421', '平坝县', '3');
INSERT INTO `sys_area` VALUES ('39', '35', '0,1,35', '520422', '普定县', '3');
INSERT INTO `sys_area` VALUES ('40', '35', '0,1,35', '520423', '镇宁布依族苗族自治县', '3');
INSERT INTO `sys_area` VALUES ('41', '35', '0,1,35', '520424', '关岭布依族苗族自治县', '3');
INSERT INTO `sys_area` VALUES ('42', '35', '0,1,35', '520425', '紫云苗族布依族自治县', '3');
INSERT INTO `sys_area` VALUES ('43', '1', '0,1', '520500', '毕节市', '2');
INSERT INTO `sys_area` VALUES ('44', '43', '0,1,43', '520501', '市辖区', '3');
INSERT INTO `sys_area` VALUES ('45', '43', '0,1,43', '520502', '七星关区', '3');
INSERT INTO `sys_area` VALUES ('46', '43', '0,1,43', '520521', '大方县', '3');
INSERT INTO `sys_area` VALUES ('47', '43', '0,1,43', '520522', '黔西县', '3');
INSERT INTO `sys_area` VALUES ('48', '43', '0,1,43', '520523', '金沙县', '3');
INSERT INTO `sys_area` VALUES ('49', '43', '0,1,43', '520524', '织金县', '3');
INSERT INTO `sys_area` VALUES ('50', '43', '0,1,43', '520525', '纳雍县', '3');
INSERT INTO `sys_area` VALUES ('51', '43', '0,1,43', '520526', '威宁彝族回族苗族自治县', '3');
INSERT INTO `sys_area` VALUES ('52', '43', '0,1,43', '520527', '赫章县', '3');
INSERT INTO `sys_area` VALUES ('53', '1', '0,1', '520600', '铜仁市', '2');
INSERT INTO `sys_area` VALUES ('54', '53', '0,1,53', '520601', '市辖区', '3');
INSERT INTO `sys_area` VALUES ('55', '53', '0,1,53', '520602', '碧江区', '3');
INSERT INTO `sys_area` VALUES ('56', '53', '0,1,53', '520603', '万山区', '3');
INSERT INTO `sys_area` VALUES ('57', '53', '0,1,53', '520621', '江口县', '3');
INSERT INTO `sys_area` VALUES ('58', '53', '0,1,53', '520622', '玉屏侗族自治县', '3');
INSERT INTO `sys_area` VALUES ('59', '53', '0,1,53', '520623', '石阡县', '3');
INSERT INTO `sys_area` VALUES ('60', '53', '0,1,53', '520624', '思南县', '3');
INSERT INTO `sys_area` VALUES ('61', '53', '0,1,53', '520625', '印江土家族苗族自治县', '3');
INSERT INTO `sys_area` VALUES ('62', '53', '0,1,53', '520626', '德江县', '3');
INSERT INTO `sys_area` VALUES ('63', '53', '0,1,53', '520627', '沿河土家族自治县', '3');
INSERT INTO `sys_area` VALUES ('64', '53', '0,1,53', '520628', '松桃苗族自治县', '3');
INSERT INTO `sys_area` VALUES ('65', '1', '0,1', '522300', '黔西南布依族苗族自治州', '2');
INSERT INTO `sys_area` VALUES ('66', '65', '0,1,65', '522301', '兴义市', '3');
INSERT INTO `sys_area` VALUES ('67', '65', '0,1,65', '522322', '兴仁县', '3');
INSERT INTO `sys_area` VALUES ('68', '65', '0,1,65', '522323', '普安县', '3');
INSERT INTO `sys_area` VALUES ('69', '65', '0,1,65', '522324', '晴隆县', '3');
INSERT INTO `sys_area` VALUES ('70', '65', '0,1,65', '522325', '贞丰县', '3');
INSERT INTO `sys_area` VALUES ('71', '65', '0,1,65', '522326', '望谟县', '3');
INSERT INTO `sys_area` VALUES ('72', '65', '0,1,65', '522327', '册亨县', '3');
INSERT INTO `sys_area` VALUES ('73', '65', '0,1,65', '522328', '安龙县', '3');
INSERT INTO `sys_area` VALUES ('74', '1', '0,1', '522600', '黔东南苗族侗族自治州', '2');
INSERT INTO `sys_area` VALUES ('75', '74', '0,1,74', '522601', '凯里市', '3');
INSERT INTO `sys_area` VALUES ('76', '74', '0,1,74', '522622', '黄平县', '3');
INSERT INTO `sys_area` VALUES ('77', '74', '0,1,74', '522623', '施秉县', '3');
INSERT INTO `sys_area` VALUES ('78', '74', '0,1,74', '522624', '三穗县', '3');
INSERT INTO `sys_area` VALUES ('79', '74', '0,1,74', '522625', '镇远县', '3');
INSERT INTO `sys_area` VALUES ('80', '74', '0,1,74', '522626', '岑巩县', '3');
INSERT INTO `sys_area` VALUES ('81', '74', '0,1,74', '522627', '天柱县', '3');
INSERT INTO `sys_area` VALUES ('82', '74', '0,1,74', '522628', '锦屏县', '3');
INSERT INTO `sys_area` VALUES ('83', '74', '0,1,74', '522629', '剑河县', '3');
INSERT INTO `sys_area` VALUES ('84', '74', '0,1,74', '522630', '台江县', '3');
INSERT INTO `sys_area` VALUES ('85', '74', '0,1,74', '522631', '黎平县', '3');
INSERT INTO `sys_area` VALUES ('86', '74', '0,1,74', '522632', '榕江县', '3');
INSERT INTO `sys_area` VALUES ('87', '74', '0,1,74', '522633', '从江县', '3');
INSERT INTO `sys_area` VALUES ('88', '74', '0,1,74', '522634', '雷山县', '3');
INSERT INTO `sys_area` VALUES ('89', '74', '0,1,74', '522635', '麻江县', '3');
INSERT INTO `sys_area` VALUES ('90', '74', '0,1,74', '522636', '丹寨县', '3');
INSERT INTO `sys_area` VALUES ('91', '1', '0,1', '522700', '黔南布依族苗族自治州', '2');
INSERT INTO `sys_area` VALUES ('92', '91', '0,1,91', '522701', '都匀市', '3');
INSERT INTO `sys_area` VALUES ('93', '91', '0,1,91', '522702', '福泉市', '3');
INSERT INTO `sys_area` VALUES ('94', '91', '0,1,91', '522722', '荔波县', '3');
INSERT INTO `sys_area` VALUES ('95', '91', '0,1,91', '522723', '贵定县', '3');
INSERT INTO `sys_area` VALUES ('96', '91', '0,1,91', '522725', '瓮安县', '3');
INSERT INTO `sys_area` VALUES ('97', '91', '0,1,91', '522726', '独山县', '3');
INSERT INTO `sys_area` VALUES ('98', '91', '0,1,91', '522727', '平塘县', '3');
INSERT INTO `sys_area` VALUES ('99', '91', '0,1,91', '522728', '罗甸县', '3');
INSERT INTO `sys_area` VALUES ('100', '91', '0,1,91', '522729', '长顺县', '3');
INSERT INTO `sys_area` VALUES ('101', '91', '0,1,91', '522730', '龙里县', '3');
INSERT INTO `sys_area` VALUES ('102', '91', '0,1,91', '522731', '惠水县', '3');
INSERT INTO `sys_area` VALUES ('103', '91', '0,1,91', '522732', '三都水族自治县', '3');
