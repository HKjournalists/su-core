/*
Navicat MySQL Data Transfer

Source Server         : GETTEC
Source Server Version : 50534
Source Host           : 127.0.0.1:3306
Source Database       : fooddata

Target Server Type    : MYSQL
Target Server Version : 50534
File Encoding         : 65001

Date: 2014-08-27 14:46:17
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `newsfoodclass`
-- ----------------------------
DROP TABLE IF EXISTS `newsfoodclass`;
CREATE TABLE `newsfoodclass` (
  `NewsID` bigint(20) NOT NULL,
  `FoodClassID` int(11) NOT NULL,
  `TitleFoodCount` int(11) DEFAULT NULL,
  `FoodCount` int(11) DEFAULT NULL,
  `StartTime` datetime DEFAULT NULL,
  KEY `NewsID` (`NewsID`),
  KEY `FoodClassID` (`FoodClassID`),
  CONSTRAINT `newsfoodclass_ibfk_1` FOREIGN KEY (`NewsID`) REFERENCES `news` (`NewsID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `newsfoodclass_ibfk_2` FOREIGN KEY (`FoodClassID`) REFERENCES `foodclasses` (`FoodClassID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of newsfoodclass
-- ----------------------------
INSERT INTO `newsfoodclass` VALUES ('816145758', '2', '-1', '24', '2014-08-08 15:11:11');
INSERT INTO `newsfoodclass` VALUES ('908145769', '2', '-1', '1', '2014-08-08 15:11:11');
INSERT INTO `newsfoodclass` VALUES ('657145666', '2', '-1', '3', '2014-08-08 15:11:11');
INSERT INTO `newsfoodclass` VALUES ('659145658', '2', '-1', '1', '2014-08-08 15:11:11');
INSERT INTO `newsfoodclass` VALUES ('121230678097', '2', '-1', '5', '2014-08-13 14:06:21');
INSERT INTO `newsfoodclass` VALUES ('110230677603', '2', '-1', '58', '2014-08-13 14:06:21');
INSERT INTO `newsfoodclass` VALUES ('1141146358', '2', '-1', '1', '2014-08-13 14:06:21');
INSERT INTO `newsfoodclass` VALUES ('1111146345', '2', '-1', '10', '2014-08-13 14:06:21');
INSERT INTO `newsfoodclass` VALUES ('1111146345', '3', '-1', '1', '2014-08-13 14:06:21');
INSERT INTO `newsfoodclass` VALUES ('950146313', '2', '-1', '3', '2014-08-13 14:06:21');
INSERT INTO `newsfoodclass` VALUES ('928146310', '2', '-1', '38', '2014-08-13 14:06:21');
INSERT INTO `newsfoodclass` VALUES ('728146286', '2', '-1', '1', '2014-08-13 14:06:21');
INSERT INTO `newsfoodclass` VALUES ('1000146315', '2', '-1', '4', '2014-08-13 14:06:21');
INSERT INTO `newsfoodclass` VALUES ('724146285', '2', '-1', '8', '2014-08-13 14:06:21');
INSERT INTO `newsfoodclass` VALUES ('724146285', '3', '-1', '1', '2014-08-13 14:06:21');
INSERT INTO `newsfoodclass` VALUES ('722146284', '2', '-1', '6', '2014-08-13 14:06:21');
INSERT INTO `newsfoodclass` VALUES ('656146167', '2', '-1', '3', '2014-08-13 14:06:21');
INSERT INTO `newsfoodclass` VALUES ('656146172', '2', '-1', '1', '2014-08-13 14:06:21');
INSERT INTO `newsfoodclass` VALUES ('655146202', '2', '-1', '1', '2014-08-13 14:06:21');
