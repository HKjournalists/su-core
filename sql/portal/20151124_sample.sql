/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50530
Source Host           : localhost:3306
Source Database       : fsn_cloud_qa

Target Server Type    : MYSQL
Target Server Version : 50530
File Encoding         : 65001

Date: 2015-11-24 10:05:20
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for sample_business_brand
-- ----------------------------
DROP TABLE IF EXISTS `sample_business_brand`;
CREATE TABLE `sample_business_brand` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `last_modify_time` datetime DEFAULT NULL,
  `name` varchar(200) DEFAULT NULL,
  `organization` bigint(20) DEFAULT NULL,
  `registration_date` varchar(20) DEFAULT NULL,
  `brand_category_id` bigint(20) DEFAULT NULL,
  `business_unit_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKE8464D7D5186610C` (`business_unit_id`),
  CONSTRAINT `FKE8464D7D5186610C` FOREIGN KEY (`business_unit_id`) REFERENCES `business_unit` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for sample_product
-- ----------------------------
DROP TABLE IF EXISTS `sample_product`;
CREATE TABLE `sample_product` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `ICB_category` varchar(255) DEFAULT NULL,
  `barcode` varchar(50) DEFAULT NULL,
  `characteristic` varchar(255) DEFAULT NULL,
  `cstm` varchar(255) DEFAULT NULL,
  `des` varchar(255) DEFAULT NULL,
  `expiration` varchar(255) DEFAULT NULL,
  `expiration_date` varchar(255) DEFAULT NULL,
  `feature` varchar(255) DEFAULT NULL,
  `format` varchar(200) DEFAULT NULL,
  `imgurl` varchar(255) DEFAULT NULL,
  `ingredient` varchar(255) DEFAULT NULL,
  `last_modify_time` datetime DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `net_content` varchar(200) DEFAULT NULL,
  `note` varchar(255) DEFAULT NULL,
  `nutri_label` varchar(255) DEFAULT NULL,
  `nutri_status` char(1) DEFAULT NULL,
  `organization` bigint(20) DEFAULT NULL,
  `other_Name` varchar(255) DEFAULT NULL,
  `package_flag` char(1) DEFAULT NULL,
  `format_pdf` varchar(200) DEFAULT NULL,
  `product_type` int(11) DEFAULT NULL,
  `qscore_censor` float DEFAULT NULL,
  `qscore_sample` float DEFAULT NULL,
  `qscore_self` float DEFAULT NULL,
  `risk_failure` varchar(255) DEFAULT NULL,
  `riskIndex` double DEFAULT NULL,
  `riskIndex_Date` datetime DEFAULT NULL,
  `risk_succeed` bit(1) DEFAULT NULL,
  `category` varchar(50) DEFAULT NULL,
  `status` varchar(50) DEFAULT NULL,
  `test_property_name` varchar(255) DEFAULT NULL,
  `category_id` bigint(20) DEFAULT NULL,
  `producer_id` bigint(20) DEFAULT NULL,
  `sample_business_brand_id` bigint(20) NOT NULL,
  `UNIT_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK49119F5AD65DE4A9` (`sample_business_brand_id`),
  KEY `FK49119F5A7D9C975B` (`category_id`),
  KEY `FK49119F5AD857A06F` (`UNIT_ID`),
  KEY `FK49119F5A629361BD` (`producer_id`),
  CONSTRAINT `FK49119F5A629361BD` FOREIGN KEY (`producer_id`) REFERENCES `business_unit` (`id`),
  CONSTRAINT `FK49119F5A7D9C975B` FOREIGN KEY (`category_id`) REFERENCES `product_category_info` (`id`),
  CONSTRAINT `FK49119F5AD65DE4A9` FOREIGN KEY (`sample_business_brand_id`) REFERENCES `sample_business_brand` (`id`),
  CONSTRAINT `FK49119F5AD857A06F` FOREIGN KEY (`UNIT_ID`) REFERENCES `t_meta_unit` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=160 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for sample_product_instance
-- ----------------------------
DROP TABLE IF EXISTS `sample_product_instance`;
CREATE TABLE `sample_product_instance` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `batch_serial_no` varchar(50) DEFAULT NULL,
  `expiration_date` datetime DEFAULT NULL,
  `production_date` datetime DEFAULT NULL,
  `qs_no` varchar(255) DEFAULT NULL,
  `serial` varchar(50) DEFAULT NULL,
  `original_id` bigint(20) DEFAULT NULL,
  `producer_id` bigint(20) DEFAULT NULL,
  `sample_product_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKFF9ECBDA4F060AE7` (`original_id`),
  KEY `FKFF9ECBDA4D171D09` (`sample_product_id`),
  KEY `FKFF9ECBDA629361BD` (`producer_id`),
  CONSTRAINT `FKFF9ECBDA4D171D09` FOREIGN KEY (`sample_product_id`) REFERENCES `sample_product` (`id`),
  CONSTRAINT `FKFF9ECBDA4F060AE7` FOREIGN KEY (`original_id`) REFERENCES `sample_product_instance` (`id`),
  CONSTRAINT `FKFF9ECBDA629361BD` FOREIGN KEY (`producer_id`) REFERENCES `business_unit` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=404 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for sample_test_property
