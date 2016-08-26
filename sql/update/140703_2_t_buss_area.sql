/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50534
Source Host           : localhost:3306
Source Database       : fsn_cloud_qa

Target Server Type    : MYSQL
Target Server Version : 50534
File Encoding         : 65001

Date: 2014-07-03 18:11:34
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `t_buss_area`
-- ----------------------------
DROP TABLE IF EXISTS `t_buss_area`;
CREATE TABLE `t_buss_area` (
  `ID` int(20) NOT NULL AUTO_INCREMENT,
  `AREA_NAME` varchar(225) NOT NULL,
  `LEVEL` int(20) DEFAULT NULL,
  `AREA_CODE` varchar(225) DEFAULT NULL,
  `PARENT_ID` int(20) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=117 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_buss_area
-- ----------------------------
INSERT INTO `t_buss_area` VALUES ('1', '贵州省', '1', 'Q', '0');
INSERT INTO `t_buss_area` VALUES ('2', '贵阳市', '2', 'A', '1');
INSERT INTO `t_buss_area` VALUES ('3', '市辖区', '3', 'AZ', '2');
INSERT INTO `t_buss_area` VALUES ('4', '南明区', '3', 'AA', '2');
INSERT INTO `t_buss_area` VALUES ('5', '云岩区', '3', 'AB', '2');
INSERT INTO `t_buss_area` VALUES ('6', '花溪区', '3', 'AC', '2');
INSERT INTO `t_buss_area` VALUES ('7', '乌当区', '3', 'AD', '2');
INSERT INTO `t_buss_area` VALUES ('8', '白云区', '3', 'AE', '2');
INSERT INTO `t_buss_area` VALUES ('9', '小河区', '3', 'AF', '2');
INSERT INTO `t_buss_area` VALUES ('10', '开阳县', '3', 'AG', '2');
INSERT INTO `t_buss_area` VALUES ('11', '息烽县', '3', 'AH', '2');
INSERT INTO `t_buss_area` VALUES ('12', '修文县', '3', 'AI', '2');
INSERT INTO `t_buss_area` VALUES ('13', '清镇市', '3', 'AJ', '2');
INSERT INTO `t_buss_area` VALUES ('14', '六盘水市', '2', 'B', '1');
INSERT INTO `t_buss_area` VALUES ('15', '钟山区', '3', 'BA', '14');
INSERT INTO `t_buss_area` VALUES ('16', '六枝特区', '3', 'BB', '14');
INSERT INTO `t_buss_area` VALUES ('17', '水城县', '3', 'BC', '14');
INSERT INTO `t_buss_area` VALUES ('18', '盘县', '3', 'BD', '14');
INSERT INTO `t_buss_area` VALUES ('19', '遵义市', '2', 'C', '1');
INSERT INTO `t_buss_area` VALUES ('20', '市辖区', '3', 'CA', '19');
INSERT INTO `t_buss_area` VALUES ('21', '红花岗区', '3', 'CB', '19');
INSERT INTO `t_buss_area` VALUES ('22', '汇川区', '3', 'CC', '19');
INSERT INTO `t_buss_area` VALUES ('23', '遵义县', '3', 'CD', '19');
INSERT INTO `t_buss_area` VALUES ('24', '桐梓县', '3', 'CE', '19');
INSERT INTO `t_buss_area` VALUES ('25', '绥阳县', '3', 'CF', '19');
INSERT INTO `t_buss_area` VALUES ('26', '正安县', '3', 'CG', '19');
INSERT INTO `t_buss_area` VALUES ('27', '道真仡佬族苗族自治县', '3', 'CH', '19');
INSERT INTO `t_buss_area` VALUES ('28', '务川仡佬族苗族自治县', '3', 'CI', '19');
INSERT INTO `t_buss_area` VALUES ('29', '凤冈县', '3', 'CJ', '19');
INSERT INTO `t_buss_area` VALUES ('30', '湄潭县', '3', 'CK', '19');
INSERT INTO `t_buss_area` VALUES ('31', '余庆县', '3', 'CL', '19');
INSERT INTO `t_buss_area` VALUES ('32', '习水县', '3', 'CM', '19');
INSERT INTO `t_buss_area` VALUES ('33', '赤水市', '3', 'CN', '19');
INSERT INTO `t_buss_area` VALUES ('34', '仁怀市', '3', 'CO', '19');
INSERT INTO `t_buss_area` VALUES ('35', '安顺市', '2', 'G', '1');
INSERT INTO `t_buss_area` VALUES ('36', '市辖区', '3', 'GA', '35');
INSERT INTO `t_buss_area` VALUES ('37', '西秀区', '3', 'GB', '35');
INSERT INTO `t_buss_area` VALUES ('38', '平坝县', '3', 'GC', '35');
INSERT INTO `t_buss_area` VALUES ('39', '普定县', '3', 'GD', '35');
INSERT INTO `t_buss_area` VALUES ('40', '镇宁布依族苗族自治县', '3', 'GE', '35');
INSERT INTO `t_buss_area` VALUES ('41', '关岭布依族苗族自治县', '3', 'GF', '35');
INSERT INTO `t_buss_area` VALUES ('42', '紫云苗族布依族自治县', '3', 'GH', '35');
INSERT INTO `t_buss_area` VALUES ('43', '铜仁市', '2', 'D', '1');
INSERT INTO `t_buss_area` VALUES ('44', '铜仁市', '3', 'DA', '43');
INSERT INTO `t_buss_area` VALUES ('45', '江口县', '3', 'DB', '43');
INSERT INTO `t_buss_area` VALUES ('46', '玉屏侗族自治县', '3', 'DC', '43');
INSERT INTO `t_buss_area` VALUES ('47', '石阡县', '3', 'DE', '43');
INSERT INTO `t_buss_area` VALUES ('48', '思南县', '3', 'DF', '43');
INSERT INTO `t_buss_area` VALUES ('49', '印江土家族苗族自治县', '3', 'DG', '43');
INSERT INTO `t_buss_area` VALUES ('50', '德江县', '3', 'DH', '43');
INSERT INTO `t_buss_area` VALUES ('51', '沿河土家族自治县', '3', 'DI', '43');
INSERT INTO `t_buss_area` VALUES ('52', '松桃苗族自治县', '3', 'DJ', '43');
INSERT INTO `t_buss_area` VALUES ('53', '万山特区', '3', 'DK', '43');
INSERT INTO `t_buss_area` VALUES ('54', '黔西南自治州', '2', 'E', '1');
INSERT INTO `t_buss_area` VALUES ('55', '兴义市', '3', 'EA', '54');
INSERT INTO `t_buss_area` VALUES ('56', '兴仁县', '3', 'EB', '54');
INSERT INTO `t_buss_area` VALUES ('57', '普安县', '3', 'EC', '54');
INSERT INTO `t_buss_area` VALUES ('58', '晴隆县', '3', 'ED', '54');
INSERT INTO `t_buss_area` VALUES ('59', '贞丰县', '3', 'EE', '54');
INSERT INTO `t_buss_area` VALUES ('60', '望谟县', '3', 'EF', '54');
INSERT INTO `t_buss_area` VALUES ('61', '册亨县', '3', 'EH', '54');
INSERT INTO `t_buss_area` VALUES ('62', '安龙县', '3', 'EI', '54');
INSERT INTO `t_buss_area` VALUES ('63', '毕节市', '2', 'F', '1');
INSERT INTO `t_buss_area` VALUES ('64', '七星关区', '3', 'FA', '63');
INSERT INTO `t_buss_area` VALUES ('65', '大方县', '3', 'FB', '63');
INSERT INTO `t_buss_area` VALUES ('66', '黔西县', '3', 'FC', '63');
INSERT INTO `t_buss_area` VALUES ('67', '金沙县', '3', 'FD', '63');
INSERT INTO `t_buss_area` VALUES ('68', '织金县', '3', 'FE', '63');
INSERT INTO `t_buss_area` VALUES ('69', '纳雍县', '3', 'FF', '63');
INSERT INTO `t_buss_area` VALUES ('70', '威宁彝族回族苗族自治县', '3', 'FG', '63');
INSERT INTO `t_buss_area` VALUES ('71', '赫章县', '3', 'FH', '63');
INSERT INTO `t_buss_area` VALUES ('72', '黔东南自治州', '2', 'H', '1');
INSERT INTO `t_buss_area` VALUES ('73', '凯里市', '3', 'HA', '72');
INSERT INTO `t_buss_area` VALUES ('74', '黄平县', '3', 'HB', '72');
INSERT INTO `t_buss_area` VALUES ('75', '施秉县', '3', 'HC', '72');
INSERT INTO `t_buss_area` VALUES ('76', '三穗县', '3', 'HD', '72');
INSERT INTO `t_buss_area` VALUES ('77', '镇远县', '3', 'HE', '72');
INSERT INTO `t_buss_area` VALUES ('78', '岑巩县', '3', 'HF', '72');
INSERT INTO `t_buss_area` VALUES ('79', '天柱县', '3', 'HG', '72');
INSERT INTO `t_buss_area` VALUES ('80', '锦屏县', '3', 'HH', '72');
INSERT INTO `t_buss_area` VALUES ('81', '剑河县', '3', 'HI', '72');
INSERT INTO `t_buss_area` VALUES ('82', '台江县', '3', 'HJ', '72');
INSERT INTO `t_buss_area` VALUES ('83', '黎平县', '3', 'HK', '72');
INSERT INTO `t_buss_area` VALUES ('84', '榕江县', '3', 'HL', '72');
INSERT INTO `t_buss_area` VALUES ('85', '从江县', '3', 'HM', '72');
INSERT INTO `t_buss_area` VALUES ('86', '雷山县', '3', 'HN', '72');
INSERT INTO `t_buss_area` VALUES ('87', '麻江县', '3', 'HO', '72');
INSERT INTO `t_buss_area` VALUES ('88', '丹寨县', '3', 'HP', '72');
INSERT INTO `t_buss_area` VALUES ('89', '黔南自治州', '2', 'J', '1');
INSERT INTO `t_buss_area` VALUES ('90', '都匀市', '3', 'JA', '89');
INSERT INTO `t_buss_area` VALUES ('91', '福泉市', '3', 'JB', '89');
INSERT INTO `t_buss_area` VALUES ('92', '荔波县', '3', 'JC', '89');
INSERT INTO `t_buss_area` VALUES ('93', '贵定县', '3', 'JD', '89');
INSERT INTO `t_buss_area` VALUES ('94', '瓮安县', '3', 'JE', '89');
INSERT INTO `t_buss_area` VALUES ('95', '独山县', '3', 'JF', '89');
INSERT INTO `t_buss_area` VALUES ('96', '平塘县', '3', 'JG', '89');
INSERT INTO `t_buss_area` VALUES ('97', '罗甸县', '3', 'JH', '89');
INSERT INTO `t_buss_area` VALUES ('98', '长顺县', '3', 'JI', '89');
INSERT INTO `t_buss_area` VALUES ('99', '龙里县', '3', 'JJ', '89');
INSERT INTO `t_buss_area` VALUES ('100', '惠水县', '3', 'JK', '89');
INSERT INTO `t_buss_area` VALUES ('101', '三都水族自治县', '3', 'JL', '89');
INSERT INTO `t_buss_area` VALUES ('102', '观山湖区', '3', 'AK', '2');
INSERT INTO `t_buss_area` VALUES ('103', '钟山开发区', '3', 'BE', '14');
INSERT INTO `t_buss_area` VALUES ('104', '红果开发区', '3', 'BF', '14');
INSERT INTO `t_buss_area` VALUES ('105', '经济技术开发区', '3', 'GI', '35');
INSERT INTO `t_buss_area` VALUES ('106', '黄果树区', '3', 'GJ', '35');
INSERT INTO `t_buss_area` VALUES ('107', '大龙开发区', '3', 'DL', '43');
INSERT INTO `t_buss_area` VALUES ('108', '顶效市', '3', 'EJ', '63');
INSERT INTO `t_buss_area` VALUES ('109', '百里杜鹃风景名胜区', '3', 'FI', '63');
INSERT INTO `t_buss_area` VALUES ('110', '凯里经济技术开发区', '3', 'HQ', '72');
INSERT INTO `t_buss_area` VALUES ('111', '黔南州经济开发区', '3', 'JM', '89');
INSERT INTO `t_buss_area` VALUES ('112', '都匀市经济开发区', '3', 'JN', '89');
INSERT INTO `t_buss_area` VALUES ('113', '贵安新区', '2', 'X', '1');
INSERT INTO `t_buss_area` VALUES ('114', '威宁', '2', 'W', '1');
INSERT INTO `t_buss_area` VALUES ('115', '仁怀', '2', 'R', '1');
INSERT INTO `t_buss_area` VALUES ('116', '中心', '2', 'Z', '1');
