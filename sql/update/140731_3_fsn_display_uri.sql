/*
Navicat MySQL Data Transfer

Source Server         : GETTEC
Source Server Version : 50534
Source Host           : 127.0.0.1:3306
Source Database       : fsn_core

Target Server Type    : MYSQL
Target Server Version : 50534
File Encoding         : 65001

Date: 2014-07-31 15:32:15
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `fsn_display_uri`
-- ----------------------------
DROP TABLE IF EXISTS `fsn_display_uri`;
CREATE TABLE `fsn_display_uri` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `type` int(1) NOT NULL DEFAULT '0' COMMENT '资源类型 1-产品 2-广告 3-栏目 4-推荐 5-新闻',
  `content` text COLLATE utf8_unicode_ci COMMENT '资源内容',
  `name` varchar(50) COLLATE utf8_unicode_ci NOT NULL DEFAULT 'test' COMMENT '资源名称',
  `pid` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=185 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='显示资源存储表';

-- ----------------------------
-- Records of fsn_display_uri
-- ----------------------------
INSERT INTO `fsn_display_uri` VALUES ('161', '4', '<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><re><rePic>http://211.151.134.74:8080/portal/img/recommend/scroll/1405660829643.jpg</rePic><reUrl>12143</reUrl><active>0</active><container>0</container><name>fdsa</name><uriId>161</uriId></re>', 'fdsa', null);
INSERT INTO `fsn_display_uri` VALUES ('159', '4', '<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><re><rePic>http://211.151.134.74:8080/portal/img/recommend/scroll/1405660561651.jpg</rePic><reUrl>239</reUrl><active>0</active><container>0</container><name>特仑苏有机奶</name><uriId>159</uriId></re>', '特仑苏有机奶', null);
INSERT INTO `fsn_display_uri` VALUES ('17', '4', '<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><re><rePic>http://211.151.134.74:8080/portal/img/recommend/scroll/1405863278946.jpg</rePic><reUrl>11480</reUrl><active>0</active><container>0</container><name>re2</name><uriId>17</uriId></re>', 're2', null);
INSERT INTO `fsn_display_uri` VALUES ('18', '4', '<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><re><rePic>http://211.151.134.74:8080/portal/img/recommend/scroll/1405660720479.jpg</rePic><reUrl>12809</reUrl><active>0</active><container>0</container><name>re24</name><uriId>18</uriId></re>', 're24', null);
INSERT INTO `fsn_display_uri` VALUES ('19', '4', '<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><re><rePic>http://211.151.134.74:8080/portal/img/recommend/scroll/1405660917657.jpg</rePic><reUrl>13677</reUrl><active>0</active><container>0</container><name>re3</name><uriId>19</uriId></re>', 're3', null);
INSERT INTO `fsn_display_uri` VALUES ('20', '4', '<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><re><rePic>http://211.151.134.74:8080/portal/img/recommend/scroll/1405912586963.jpg</rePic><reUrl>11642</reUrl><active>0</active><container>0</container><name>绿茶</name><uriId>20</uriId></re>', '绿茶', null);
INSERT INTO `fsn_display_uri` VALUES ('23', '4', '<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><re><rePic>http://qa.fsnrec.com/portal/img/recommendAd/lgd2.jpg</rePic><reUrl>\r\n</reUrl><active>1</active><container>6</container></re>', 're7', null);
INSERT INTO `fsn_display_uri` VALUES ('24', '4', '<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><re><rePic>http://qa.fsnrec.com/portal/img/recommendAd/visual2.jpg</rePic><reUrl>\r\n</reUrl><active>1</active><container>7</container></re>', 're8', null);
INSERT INTO `fsn_display_uri` VALUES ('26', '4', '<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><re><rePic>http://qa.fsnrec.com/portal/img/recommendAd/lxc2.png</rePic><reUrl></reUrl><active>1</active><container>9</container></re>', 're10', null);
INSERT INTO `fsn_display_uri` VALUES ('184', '4', '<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><re><rePic>http://211.151.134.74:8080/portal/img/recommend/scroll/1405925210138.jpg</rePic><reUrl>11710</reUrl><active>0</active><container>0</container><name>好迪</name></re>', '好迪', null);
