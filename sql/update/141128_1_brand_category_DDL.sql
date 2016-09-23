/*
Navicat MySQL Data Transfer

Source Server         : local
Source Server Version : 50534
Source Host           : localhost:3306
Source Database       : fsn_core

Target Server Type    : MYSQL
Target Server Version : 50534
File Encoding         : 65001

Date: 2014-11-27 09:52:15
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `brand_category`
-- ----------------------------
DROP TABLE IF EXISTS `brand_category`;
CREATE TABLE `brand_category` (
  `ID` int(10) NOT NULL AUTO_INCREMENT,
  `CATEGORY_NAME` text NOT NULL,
  `CORD` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Records of brand_category
-- ----------------------------
INSERT INTO `brand_category`(CATEGORY_NAME,cord) VALUES ('医药', '05');
INSERT INTO `brand_category`(CATEGORY_NAME,cord) VALUES ('医药.药品，消毒剂，中药药材，药酒', '0501');
INSERT INTO `brand_category`(CATEGORY_NAME,cord) VALUES ('医药.医用营养品，人用膳食补充剂，婴儿食品', '0502');
INSERT INTO `brand_category`(CATEGORY_NAME,cord) VALUES ('食品', '29');
INSERT INTO `brand_category`(CATEGORY_NAME,cord) VALUES ('食品.肉，非活的家禽，野味，肉汁', '2901');
INSERT INTO `brand_category`(CATEGORY_NAME,cord) VALUES ('食品.非活水产品', '2902');
INSERT INTO `brand_category`(CATEGORY_NAME,cord) VALUES ('食品.罐头食品（软包装食品不包括在内，随原料制成品归类）', '2903');
INSERT INTO `brand_category`(CATEGORY_NAME,cord) VALUES ('食品.腌渍、干制水果及制品', '2904');
INSERT INTO `brand_category`(CATEGORY_NAME,cord) VALUES ('食品.腌制、干制蔬菜', '2905');
INSERT INTO `brand_category`(CATEGORY_NAME,cord) VALUES ('食品.蛋品 ', '2906');
INSERT INTO `brand_category`(CATEGORY_NAME,cord) VALUES ('食品.奶及乳制品', '2907');
INSERT INTO `brand_category`(CATEGORY_NAME,cord) VALUES ('食品.食用油脂', '2908');
INSERT INTO `brand_category`(CATEGORY_NAME,cord) VALUES ('食品.色拉 ', '2909');
INSERT INTO `brand_category`(CATEGORY_NAME,cord) VALUES ('食品.食用果胶', '2910');
INSERT INTO `brand_category`(CATEGORY_NAME,cord) VALUES ('食品.加工过的坚果', '2911');
INSERT INTO `brand_category`(CATEGORY_NAME,cord) VALUES ('食品.菌类干制品', '2912');
INSERT INTO `brand_category`(CATEGORY_NAME,cord) VALUES ('食品.食物蛋白，豆腐制品 ', '2913');
INSERT INTO `brand_category`(CATEGORY_NAME,cord) VALUES ('方便食品', '30');
INSERT INTO `brand_category`(CATEGORY_NAME,cord) VALUES ('方便食品.咖啡，咖啡代用品，可可', '3001');
INSERT INTO `brand_category`(CATEGORY_NAME,cord) VALUES ('方便食品.茶及茶叶代用品', '3002');
INSERT INTO `brand_category`(CATEGORY_NAME,cord) VALUES ('方便食品.糖', '3003');
INSERT INTO `brand_category`(CATEGORY_NAME,cord) VALUES ('方便食品.糖果，南糖，糖', '3004');
INSERT INTO `brand_category`(CATEGORY_NAME,cord) VALUES ('方便食品.蜂蜜，糖浆', '3005');
INSERT INTO `brand_category`(CATEGORY_NAME,cord) VALUES ('方便食品.面包，糕点', '3006');
INSERT INTO `brand_category`(CATEGORY_NAME,cord) VALUES ('方便食品.方便食品 ', '3007');
INSERT INTO `brand_category`(CATEGORY_NAME,cord) VALUES ('方便食品.米，面粉（包括五谷杂粮）', '3008');
INSERT INTO `brand_category`(CATEGORY_NAME,cord) VALUES ('方便食品.面条及米面制品', '3009');
INSERT INTO `brand_category`(CATEGORY_NAME,cord) VALUES ('方便食品.谷物膨化食品', '3010');
INSERT INTO `brand_category`(CATEGORY_NAME,cord) VALUES ('方便食品.豆粉，食用面筋', '3011');
INSERT INTO `brand_category`(CATEGORY_NAME,cord) VALUES ('方便食品.食用淀粉及其制品', '3012');
INSERT INTO `brand_category`(CATEGORY_NAME,cord) VALUES ('方便食品.食用冰，冰制品', '3013');
INSERT INTO `brand_category`(CATEGORY_NAME,cord) VALUES ('方便食品.食盐 ', '3014');
INSERT INTO `brand_category`(CATEGORY_NAME,cord) VALUES ('方便食品.酱油，醋', '3015');
INSERT INTO `brand_category`(CATEGORY_NAME,cord) VALUES ('方便食品.芥末，味精，沙司，酱等调味品', '3016');
INSERT INTO `brand_category`(CATEGORY_NAME,cord) VALUES ('方便食品.酵母', '3017');
INSERT INTO `brand_category`(CATEGORY_NAME,cord) VALUES ('方便食品.食用香精，香料', '3018');
INSERT INTO `brand_category`(CATEGORY_NAME,cord) VALUES ('方便食品.单一商品 ', '3019');
INSERT INTO `brand_category`(CATEGORY_NAME,cord) VALUES ('啤酒饮料', '32');
INSERT INTO `brand_category`(CATEGORY_NAME,cord) VALUES ('啤酒饮料.啤酒', '3201');
INSERT INTO `brand_category`(CATEGORY_NAME,cord) VALUES ('啤酒饮料.不含酒精饮料 ', '3202');
INSERT INTO `brand_category`(CATEGORY_NAME,cord) VALUES ('啤酒饮料.糖浆及其他供饮料用的制剂 ', '3203');
INSERT INTO `brand_category`(CATEGORY_NAME,cord) VALUES ('酒', '33');
INSERT INTO `brand_category`(CATEGORY_NAME,cord) VALUES ('酒.含酒精的饮料（啤酒除外）', '3301');
