/*
Navicat MySQL Data Transfer

Source Server         : GETTEC
Source Server Version : 50534
Source Host           : 127.0.0.1:3306
Source Database       : fooddata

Target Server Type    : MYSQL
Target Server Version : 50534
File Encoding         : 65001

Date: 2014-08-27 14:46:06
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `foodclasses`
-- ----------------------------
DROP TABLE IF EXISTS `foodclasses`;
CREATE TABLE `foodclasses` (
  `FoodClassID` int(11) NOT NULL AUTO_INCREMENT,
  `FoodClassName` varchar(20) NOT NULL,
  `ParentFoodClassID` int(11) NOT NULL,
  PRIMARY KEY (`FoodClassID`)
) ENGINE=InnoDB AUTO_INCREMENT=29 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of foodclasses
-- ----------------------------
INSERT INTO `foodclasses` VALUES ('1', '未知食品', '0');
INSERT INTO `foodclasses` VALUES ('2', '食品', '1');
INSERT INTO `foodclasses` VALUES ('3', '乳制品', '2');
INSERT INTO `foodclasses` VALUES ('4', '婴幼儿食品', '2');
INSERT INTO `foodclasses` VALUES ('5', '较大婴儿和幼儿配方食品', '3');
INSERT INTO `foodclasses` VALUES ('6', '酸奶', '3');
INSERT INTO `foodclasses` VALUES ('7', '鲜牛奶', '3');
INSERT INTO `foodclasses` VALUES ('8', '婴儿配方食品', '3');
INSERT INTO `foodclasses` VALUES ('9', '发酵乳', '4');
INSERT INTO `foodclasses` VALUES ('10', '灭菌乳', '4');
INSERT INTO `foodclasses` VALUES ('11', '奶粉', '3');
INSERT INTO `foodclasses` VALUES ('12', '乳基粉状', '4');
INSERT INTO `foodclasses` VALUES ('13', '乳基婴儿配方食品', '4');
INSERT INTO `foodclasses` VALUES ('14', '中老年食品', '2');
INSERT INTO `foodclasses` VALUES ('15', '发酵乳（未经热处理）', '5');
INSERT INTO `foodclasses` VALUES ('16', '风味发酵乳（经热处理）', '5');
INSERT INTO `foodclasses` VALUES ('17', '风味发酵乳（未经热处理）', '5');
INSERT INTO `foodclasses` VALUES ('18', '加蔬菜和水果', '5');
INSERT INTO `foodclasses` VALUES ('19', '较大婴儿和幼儿配方奶粉', '4');
INSERT INTO `foodclasses` VALUES ('20', '灭菌乳', '4');
INSERT INTO `foodclasses` VALUES ('21', '牛乳', '5');
INSERT INTO `foodclasses` VALUES ('22', '其他乳粉', '5');
INSERT INTO `foodclasses` VALUES ('23', '其他乳制品', '3');
INSERT INTO `foodclasses` VALUES ('24', '全脂、脱脂、部分脱脂乳粉', '4');
INSERT INTO `foodclasses` VALUES ('25', '乳基粉状产品', '5');
INSERT INTO `foodclasses` VALUES ('26', '调制乳', '4');
INSERT INTO `foodclasses` VALUES ('27', '婴儿配方奶粉', '4');
INSERT INTO `foodclasses` VALUES ('28', '中老年奶粉', '3');
