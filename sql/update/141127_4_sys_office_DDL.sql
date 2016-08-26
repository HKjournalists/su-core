/*
Navicat MySQL Data Transfer

Source Server         : local
Source Server Version : 50534
Source Host           : localhost:3306
Source Database       : fsn_core

Target Server Type    : MYSQL
Target Server Version : 50534
File Encoding         : 65001

Date: 2014-11-27 17:47:20
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `sys_office`
-- ----------------------------
DROP TABLE IF EXISTS `sys_office`;
CREATE TABLE `sys_office` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `parent_id` bigint(20) DEFAULT '0' COMMENT '父级编号',
  `parent_ids` varchar(2000) CHARACTER SET utf8 DEFAULT '' COMMENT '所有父级编号',
  `area_id` bigint(20) DEFAULT '0' COMMENT '归属区域',
  `code` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '区域编码',
  `name` varchar(100) CHARACTER SET utf8 DEFAULT '' COMMENT '机构名称',
  `type` char(1) CHARACTER SET utf8 DEFAULT '' COMMENT '机构类型',
  `dqjbxh` char(1) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '地区级别编号  考核指标会用到',
  PRIMARY KEY (`id`),
  KEY `sys_office_parent_id` (`parent_id`)
) ENGINE=InnoDB AUTO_INCREMENT=88 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='机构表';

-- ----------------------------
-- Records of sys_office
-- ----------------------------
INSERT INTO `sys_office` VALUES ('1', '0', '0', '1', '520000', '贵州省食药监局流通处', '1', null);
INSERT INTO `sys_office` VALUES ('2', '1', '0,1', '2', '520100', '贵阳市食药监局流通处', '2', 'A');
INSERT INTO `sys_office` VALUES ('3', '2', '0,1,2', '6', '520111', '贵阳市花溪区食药监局流通处', '3', 'A');
INSERT INTO `sys_office` VALUES ('4', '2', '0,1,2', '7', '520112', '贵阳市乌当区食药监局流通处', '3', 'A');
INSERT INTO `sys_office` VALUES ('5', '2', '0,1,2', '8', '520113', '贵阳市白云区食药监局流通处', '3', 'A');
INSERT INTO `sys_office` VALUES ('6', '2', '0,1,2', '3', '520101', '贵阳市小河区食药监局流通处', '3', 'A');
INSERT INTO `sys_office` VALUES ('7', '2', '0,1,2', '10', '520121', '贵阳市开阳县食药监局流通处', '3', 'A');
INSERT INTO `sys_office` VALUES ('8', '2', '0,1,2', '11', '520122', '贵阳市息烽县食药监局流通处', '3', 'A');
INSERT INTO `sys_office` VALUES ('9', '2', '0,1,2', '12', '520123', '贵阳市修文县食药监局流通处', '3', 'A');
INSERT INTO `sys_office` VALUES ('10', '2', '0,1,2', '13', '520181', '贵阳市清镇市食药监局流通处', '3', 'A');
INSERT INTO `sys_office` VALUES ('11', '1', '0,1', '14', '520200', '六盘水市食药监局流通处', '2', 'B');
INSERT INTO `sys_office` VALUES ('12', '11', '0,1,11', '18', '520222', '盘县食药监局流通处', '3', 'B');
INSERT INTO `sys_office` VALUES ('13', '11', '0,1,11', '16', '520203', '六枝特区食药监局流通处', '3', 'B');
INSERT INTO `sys_office` VALUES ('14', '11', '0,1,11', '17', '520221', '水城县食药监局流通处', '3', 'B');
INSERT INTO `sys_office` VALUES ('15', '1', '0,1', '19', '520300', '遵义市食药监局流通处', '2', 'C');
INSERT INTO `sys_office` VALUES ('16', '15', '0,1,15', '33', '520381', '赤水市食药监局流通处', '3', 'C');
INSERT INTO `sys_office` VALUES ('17', '15', '0,1,15', '34', '520382', '仁怀市食药监局流通处', '3', 'C');
INSERT INTO `sys_office` VALUES ('18', '15', '0,1,15', '23', '520321', '遵义县食药监局流通处', '3', 'C');
INSERT INTO `sys_office` VALUES ('19', '15', '0,1,15', '24', '520322', '桐梓县食药监局流通处', '3', 'C');
INSERT INTO `sys_office` VALUES ('20', '15', '0,1,15', '25', '520323', '绥阳县食药监局流通处', '3', 'C');
INSERT INTO `sys_office` VALUES ('21', '15', '0,1,15', '26', '520324', '正安县食药监局流通处', '3', 'C');
INSERT INTO `sys_office` VALUES ('22', '15', '0,1,15', '27', '520325', '道真县食药监局流通处', '3', 'C');
INSERT INTO `sys_office` VALUES ('23', '15', '0,1,15', '28', '520326', '务川县食药监局流通处', '3', 'C');
INSERT INTO `sys_office` VALUES ('24', '15', '0,1,15', '29', '520327', '凤冈县食药监局流通处', '3', 'C');
INSERT INTO `sys_office` VALUES ('25', '15', '0,1,15', '30', '520328', '湄潭县食药监局流通处', '3', 'C');
INSERT INTO `sys_office` VALUES ('26', '15', '0,1,15', '31', '520329', '余庆县食药监局流通处', '3', 'C');
INSERT INTO `sys_office` VALUES ('27', '15', '0,1,15', '32', '520330', '习水县食药监局流通处', '3', 'C');
INSERT INTO `sys_office` VALUES ('28', '1', '0,1', '53', '520600', '铜仁地区食药监局流通处', '2', 'D');
INSERT INTO `sys_office` VALUES ('29', '28', '0,1,28', '57', '520621', '江口县食药监局流通处', '3', 'D');
INSERT INTO `sys_office` VALUES ('30', '28', '0,1,28', '58', '520622', '玉屏县食药监局流通处', '3', 'D');
INSERT INTO `sys_office` VALUES ('31', '28', '0,1,28', '59', '520623', '石阡县食药监局流通处', '3', 'D');
INSERT INTO `sys_office` VALUES ('32', '28', '0,1,28', '60', '520624', '思南县食药监局流通处', '3', 'D');
INSERT INTO `sys_office` VALUES ('33', '28', '0,1,28', '61', '520625', '印江县食药监局流通处', '3', 'D');
INSERT INTO `sys_office` VALUES ('34', '28', '0,1,28', '62', '520626', '德江县食药监局流通处', '3', 'D');
INSERT INTO `sys_office` VALUES ('35', '28', '0,1,28', '63', '520627', '沿河县食药监局流通处', '3', 'D');
INSERT INTO `sys_office` VALUES ('36', '28', '0,1,28', '64', '520628', '松桃县食药监局流通处', '3', 'D');
INSERT INTO `sys_office` VALUES ('37', '28', '0,1,28', '55', '520603', '万山特区食药监局流通处', '3', 'D');
INSERT INTO `sys_office` VALUES ('38', '1', '0,1', '65', '522300', '黔西南州食药监局流通处', '2', 'E');
INSERT INTO `sys_office` VALUES ('39', '38', '0,1,38', '67', '522322', '兴仁县食药监局流通处', '3', 'E');
INSERT INTO `sys_office` VALUES ('40', '38', '0,1,38', '68', '522323', '普安县食药监局流通处', '3', 'E');
INSERT INTO `sys_office` VALUES ('41', '38', '0,1,38', '69', '522324', '晴隆县食药监局流通处', '3', 'E');
INSERT INTO `sys_office` VALUES ('42', '38', '0,1,38', '70', '522325', '贞丰县食药监局流通处', '3', 'E');
INSERT INTO `sys_office` VALUES ('43', '38', '0,1,38', '71', '522326', '望谟县食药监局流通处', '3', 'E');
INSERT INTO `sys_office` VALUES ('44', '38', '0,1,38', '72', '522327', '册亨县食药监局流通处', '3', 'E');
INSERT INTO `sys_office` VALUES ('45', '38', '0,1,38', '73', '522328', '安龙县食药监局流通处', '3', 'E');
INSERT INTO `sys_office` VALUES ('46', '1', '0,1', '43', '520500', '毕节地区食药监局流通处', '2', 'F');
INSERT INTO `sys_office` VALUES ('47', '46', '0,1,46', '46', '520521', '大方县食药监局流通处', '3', 'F');
INSERT INTO `sys_office` VALUES ('48', '46', '0,1,46', '47', '520522', '黔西县食药监局流通处', '3', 'F');
INSERT INTO `sys_office` VALUES ('49', '46', '0,1,46', '48', '520523', '金沙县食药监局流通处', '3', 'F');
INSERT INTO `sys_office` VALUES ('50', '46', '0,1,46', '49', '520524', '织金县食药监局流通处', '3', 'F');
INSERT INTO `sys_office` VALUES ('51', '46', '0,1,46', '50', '520525', '纳雍县食药监局流通处', '3', 'F');
INSERT INTO `sys_office` VALUES ('52', '46', '0,1,46', '51', '520526', '威宁县食药监局流通处', '3', 'F');
INSERT INTO `sys_office` VALUES ('53', '46', '0,1,46', '52', '520527', '赫章县食药监局流通处', '3', 'F');
INSERT INTO `sys_office` VALUES ('54', '1', '0,1', '35', '520400', '安顺市食药监局流通处', '2', 'G');
INSERT INTO `sys_office` VALUES ('55', '54', '0,1,54', '38', '520421', '平坝县食药监局流通处', '3', 'G');
INSERT INTO `sys_office` VALUES ('56', '54', '0,1,54', '39', '520422', '普定县食药监局流通处', '3', 'G');
INSERT INTO `sys_office` VALUES ('57', '54', '0,1,54', '41', '520424', '关岭县食药监局流通处', '3', 'G');
INSERT INTO `sys_office` VALUES ('58', '54', '0,1,54', '40', '520423', '镇宁县食药监局流通处', '3', 'G');
INSERT INTO `sys_office` VALUES ('59', '54', '0,1,54', '42', '520425', '紫云县食药监局流通处', '3', 'G');
INSERT INTO `sys_office` VALUES ('60', '1', '0,1', '74', '522600', '黔东南州食药监局流通处', '2', 'H');
INSERT INTO `sys_office` VALUES ('61', '60', '0,1,60', '76', '522622', '黄平县食药监局流通处', '3', 'H');
INSERT INTO `sys_office` VALUES ('62', '60', '0,1,60', '77', '522623', '施秉县食药监局流通处', '3', 'H');
INSERT INTO `sys_office` VALUES ('63', '60', '0,1,60', '78', '522624', '三穗县食药监局流通处', '3', 'H');
INSERT INTO `sys_office` VALUES ('64', '60', '0,1,60', '79', '522625', '镇远县食药监局流通处', '3', 'H');
INSERT INTO `sys_office` VALUES ('65', '60', '0,1,60', '80', '522626', '岑巩县食药监局流通处', '3', 'H');
INSERT INTO `sys_office` VALUES ('66', '60', '0,1,60', '81', '522627', '天柱县食药监局流通处', '3', 'H');
INSERT INTO `sys_office` VALUES ('67', '60', '0,1,60', '82', '522628', '锦屏县食药监局流通处', '3', 'H');
INSERT INTO `sys_office` VALUES ('68', '60', '0,1,60', '83', '522629', '剑河县食药监局流通处', '3', 'H');
INSERT INTO `sys_office` VALUES ('69', '60', '0,1,60', '84', '522630', '台江县食药监局流通处', '3', 'H');
INSERT INTO `sys_office` VALUES ('70', '60', '0,1,60', '85', '522631', '黎平县食药监局流通处', '3', 'H');
INSERT INTO `sys_office` VALUES ('71', '60', '0,1,60', '86', '522632', '榕江县食药监局流通处', '3', 'H');
INSERT INTO `sys_office` VALUES ('72', '60', '0,1,60', '87', '522633', '从江县食药监局流通处', '3', 'H');
INSERT INTO `sys_office` VALUES ('73', '60', '0,1,60', '88', '522634', '雷山县食药监局流通处', '3', 'H');
INSERT INTO `sys_office` VALUES ('74', '60', '0,1,60', '89', '522635', '麻江县食药监局流通处', '3', 'H');
INSERT INTO `sys_office` VALUES ('75', '60', '0,1,60', '90', '522636', '丹寨县食药监局流通处', '3', 'H');
INSERT INTO `sys_office` VALUES ('76', '1', '0,1', '91', '522700', '黔南州食药监局流通处', '2', 'J');
INSERT INTO `sys_office` VALUES ('77', '76', '0,1,76', '94', '522722', '荔波县食药监局流通处', '3', 'J');
INSERT INTO `sys_office` VALUES ('78', '76', '0,1,76', '95', '522723', '贵定县食药监局流通处', '3', 'J');
INSERT INTO `sys_office` VALUES ('79', '76', '0,1,76', '93', '522702', '福泉市食药监局流通处', '3', 'J');
INSERT INTO `sys_office` VALUES ('80', '76', '0,1,76', '96', '522725', '瓮安县食药监局流通处', '3', 'J');
INSERT INTO `sys_office` VALUES ('81', '76', '0,1,76', '97', '522726', '独山县食药监局流通处', '3', 'J');
INSERT INTO `sys_office` VALUES ('82', '76', '0,1,76', '98', '522727', '平塘县食药监局流通处', '3', 'J');
INSERT INTO `sys_office` VALUES ('83', '76', '0,1,76', '99', '522728', '罗甸县食药监局流通处', '3', 'J');
INSERT INTO `sys_office` VALUES ('84', '76', '0,1,76', '100', '522729', '长顺县食药监局流通处', '3', 'J');
INSERT INTO `sys_office` VALUES ('85', '76', '0,1,76', '101', '522730', '龙里县食药监局流通处', '3', 'J');
INSERT INTO `sys_office` VALUES ('86', '76', '0,1,76', '102', '522731', '惠水县食药监局流通处', '3', 'J');
INSERT INTO `sys_office` VALUES ('87', '76', '0,1,76', '103', '522732', '三都县食药监局流通处', '3', 'J');
