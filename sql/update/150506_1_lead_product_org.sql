/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50534
Source Host           : localhost:3306
Source Database       : fsn_new

Target Server Type    : MYSQL
Target Server Version : 50534
File Encoding         : 65001

Date: 2015-05-06 14:03:19
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `lead_product_org`
-- ----------------------------
DROP TABLE IF EXISTS `lead_product_org`;
CREATE TABLE `lead_product_org` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `orgName` varchar(255) DEFAULT NULL COMMENT '组织机构名称',
  `organization` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of lead_product_org
-- ----------------------------
INSERT INTO `lead_product_org` VALUES ('1', '贵州省分析测试研究院 \r\n贵州省分析测试研究院 \r\n贵州省分析测试研究院 ', '1');
INSERT INTO `lead_product_org` VALUES ('2', '亿易通 ', '24');
INSERT INTO `lead_product_org` VALUES ('3', '琼玲科技有限公司', '27');
INSERT INTO `lead_product_org` VALUES ('4', '新技术所 ', '52');
INSERT INTO `lead_product_org` VALUES ('5', '食安公司  ', '54');
INSERT INTO `lead_product_org` VALUES ('6', '新疆分析测试研究院 ', '90');
INSERT INTO `lead_product_org` VALUES ('7', '永辉超市', '91');
INSERT INTO `lead_product_org` VALUES ('8', '山西省分析科学研究院 ', '92');
INSERT INTO `lead_product_org` VALUES ('9', '中国广州分析测试中心 ', '100');
INSERT INTO `lead_product_org` VALUES ('10', '江西测试院  ', '107');
INSERT INTO `lead_product_org` VALUES ('11', '大润发超市', '116');
