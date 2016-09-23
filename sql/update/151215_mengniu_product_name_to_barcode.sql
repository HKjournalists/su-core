/*
Navicat MySQL Data Transfer

Source Server         : 本机数据库
Source Server Version : 50534
Source Host           : localhost:3306
Source Database       : fsn

Target Server Type    : MYSQL
Target Server Version : 50534
File Encoding         : 65001

Date: 2015-12-14 10:32:23
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `mengniu_product_name_to_barcode`
-- ----------------------------
DROP TABLE IF EXISTS `mengniu_product_name_to_barcode`;
CREATE TABLE `mengniu_product_name_to_barcode` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `product_name` varchar(255) NOT NULL,
  `barcode` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `product_name` (`product_name`,`barcode`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of mengniu_product_name_to_barcode
-- ----------------------------
INSERT INTO `mengniu_product_name_to_barcode` VALUES ('3', '优益C活菌型乳酸菌饮品原味塑料瓶340ml×24瓶', '6934665087653');
INSERT INTO `mengniu_product_name_to_barcode` VALUES ('20', '特仑苏有机牛奶苗条装250ml×12盒', '6923644270940');
INSERT INTO `mengniu_product_name_to_barcode` VALUES ('19', '特仑苏纯牛奶苗条装250ml×12盒', '6923644266318');
INSERT INTO `mengniu_product_name_to_barcode` VALUES ('12', '真果粒椰果粒250g×12盒', '6923644268923');
INSERT INTO `mengniu_product_name_to_barcode` VALUES ('16', '真果粒芦荟粒250g×12盒', '6923644268930');
INSERT INTO `mengniu_product_name_to_barcode` VALUES ('18', '真果粒草莓果粒250g×12盒', '6923644268916');
INSERT INTO `mengniu_product_name_to_barcode` VALUES ('17', '真果粒黄桃果粒250g×12盒', '6923644268909');
INSERT INTO `mengniu_product_name_to_barcode` VALUES ('10', '蒙牛低脂高钙牛奶利乐包1000ml×6盒', '6923644264154');
INSERT INTO `mengniu_product_name_to_barcode` VALUES ('11', '蒙牛全脂牛奶利乐包1000ml×12盒', '6923644269623');
INSERT INTO `mengniu_product_name_to_barcode` VALUES ('4', '蒙牛双株酸牛奶原味塑杯100g×8杯', '6934665080982');
INSERT INTO `mengniu_product_name_to_barcode` VALUES ('6', '蒙牛早餐奶核桃味利乐枕230ml×16包', '6923644278922');
INSERT INTO `mengniu_product_name_to_barcode` VALUES ('2', '蒙牛红谷谷粒早餐苗条装250ml×12盒', '6923644277048');
INSERT INTO `mengniu_product_name_to_barcode` VALUES ('7', '蒙牛纯牛奶利乐包1000ml×6盒', '6923644212346');
INSERT INTO `mengniu_product_name_to_barcode` VALUES ('5', '蒙牛酸牛奶红枣百利包150g×15袋', '6934665085390');
INSERT INTO `mengniu_product_name_to_barcode` VALUES ('1', '蒙牛高钙牛奶利乐包1000ml×6盒', '6923644251130');
INSERT INTO `mengniu_product_name_to_barcode` VALUES ('8', '蒙牛高钙牛奶利乐包250ml×24盒', '6923644251147');
INSERT INTO `mengniu_product_name_to_barcode` VALUES ('9', '蒙牛黑谷谷粒早餐苗条装250ml×12盒', '6923644276911');
INSERT INTO `mengniu_product_name_to_barcode` VALUES ('15', '酸酸乳果蔬系列紫薯菠萝味苗条装250ml×12盒', '6923644273019');
INSERT INTO `mengniu_product_name_to_barcode` VALUES ('14', '酸酸乳营养乳味饮品原味利乐包250ml×24包', '6923644242961');
INSERT INTO `mengniu_product_name_to_barcode` VALUES ('13', '酸酸乳营养乳味饮品草莓味利乐包250ml×24包', '6923644242978');