-- ----------------------------
DROP TABLE IF EXISTS `sample_test_property`;
CREATE TABLE `sample_test_property` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `assessment` varchar(50) DEFAULT NULL,
  `name` varchar(50) DEFAULT NULL,
  `result` varchar(20) DEFAULT NULL,
  `standard` varchar(50) DEFAULT NULL,
  `tech_indicator` varchar(50) DEFAULT NULL,
  `test_result_id` bigint(20) DEFAULT NULL,
  `unit` varchar(20) DEFAULT NULL,
  `category` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKE0C4E50D1B2CA02` (`category`),
  CONSTRAINT `FKE0C4E50D1B2CA02` FOREIGN KEY (`category`) REFERENCES `test_property_category` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for sample_test_result
-- ----------------------------
DROP TABLE IF EXISTS `sample_test_result`;
CREATE TABLE `sample_test_result` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `approve_by` varchar(255) DEFAULT NULL,
  `audit_by` varchar(255) DEFAULT NULL,
  `auto_report_flag` bit(1) DEFAULT NULL,
  `back_count` int(11) DEFAULT NULL,
  `backLimsURL` varchar(255) DEFAULT NULL,
  `back_result` varchar(255) DEFAULT NULL,
  `back_time` datetime DEFAULT NULL,
  `check_org_name` varchar(255) DEFAULT NULL,
  `comment` varchar(255) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `create_user` varchar(255) DEFAULT NULL,
  `dbflag` varchar(20) DEFAULT NULL,
  `del` int(11) DEFAULT NULL,
  `edition` varchar(255) DEFAULT NULL,
  `end_date` datetime DEFAULT NULL,
  `equipment` varchar(255) DEFAULT NULL,
  `fullPdfPath` varchar(255) DEFAULT NULL,
  `interceptionPdfPath` varchar(255) DEFAULT NULL,
  `key_tester` varchar(255) DEFAULT NULL,
  `last_modify_time` datetime DEFAULT NULL,
  `last_modify_user` varchar(255) DEFAULT NULL,
  `limsIdentification` varchar(255) DEFAULT NULL,
  `limsSampleId` bigint(20) DEFAULT NULL,
  `organization` bigint(20) DEFAULT NULL,
  `organization_name` varchar(255) DEFAULT NULL,
  `pass` bit(1) DEFAULT NULL,
  `pub_user_name` varchar(255) DEFAULT NULL,
  `publishDate` datetime DEFAULT NULL,
  `publish_flag` char(1) DEFAULT NULL,
  `receiveDate` datetime DEFAULT NULL,
  `result` varchar(255) DEFAULT NULL,
  `sample_no` varchar(255) DEFAULT NULL,
  `sample_quantity` varchar(255) DEFAULT NULL,
  `sampling_date` datetime DEFAULT NULL,
  `sampling_location` varchar(255) DEFAULT NULL,
  `service_order` varchar(255) DEFAULT NULL,
  `SIGN_FLAG` bit(1) DEFAULT NULL,
  `standard` varchar(255) DEFAULT NULL,
  `suppliers_back_count` int(11) DEFAULT NULL,
  `test_date` datetime DEFAULT NULL,
  `test_orgnization` varchar(255) DEFAULT NULL,
  `test_place` varchar(255) DEFAULT NULL,
  `test_type` varchar(255) DEFAULT NULL,
  `tips` varchar(255) DEFAULT NULL,
  `upload_path` varchar(255) DEFAULT NULL,
  `sample_brand_id` bigint(20) DEFAULT NULL,
  `sample_id` bigint(20) DEFAULT NULL,
  `testee_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK664D3D55DC9C2D54` (`sample_brand_id`),
  KEY `FK664D3D557638289D` (`testee_id`),
  KEY `FK664D3D552A27E46E` (`sample_id`),
  CONSTRAINT `FK664D3D552A27E46E` FOREIGN KEY (`sample_id`) REFERENCES `sample_product_instance` (`id`),
  CONSTRAINT `FK664D3D557638289D` FOREIGN KEY (`testee_id`) REFERENCES `business_unit` (`id`),
  CONSTRAINT `FK664D3D55DC9C2D54` FOREIGN KEY (`sample_brand_id`) REFERENCES `sample_business_brand` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=404 DEFAULT CHARSET=utf8;
