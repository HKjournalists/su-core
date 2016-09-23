/*
Navicat MySQL Data Transfer

Source Server         : fsn
Source Server Version : 50534
Source Host           : 192.168.79.28:3306
Source Database       : fsn_cloud_stg

Target Server Type    : MYSQL
Target Server Version : 50534
File Encoding         : 65001

Date: 2015-01-26 16:36:46
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `hat_province`
-- ----------------------------
DROP TABLE IF EXISTS `hat_province`;
CREATE TABLE `hat_province` (
  `provinceID` varchar(6) DEFAULT NULL COMMENT '省id',
  `province` varchar(40) DEFAULT NULL COMMENT '省名称',
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `short_prov` varchar(10) DEFAULT NULL COMMENT '省简称',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=35 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of hat_province
-- ----------------------------
INSERT INTO `hat_province` VALUES ('110000', '北京市', '1', '京');
INSERT INTO `hat_province` VALUES ('120000', '天津市', '2', '津');
INSERT INTO `hat_province` VALUES ('130000', '河北省', '3', '冀');
INSERT INTO `hat_province` VALUES ('140000', '山西省', '4', '晋');
INSERT INTO `hat_province` VALUES ('150000', '内蒙古自治区', '5', '蒙');
INSERT INTO `hat_province` VALUES ('210000', '辽宁省', '6', '辽');
INSERT INTO `hat_province` VALUES ('220000', '吉林省', '7', '吉');
INSERT INTO `hat_province` VALUES ('230000', '黑龙江省', '8', '黑');
INSERT INTO `hat_province` VALUES ('310000', '上海市', '9', '沪');
INSERT INTO `hat_province` VALUES ('320000', '江苏省', '10', '苏');
INSERT INTO `hat_province` VALUES ('330000', '浙江省', '11', '浙');
INSERT INTO `hat_province` VALUES ('340000', '安徽省', '12', '皖');
INSERT INTO `hat_province` VALUES ('350000', '福建省', '13', '闽');
INSERT INTO `hat_province` VALUES ('360000', '江西省', '14', '赣');
INSERT INTO `hat_province` VALUES ('370000', '山东省', '15', '鲁');
INSERT INTO `hat_province` VALUES ('410000', '河南省', '16', '豫');
INSERT INTO `hat_province` VALUES ('420000', '湖北省', '17', '鄂');
INSERT INTO `hat_province` VALUES ('430000', '湖南省', '18', '湘');
INSERT INTO `hat_province` VALUES ('440000', '广东省', '19', '粤');
INSERT INTO `hat_province` VALUES ('450000', '广西壮族自治区', '20', '桂');
INSERT INTO `hat_province` VALUES ('460000', '海南省', '21', '琼');
INSERT INTO `hat_province` VALUES ('500000', '重庆市', '22', '渝');
INSERT INTO `hat_province` VALUES ('510000', '四川省', '23', '川');
INSERT INTO `hat_province` VALUES ('520000', '贵州省', '24', '黔');
INSERT INTO `hat_province` VALUES ('530000', '云南省', '25', '滇');
INSERT INTO `hat_province` VALUES ('540000', '西藏自治区', '26', '藏');
INSERT INTO `hat_province` VALUES ('610000', '陕西省', '27', '陕');
INSERT INTO `hat_province` VALUES ('620000', '甘肃省', '28', '甘');
INSERT INTO `hat_province` VALUES ('630000', '青海省', '29', '青');
INSERT INTO `hat_province` VALUES ('640000', '宁夏回族自治区', '30', '宁');
INSERT INTO `hat_province` VALUES ('650000', '新疆维吾尔自治区', '31', '新');
INSERT INTO `hat_province` VALUES ('710000', '台湾省', '32', '台');
INSERT INTO `hat_province` VALUES ('810000', '香港特别行政区', '33', '港');
INSERT INTO `hat_province` VALUES ('820000', '澳门特别行政区', '34', '澳');
