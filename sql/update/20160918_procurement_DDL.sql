/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50534
Source Host           : localhost:3306
Source Database       : fsn_stg_20160912

Target Server Type    : MYSQL
Target Server Version : 50534
File Encoding         : 65001

Date: 2016-09-18 09:05:26
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `procurement_info`
-- ----------------------------
DROP TABLE IF EXISTS `procurement_info`;
CREATE TABLE `procurement_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(200) DEFAULT NULL COMMENT '����',
  `provider_id` bigint(20) DEFAULT NULL COMMENT '��Ӧ����ҵid',
  `provider_name` varchar(200) DEFAULT NULL COMMENT '��Ӧ������',
  `format` varchar(50) DEFAULT NULL COMMENT '���',
  `batch` varchar(50) DEFAULT NULL COMMENT '����',
  `procurement_num` int(20) DEFAULT '0' COMMENT '�ɹ�����',
  `procurement_date` datetime DEFAULT NULL COMMENT '�ɹ�����',
  `expire_date` datetime DEFAULT NULL COMMENT '��������',
  `remark` varchar(500) DEFAULT NULL COMMENT '��ע',
  `surplus_num` int(11) DEFAULT '0' COMMENT 'ʣ������',
  `type` int(11) DEFAULT '0' COMMENT '�ɹ�����   1��ԭ����  2����Ӽ�  3����װ����',
  `create_date` datetime DEFAULT NULL COMMENT '��������',
  `creator` varchar(20) DEFAULT NULL COMMENT '������',
  `organization_id` bigint(20) DEFAULT NULL COMMENT '������ҵ����id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of procurement_info
-- ----------------------------

DROP TABLE IF EXISTS `procurement_dispose`;
CREATE TABLE `procurement_dispose` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `procurement_id` bigint(20) DEFAULT NULL COMMENT '�ɹ���Ϣid',
  `procurement_name` varchar(200) DEFAULT NULL COMMENT '�ɹ�����',
  `format` varchar(50) DEFAULT NULL COMMENT '���',
  `batch` varchar(50) DEFAULT NULL COMMENT '����',
  `dispose_num` int(11) DEFAULT '0' COMMENT '��������',
  `dispose_date` datetime DEFAULT NULL COMMENT '����ʱ��',
  `dispose_cause` varchar(200) DEFAULT NULL COMMENT '����ԭ��',
  `dispose_place` varchar(200) DEFAULT NULL COMMENT '����ص�',
  `handler` varchar(50) DEFAULT NULL COMMENT '������',
  `dispose_method` varchar(200) DEFAULT NULL COMMENT '����ʽ',
  `remark` varchar(500) DEFAULT NULL COMMENT '��ע',
  `type` int(11) DEFAULT '0' COMMENT '��������  1��ԭ����  2����Ӽ�',
  `create_date` datetime DEFAULT NULL COMMENT '��������',
  `creator` varchar(20) DEFAULT NULL COMMENT '������',
  `organization_id` bigint(20) DEFAULT NULL COMMENT '������ҵ����id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `procurement_usage_record`;
CREATE TABLE `procurement_usage_record` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `procurement_id` bigint(20) DEFAULT NULL COMMENT '�ɹ���Ϣ��id',
  `procurement_name` varchar(200) DEFAULT NULL COMMENT '�ɹ�����',
  `use_date` date DEFAULT NULL COMMENT 'ʹ������',
  `use_num` int(11) DEFAULT '0' COMMENT 'ʹ������',
  `purpose` varchar(100) DEFAULT NULL COMMENT '��;',
  `remark` varchar(500) DEFAULT NULL COMMENT '��ע',
  `creator` varchar(50) DEFAULT NULL COMMENT '������',
  `create_date` datetime DEFAULT NULL COMMENT '����ʱ��',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS `procurement_info_to_resource`;
CREATE TABLE `procurement_info_to_resource` (
  `procurement_id` bigint(20) NOT NULL,
  `RESOURCE_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`procurement_id`,`RESOURCE_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS `procurement_dispose_to_source`;
CREATE TABLE `procurement_dispose_to_source` (
  `dispose_id` bigint(20) NOT NULL,
  `RESOURCE_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`dispose_id`,`RESOURCE_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;