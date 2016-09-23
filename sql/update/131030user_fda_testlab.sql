/*
Navicat MySQL Data Transfer

Source Server         : gettec
Source Server Version : 50530
Source Host           : localhost:3306
Source Database       : fsn_cloud

Target Server Type    : MYSQL
Target Server Version : 50530
File Encoding         : 65001

Date: 2013-10-30 17:58:36
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `user`
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(200) NOT NULL,
  `display_name` varchar(200) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=33 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES ('1', '贵州省食品与药物管理局.贵阳食品与药物管理局', '贵阳食品与药物管理局');
INSERT INTO `user` VALUES ('5', '贵州省分析测试研究院.贵阳市分析测试研究院', '贵阳市分析测试研究院');
INSERT INTO `user` VALUES ('27', '贵州省食品与药物管理局.遵义食品与药物管理局', '遵义食品与药物管理局');
INSERT INTO `user` VALUES ('28', '贵州省食品与药物管理局.安顺食品与药物管理局', '安顺食品与药物管理局');
INSERT INTO `user` VALUES ('30', '贵州省分析测试研究院.遵义市分析测试研究院', '遵义市分析测试研究院');
INSERT INTO `user` VALUES ('32', '贵州省分析测试研究院.安顺市分析测试研究院', '安顺市分析测试研究院');
COMMIT;

-- ----------------------------
-- Table structure for `fda`
-- ----------------------------
DROP TABLE IF EXISTS `fda`;
CREATE TABLE `fda` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `address` varchar(200) DEFAULT NULL,
  `code` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_fda_code` (`code`)
) ENGINE=InnoDB AUTO_INCREMENT=29 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of fda
-- ----------------------------
INSERT INTO `fda` VALUES ('1', '贵阳市', 'GZFDA');
INSERT INTO `fda` VALUES ('27', '遵义市', 'ZYFDA');
INSERT INTO `fda` VALUES ('28', '安顺市', 'ASFDA');
COMMIT;


-- ----------------------------
-- Table structure for `test_lab`
-- ----------------------------
DROP TABLE IF EXISTS `test_lab`;
CREATE TABLE `test_lab` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `address` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=33 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of test_lab
-- ----------------------------
INSERT INTO `test_lab` VALUES ('5', '贵阳');
INSERT INTO `test_lab` VALUES ('30', '遵义市');
INSERT INTO `test_lab` VALUES ('32', '安顺市');
COMMIT;