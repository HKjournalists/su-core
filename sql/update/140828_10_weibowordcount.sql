/*
Navicat MySQL Data Transfer

Source Server         : GETTEC
Source Server Version : 50534
Source Host           : 127.0.0.1:3306
Source Database       : fooddata

Target Server Type    : MYSQL
Target Server Version : 50534
File Encoding         : 65001

Date: 2014-08-27 14:46:53
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `weibowordcount`
-- ----------------------------
DROP TABLE IF EXISTS `weibowordcount`;
CREATE TABLE `weibowordcount` (
  `WeiboID` bigint(20) NOT NULL,
  `WordID` int(11) NOT NULL,
  `WordCount` int(11) DEFAULT NULL,
  `StartTime` datetime DEFAULT NULL,
  KEY `WeiboID` (`WeiboID`),
  CONSTRAINT `weibowordcount_ibfk_1` FOREIGN KEY (`WeiboID`) REFERENCES `weibo` (`WeiboID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of weibowordcount
-- ----------------------------
INSERT INTO `weibowordcount` VALUES ('3741311410249289', '3', '1', '2014-08-08 15:06:53');
INSERT INTO `weibowordcount` VALUES ('3741311586663153', '3', '1', '2014-08-08 15:07:53');
INSERT INTO `weibowordcount` VALUES ('3741314925432146', '3', '2', '2014-08-08 15:21:35');
INSERT INTO `weibowordcount` VALUES ('3741316783176291', '3', '1', '2014-08-08 15:28:03');
INSERT INTO `weibowordcount` VALUES ('3741317131949025', '10', '1', '2014-08-08 15:29:32');
INSERT INTO `weibowordcount` VALUES ('3741318214095733', '3', '1', '2014-08-08 15:34:10');
INSERT INTO `weibowordcount` VALUES ('3741318998022300', '3', '2', '2014-08-08 15:37:19');
INSERT INTO `weibowordcount` VALUES ('3741319803511469', '3', '1', '2014-08-08 15:40:29');
INSERT INTO `weibowordcount` VALUES ('3741325138293126', '3', '1', '2014-08-08 16:01:28');
INSERT INTO `weibowordcount` VALUES ('3741326032061173', '9', '2', '2014-08-08 16:05:08');
INSERT INTO `weibowordcount` VALUES ('3741326304515560', '3', '1', '2014-08-08 16:06:13');
INSERT INTO `weibowordcount` VALUES ('3741327261097190', '3', '3', '2014-08-08 16:10:12');
INSERT INTO `weibowordcount` VALUES ('3741331228637929', '3', '1', '2014-08-08 16:26:03');
INSERT INTO `weibowordcount` VALUES ('3741331471544425', '3', '1', '2014-08-08 16:27:03');
INSERT INTO `weibowordcount` VALUES ('3741332788826261', '3', '1', '2014-08-08 16:32:21');
INSERT INTO `weibowordcount` VALUES ('3741333350925471', '3', '1', '2014-08-08 16:34:41');
INSERT INTO `weibowordcount` VALUES ('3741333350925307', '13', '3', '2014-08-08 16:34:41');
INSERT INTO `weibowordcount` VALUES ('3741337381560542', '3', '1', '2014-08-08 16:50:23');
INSERT INTO `weibowordcount` VALUES ('3741341537842808', '8', '1', '2014-08-08 17:07:14');
INSERT INTO `weibowordcount` VALUES ('3741343035896641', '3', '1', '2014-08-08 17:13:09');
INSERT INTO `weibowordcount` VALUES ('3741343190779795', '10', '1', '2014-08-08 17:13:39');
INSERT INTO `weibowordcount` VALUES ('3741344629759348', '10', '1', '2014-08-08 17:18:43');
INSERT INTO `weibowordcount` VALUES ('3741346440955165', '3', '1', '2014-08-08 17:26:19');
INSERT INTO `weibowordcount` VALUES ('3741346517464752', '3', '1', '2014-08-08 17:26:58');
INSERT INTO `weibowordcount` VALUES ('3741346727014058', '3', '1', '2014-08-08 17:27:58');
INSERT INTO `weibowordcount` VALUES ('3741347300873268', '16', '1', '2014-08-08 17:29:58');
INSERT INTO `weibowordcount` VALUES ('3741348912126243', '3', '1', '2014-08-08 17:36:33');
INSERT INTO `weibowordcount` VALUES ('3741415609975982', '3', '1', '2014-08-08 22:01:37');
INSERT INTO `weibowordcount` VALUES ('3741415794044972', '2', '3', '2014-08-08 22:02:02');
INSERT INTO `weibowordcount` VALUES ('3741415794044972', '9', '1', '2014-08-08 22:02:02');
INSERT INTO `weibowordcount` VALUES ('3741415794044972', '10', '1', '2014-08-08 22:02:02');
INSERT INTO `weibowordcount` VALUES ('3742358019382765', '3', '1', '2014-08-11 12:26:50');
INSERT INTO `weibowordcount` VALUES ('3742358648970908', '3', '1', '2014-08-11 12:29:10');
INSERT INTO `weibowordcount` VALUES ('3742359324318614', '3', '1', '2014-08-11 12:31:30');
INSERT INTO `weibowordcount` VALUES ('3742360473355874', '3', '1', '2014-08-11 12:36:00');
INSERT INTO `weibowordcount` VALUES ('3742360678321096', '3', '1', '2014-08-11 12:36:27');
INSERT INTO `weibowordcount` VALUES ('3742360808365272', '3', '2', '2014-08-11 12:37:02');
INSERT INTO `weibowordcount` VALUES ('3742361446529005', '3', '2', '2014-08-11 12:39:27');
INSERT INTO `weibowordcount` VALUES ('3742362943744671', '3', '1', '2014-08-11 12:45:50');
INSERT INTO `weibowordcount` VALUES ('3742363996049273', '3', '2', '2014-08-11 12:50:22');
INSERT INTO `weibowordcount` VALUES ('3742672994794961', '3', '1', '2014-08-12 09:18:14');
INSERT INTO `weibowordcount` VALUES ('3742676819699361', '3', '2', '2014-08-12 09:33:06');
INSERT INTO `weibowordcount` VALUES ('3742679315610892', '10', '1', '2014-08-12 09:43:09');
INSERT INTO `weibowordcount` VALUES ('3742679974226167', '3', '2', '2014-08-12 09:45:47');
INSERT INTO `weibowordcount` VALUES ('3742680066980569', '3', '1', '2014-08-12 09:46:14');
INSERT INTO `weibowordcount` VALUES ('3742680791991016', '10', '1', '2014-08-12 09:49:25');
INSERT INTO `weibowordcount` VALUES ('3742681056276200', '3', '1', '2014-08-12 09:50:20');
INSERT INTO `weibowordcount` VALUES ('3742686110962771', '10', '1', '2014-08-12 10:10:17');
INSERT INTO `weibowordcount` VALUES ('3742687066656048', '3', '1', '2014-08-12 10:13:39');
INSERT INTO `weibowordcount` VALUES ('3742689113564411', '3', '1', '2014-08-12 10:21:57');
INSERT INTO `weibowordcount` VALUES ('3742689332066518', '3', '1', '2014-08-12 10:22:52');
INSERT INTO `weibowordcount` VALUES ('3742693895679179', '16', '2', '2014-08-12 10:41:10');
INSERT INTO `weibowordcount` VALUES ('3742694159362805', '16', '1', '2014-08-12 10:42:30');
INSERT INTO `weibowordcount` VALUES ('3742694863863252', '12', '1', '2014-08-12 10:45:12');
INSERT INTO `weibowordcount` VALUES ('3742698949668993', '16', '1', '2014-08-12 11:01:57');
INSERT INTO `weibowordcount` VALUES ('3742701235543965', '3', '1', '2014-08-12 11:10:31');
INSERT INTO `weibowordcount` VALUES ('3742701369211462', '3', '1', '2014-08-12 11:10:57');
INSERT INTO `weibowordcount` VALUES ('3742701738154163', '12', '3', '2014-08-12 11:12:24');
INSERT INTO `weibowordcount` VALUES ('3742702459900320', '3', '1', '2014-08-12 11:15:31');
INSERT INTO `weibowordcount` VALUES ('3742703126435931', '3', '1', '2014-08-12 11:17:43');
INSERT INTO `weibowordcount` VALUES ('3742703629834900', '3', '1', '2014-08-12 11:19:55');
INSERT INTO `weibowordcount` VALUES ('3742705945400861', '3', '1', '2014-08-12 11:28:56');
INSERT INTO `weibowordcount` VALUES ('3742705940767047', '3', '1', '2014-08-12 11:28:56');
INSERT INTO `weibowordcount` VALUES ('3742706402395264', '3', '1', '2014-08-12 11:30:21');
INSERT INTO `weibowordcount` VALUES ('3742706717018784', '10', '1', '2014-08-12 11:32:01');
INSERT INTO `weibowordcount` VALUES ('3742707157491384', '3', '1', '2014-08-12 11:33:54');
INSERT INTO `weibowordcount` VALUES ('3742707157490868', '3', '1', '2014-08-12 11:33:54');
INSERT INTO `weibowordcount` VALUES ('3742707522538523', '3', '1', '2014-08-12 11:35:13');
INSERT INTO `weibowordcount` VALUES ('3742707954113605', '3', '1', '2014-08-12 11:36:33');
INSERT INTO `weibowordcount` VALUES ('3742708251958468', '3', '2', '2014-08-12 11:38:19');
INSERT INTO `weibowordcount` VALUES ('3742708507853640', '3', '1', '2014-08-12 11:39:08');
INSERT INTO `weibowordcount` VALUES ('3742709284273268', '3', '1', '2014-08-12 11:42:05');
INSERT INTO `weibowordcount` VALUES ('3742709536137107', '3', '1', '2014-08-12 11:42:57');
INSERT INTO `weibowordcount` VALUES ('3742709753512185', '16', '1', '2014-08-12 11:44:26');
INSERT INTO `weibowordcount` VALUES ('3742710190472437', '3', '1', '2014-08-12 11:46:09');
INSERT INTO `weibowordcount` VALUES ('3742710479391799', '3', '1', '2014-08-12 11:47:00');
INSERT INTO `weibowordcount` VALUES ('3742739113756063', '3', '1', '2014-08-12 13:40:59');
INSERT INTO `weibowordcount` VALUES ('3742739985910026', '3', '1', '2014-08-12 13:43:42');
INSERT INTO `weibowordcount` VALUES ('3742742423032800', '3', '1', '2014-08-12 13:53:57');
INSERT INTO `weibowordcount` VALUES ('3743107391027508', '3', '4', '2014-08-13 14:03:59');
