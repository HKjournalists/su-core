/*
Navicat MySQL Data Transfer

Source Server         : GETTEC
Source Server Version : 50534
Source Host           : 127.0.0.1:3306
Source Database       : fooddata

Target Server Type    : MYSQL
Target Server Version : 50534
File Encoding         : 65001

Date: 2014-08-27 14:46:00
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `dictionary`
-- ----------------------------
DROP TABLE IF EXISTS `dictionary`;
CREATE TABLE `dictionary` (
  `WordID` int(11) NOT NULL AUTO_INCREMENT,
  `Word` varchar(64) NOT NULL,
  PRIMARY KEY (`WordID`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of dictionary
-- ----------------------------
INSERT INTO `dictionary` VALUES ('1', '安赛蜜');
INSERT INTO `dictionary` VALUES ('2', '大肠菌群');
INSERT INTO `dictionary` VALUES ('3', '蛋白质');
INSERT INTO `dictionary` VALUES ('4', '铬*');
INSERT INTO `dictionary` VALUES ('5', '环己基氨基磺酸钠（甜蜜素）');
INSERT INTO `dictionary` VALUES ('6', '黄曲霉毒素B1');
INSERT INTO `dictionary` VALUES ('7', '黄曲霉毒素M1');
INSERT INTO `dictionary` VALUES ('8', '金黄色葡萄球菌');
INSERT INTO `dictionary` VALUES ('9', '菌落总数');
INSERT INTO `dictionary` VALUES ('10', '铅');
INSERT INTO `dictionary` VALUES ('11', '铅（Pb）');
INSERT INTO `dictionary` VALUES ('12', '三聚氰胺');
INSERT INTO `dictionary` VALUES ('13', '沙门氏菌');
INSERT INTO `dictionary` VALUES ('14', '糖精钠');
INSERT INTO `dictionary` VALUES ('15', '甜蜜素');
INSERT INTO `dictionary` VALUES ('16', '亚硝酸盐');
INSERT INTO `dictionary` VALUES ('17', '亚硝酸盐（以亚硝酸钠计）');
INSERT INTO `dictionary` VALUES ('18', '总砷');
INSERT INTO `dictionary` VALUES ('19', '总砷（以As计）');
