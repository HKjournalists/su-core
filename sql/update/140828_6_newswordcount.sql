/*
Navicat MySQL Data Transfer

Source Server         : GETTEC
Source Server Version : 50534
Source Host           : 127.0.0.1:3306
Source Database       : fooddata

Target Server Type    : MYSQL
Target Server Version : 50534
File Encoding         : 65001

Date: 2014-08-27 14:46:27
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `newswordcount`
-- ----------------------------
DROP TABLE IF EXISTS `newswordcount`;
CREATE TABLE `newswordcount` (
  `NewsID` bigint(20) NOT NULL,
  `WordID` int(11) NOT NULL,
  `TitleWordCount` int(11) DEFAULT NULL,
  `WordCount` int(11) DEFAULT NULL,
  `StartTime` datetime DEFAULT NULL,
  KEY `NewsID` (`NewsID`),
  CONSTRAINT `newswordcount_ibfk_1` FOREIGN KEY (`NewsID`) REFERENCES `news` (`NewsID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of newswordcount
-- ----------------------------
INSERT INTO `newswordcount` VALUES ('816145758', '2', '-1', '2', '2014-08-08 15:11:11');
INSERT INTO `newswordcount` VALUES ('816145758', '9', '-1', '12', '2014-08-08 15:11:11');
INSERT INTO `newswordcount` VALUES ('655145646', '3', '-1', '1', '2014-08-08 15:11:11');
INSERT INTO `newswordcount` VALUES ('1036145797', '3', '-1', '4', '2014-08-08 15:11:11');
INSERT INTO `newswordcount` VALUES ('658145664', '9', '-1', '1', '2014-08-08 15:11:11');
INSERT INTO `newswordcount` VALUES ('658145671', '3', '-1', '1', '2014-08-08 15:11:11');
INSERT INTO `newswordcount` VALUES ('658145671', '10', '-1', '1', '2014-08-08 15:11:11');
INSERT INTO `newswordcount` VALUES ('1714145858', '3', '-1', '2', '2014-08-08 17:16:06');
INSERT INTO `newswordcount` VALUES ('110230677603', '9', '-1', '1', '2014-08-13 14:06:21');
INSERT INTO `newswordcount` VALUES ('110230677603', '10', '-1', '1', '2014-08-13 14:06:21');
INSERT INTO `newswordcount` VALUES ('656146167', '3', '-1', '6', '2014-08-13 14:06:21');
INSERT INTO `newswordcount` VALUES ('655146200', '3', '-1', '1', '2014-08-13 14:06:21');
INSERT INTO `newswordcount` VALUES ('654146210', '3', '-1', '3', '2014-08-13 14:06:21');
INSERT INTO `newswordcount` VALUES ('654146224', '3', '-1', '2', '2014-08-13 14:06:21');
