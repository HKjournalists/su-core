/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50534
Source Host           : localhost:3306
Source Database       : fsn_new

Target Server Type    : MYSQL
Target Server Version : 50534
File Encoding         : 65001

Date: 2015-05-06 14:15:23
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `report_flow_log`
-- ----------------------------
DROP TABLE IF EXISTS `report_flow_log`;
CREATE TABLE `report_flow_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `test_result_id` bigint(20) DEFAULT NULL COMMENT '报告ID',
  `barcode` varchar(100) DEFAULT NULL COMMENT '条形码',
  `batch_serial_no` varchar(100) DEFAULT NULL COMMENT '批次',
  `service_order` varchar(100) DEFAULT NULL COMMENT '报告编号',
  `handlers` varchar(100) DEFAULT NULL COMMENT '操作者',
  `operation` varchar(255) DEFAULT NULL COMMENT '所做操作',
  `operation_time` datetime DEFAULT NULL COMMENT '操作时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of report_flow_log
-- ----------------------------
INSERT INTO `report_flow_log` VALUES ('1', '64533', 'test', '5', '20150415007', 'testlab', '完成报告从portal撤回操作', '2015-04-30 18:16:58');
INSERT INTO `report_flow_log` VALUES ('2', '64020', '酒乡酒（A9）', '201503311111', 'gg1234', 'testlab', '完成报告从portal撤回操作', '2015-04-30 18:17:12');
INSERT INTO `report_flow_log` VALUES ('3', '64523', '分类', '20140509', 'GC433333353', 'testlab', '完成报告审核通过发布到portal的操作', '2015-04-30 18:17:14');
INSERT INTO `report_flow_log` VALUES ('4', '64521', '555333333666', '20140509', 'GC4333333', 'testlab', '完成报告从portal撤回操作', '2015-04-30 18:19:52');
INSERT INTO `report_flow_log` VALUES ('5', '64520', '99999999992', '20150123', 'GC45435433', 'testlab', '完成报告从portal撤回操作', '2015-04-30 18:19:58');
INSERT INTO `report_flow_log` VALUES ('6', '64513', '', 'fage1', 'fage1', 'testlab', '完成报告审核通过发布到portal的操作', '2015-04-30 18:20:21');
INSERT INTO `report_flow_log` VALUES ('7', '64520', '99999999992', '20150123', 'GC45435433', 'testlab', '完成报告审核通过发布到portal的操作', '2015-04-30 18:20:24');
INSERT INTO `report_flow_log` VALUES ('8', '64311', '', '飞飞飞', 'W76', 'testlab', '完成报告审核通过发布到portal的操作', '2015-04-30 18:21:55');
INSERT INTO `report_flow_log` VALUES ('9', '64313', '4444444444440', 'Apr 10, 2015', '莫非菲菲非', 'testlab', '完成报告审核通过发布到portal的操作', '2015-04-30 18:21:57');
INSERT INTO `report_flow_log` VALUES ('10', '64313', '4444444444440', 'Apr 10, 2015', '莫非菲菲非', 'testlab', '完成报告从portal撤回操作', '2015-04-30 18:24:38');
INSERT INTO `report_flow_log` VALUES ('11', '64520', '99999999992', '20150123', 'GC45435433', 'testlab', '完成报告从portal撤回操作', '2015-04-30 18:24:40');
INSERT INTO `report_flow_log` VALUES ('12', '64513', '', 'fage1', 'fage1', 'testlab', '完成报告从portal撤回操作', '2015-04-30 18:24:41');
INSERT INTO `report_flow_log` VALUES ('13', '64522', '555333333666', '20140509', 'GC43333335', 'testlab', '完成报告从portal撤回操作', '2015-04-30 18:24:42');
INSERT INTO `report_flow_log` VALUES ('14', '64311', '', '飞飞飞', 'W76', 'testlab', '完成报告从portal撤回操作', '2015-04-30 18:30:53');
