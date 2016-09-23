/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50534
Source Host           : localhost:3306
Source Database       : fsn_pro_20150527

Target Server Type    : MYSQL
Target Server Version : 50534
File Encoding         : 65001

Date: 2015-05-28 16:55:39
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `sanitary_cert_pdf_to_resource`
-- ----------------------------
DROP TABLE IF EXISTS `sanitary_cert_pdf_to_resource`;
CREATE TABLE `sanitary_cert_pdf_to_resource` (
  `RESOURCE_ID` bigint(20) NOT NULL COMMENT '资源id',
  `test_result_imp_pro_id` bigint(20) NOT NULL COMMENT '进口食品检测结果表ID',
  `createDate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`RESOURCE_ID`,`test_result_imp_pro_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sanitary_cert_pdf_to_resource
-- ----------------------------
