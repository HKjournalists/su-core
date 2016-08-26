/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50534
Source Host           : localhost:3306
Source Database       : fsn_cloud_qa

Target Server Type    : MYSQL
Target Server Version : 50534
File Encoding         : 65001

Date: 2014-06-27 14:58:09
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `enterprise_registe`
-- ----------------------------
DROP TABLE IF EXISTS `enterprise_registe`;
CREATE TABLE `enterprise_registe` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `userName` varchar(200) DEFAULT NULL COMMENT '用户名',
  `password` varchar(200) DEFAULT NULL COMMENT '密码',
  `email` varchar(200) DEFAULT NULL COMMENT '邮箱',
  `enterpriteName` varchar(200) DEFAULT NULL COMMENT '企业名称',
  `enterptiteAddress` varchar(200) DEFAULT NULL COMMENT '企业地址',
  `enterpriteType` varchar(200) DEFAULT NULL COMMENT '企业类型（生产企业，流通环节企业，餐饮企业）',
  `legalPerson` varchar(200) DEFAULT NULL COMMENT '法人代表',
  `status` varchar(20) DEFAULT '待审核' COMMENT '审核状态（待审核/审核通过/审核失败）',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of enterprise_registe
-- ----------------------------
INSERT INTO `enterprise_registe` VALUES ('5', 'fsdfdfsd', 'fsdfdfsd', 'fsdfdfsd@ww.ff', 'fsdfdfsd', 'fsdfdfsd', '生产企业', 'fsdfdfsd', '待审核');
INSERT INTO `enterprise_registe` VALUES ('6', 'fsdfdfsd', 'fsdfdfsd', 'fsdfdfsd@ww.ff', 'fsdfdfsd', 'fsdfdfsd', '生产企业', 'fsdfdfsd', '审核通过');
INSERT INTO `enterprise_registe` VALUES ('7', 'fdgdrgdfgd', 'fdgdrgdfgd', 'fdgdrgdfgd', 'fdgdrgdfgd', 'fdgdrgdfgd', '生产企业', 'fdgdrgdfgd', '审核通过');
INSERT INTO `enterprise_registe` VALUES ('8', 'jazhen12', 'jazhen1', 'jj@qq.cnm', 'fdsfsd', 'ggdhdh', '生产企业', 'hfhf', '待审核');
INSERT INTO `enterprise_registe` VALUES ('9', 'jazhen12tyyt', 'jazhen1', 'jj@qq.cnm', 'fdsfsd', 'ggdhdh', '生产企业', 'hfhf', '待审核');
INSERT INTO `enterprise_registe` VALUES ('10', 'jazhen12tyythth', 'jazhen1', 'jj@qq.cnm', 'fdsfsd', 'ggdhdh', '生产企业', 'hfhf', '待审核');
INSERT INTO `enterprise_registe` VALUES ('11', 'jazhen12tyyththgdgf', 'jazhen1', 'jj@qq.cnm', 'fdsfsd', 'ggdhdh', '生产企业', 'hfhf', '待审核');
INSERT INTO `enterprise_registe` VALUES ('12', 'longxianzhen', 'longxianzhen', 'fdshjfh@jgj.com', '小龙', '龙哥', '生产企业', '大龙', '待审核');
INSERT INTO `enterprise_registe` VALUES ('13', 'fdjhjhj', 'fdjhjhj', 'uty', 'fdjhjhj', 'fdjhjhj', '生产企业', 'fdjhjhj', '待审核');
INSERT INTO `enterprise_registe` VALUES ('14', 'fdgfdhgfdhh', 'fdgfdhgfdhh', 'fege@qq.dd', 'fdgfdhgfdhh', 'fdgfdhgfdhh', '生产企业', 'fdgfdhgfdhh', '待审核');
INSERT INTO `enterprise_registe` VALUES ('15', 'dfsgdfgdg', 'dfsgdfgdg', 'dfsgdfgdg@qq.com', 'dfsgdfgdg', 'dfsgdfgdg', '流通环节企业', 'dfsgdfgdg', '待审核');
INSERT INTO `enterprise_registe` VALUES ('16', 'bfdhdfhdh', 'bfdhdfhdh', 'bfdhdfhdh@qq.com', 'bfdhdfhdh', 'bfdhdfhdh', '生产企业', 'bfdhdfhdh', '待审核');
INSERT INTO `enterprise_registe` VALUES ('17', 'longzhen', 'jazhen', 'xianzhen.long@gettec.net', '贵州技泰', '贵州贵阳', '生产企业', '小龙', null);
INSERT INTO `enterprise_registe` VALUES ('18', 'xianzhen', 'jazhen', 'xianzhen.long@gettec.net', '食安公司', '贵州贵阳', '生产企业', '梅西', null);
INSERT INTO `enterprise_registe` VALUES ('19', 'xiaolong', 'jazhen', 'xianzhen.long@gettec.net', '食品安全', '贵阳', '生产企业', 'C罗', '审核通过');
INSERT INTO `enterprise_registe` VALUES ('20', 'xianja', 'jazhen', 'xianja@qq.com', '豆沙包', '豆沙包fdsf', '生产企业', '豆沙包fre', '审核通过');
